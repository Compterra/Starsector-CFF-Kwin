package data.hullmods;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.WeakHashMap;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import data.combat.KE_YZ_EnergyParticle.Impl.KE_YZ_Upgrade_MEM;
import data.combat.KE_YZ_EnergyParticle.Impl.KE_YZ_Upgrade_MS;
import data.combat.KE_YZ_EnergyParticle.Impl.KE_YZ_Upgrade_ecm;
import data.combat.KE_YZ_EnergyParticle.Impl.KE_YZ_Upgrade_safetyoverrides;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleUpgrade;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleState;
import org.lwjgl.util.vector.Vector2f;

public class KE_YZ_EnergySwarmCore extends BaseHullMod {

    private static final WeakHashMap<ShipAPI, ShipState> GLOBAL_SHIP_STATE_CACHE = new WeakHashMap<>();
    private static final Object CACHE_LOCK = new Object();
    private static final float SMOD_SWARM_CAP_MULT = 1.2f;
    private IntervalUtil Clock = new IntervalUtil(0.15f, 0.15f);
    private boolean Started = false;
    // Change ShipState to public static and add getter
    public static class ShipState {
        int maxParticles = 15;
        Stack<Integer> availableSlots = new Stack<>();
        List<MissileAPI> children = new ArrayList<>();
        IntervalUtil clock2 = new IntervalUtil(0.1f, 0.1f);

        public List<MissileAPI> getChildrenCopy() {
            return new ArrayList<>(children);
        }
    }
    private static final HashMap<HullSize, Integer> SwarmCoreSize = new HashMap<>();
    static {
        SwarmCoreSize.put(HullSize.FIGHTER, 5);
        SwarmCoreSize.put(HullSize.FRIGATE, 25);
        SwarmCoreSize.put(HullSize.DESTROYER, 40);
        SwarmCoreSize.put(HullSize.CRUISER, 65);
        SwarmCoreSize.put(HullSize.CAPITAL_SHIP, 90);
    }

    private int getMaxParticles(ShipAPI ship, String id) {
        int maxParticles = SwarmCoreSize.get(ship.getHullSize());
        if (ship.getHullSpec().hasTag("moci_ms")) {
            maxParticles = 20;
        }
        if (ship.getHullSpec().hasTag("mem_mech")) {
            maxParticles = 10;
        }
        if (ship.getVariant() != null && ship.getVariant().getSMods().contains(id)) {
            maxParticles = Math.round(maxParticles * SMOD_SWARM_CAP_MULT);
        }
        return maxParticles;
    }

    // Add public static method
    public static ShipState getShipState(ShipAPI ship) {
        synchronized (CACHE_LOCK) {
            return GLOBAL_SHIP_STATE_CACHE.get(ship);
        }
    }

    // Initialize 15 slots
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        synchronized (CACHE_LOCK) {
            if (GLOBAL_SHIP_STATE_CACHE.containsKey(ship)) return;

            ShipState state = new ShipState();
            state.maxParticles = getMaxParticles(ship, id);
            for (int i = 0; i < state.maxParticles; i++) {
                state.availableSlots.push(i);
            }
            GLOBAL_SHIP_STATE_CACHE.put(ship, state);
            ship.getCustomData().put("KE_YZ_EnergySwarmCore_State", state);
        }
    }

    // Run every frame
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine == null || engine.isPaused() || !ship.isAlive()) return;
        ShipState state;
        synchronized (CACHE_LOCK) {
            state = GLOBAL_SHIP_STATE_CACHE.get(ship);
            if (state == null) {
                state = new ShipState();
                state.maxParticles = SwarmCoreSize.get(ship.getHullSize());
                for (int i = 0; i < state.maxParticles; i++) state.availableSlots.push(i);
                GLOBAL_SHIP_STATE_CACHE.put(ship, state);
            }
        }

        updateChildren(ship, engine, state, amount);

    }

    // ============== Core fix: slot recycling + upper limit control ==============
    private void updateChildren(ShipAPI ship, CombatEngineAPI engine, ShipState state, float amount) {
        state.clock2.advance(amount);

        // 1. Repair: Strictly detect failed missiles and recycle slots (solve infinite generation)
        List<Integer> slotsToRecycle = new ArrayList<>();
        for (int i = 0; i < state.children.size(); i++) {
            MissileAPI m = state.children.get(i);
            // Judgment: The missile is empty/destroyed/expired/disappears → the slot must be recycled
            boolean invalid = (m == null || !engine.isEntityInPlay(m) || m.isExpired()
                    || m.isFading() || m.isFizzling() || m.getHitpoints() <= 0);
            if (invalid) {
                slotsToRecycle.add(i);
                state.children.set(i, null);
            }
        }

        // 2. Fix: Return invalid slots to the available pool (key!)
        for (int slot : slotsToRecycle) {
            if (!state.availableSlots.contains(slot)) {
                state.availableSlots.push(slot);
            }
        }

        // 3. Mandatory upper limit
        int aliveCount = 0;
        for (MissileAPI m : state.children) {
            if (m != null && engine.isEntityInPlay(m)) aliveCount++;
        }
        if (aliveCount >= state.maxParticles) {
            state.availableSlots.clear();
            return;
        }

        // 4. Only generated if there is a vacancy + the clock is up →
        if (!state.availableSlots.isEmpty() && state.clock2.intervalElapsed()) {
//            Clock.advance(amount);
//            if(Clock.intervalElapsed()) {
//                spawnChildMissile(ship, engine, state);
//            }
//            if(!Started){
//                spawnChildMissile(ship, engine, state);
//            }
            spawnChildMissile(ship, engine, state);
        }
        if(state.availableSlots.isEmpty()){
            Started = true;
        }
    }

    // The logic of generating missiles remains unchanged
    private void spawnChildMissile(ShipAPI ship, CombatEngineAPI engine, ShipState state) {
        List<KE_YZ_EnergyParticleUpgrade> upgrades = new ArrayList<>();
        ShipVariantAPI variant = ship.getVariant();

        if (variant.hasHullMod("safetyoverrides")) {
            upgrades.add(new KE_YZ_Upgrade_safetyoverrides());
        }else {
            upgrades.remove(new KE_YZ_Upgrade_safetyoverrides());
        }
        if (variant.hasHullMod("ecm")) {
            upgrades.add(new KE_YZ_Upgrade_ecm());
        }else {
            upgrades.remove(new KE_YZ_Upgrade_ecm());
        }
        if(ship.getHullSpec().hasTag("moci_ms")){
            upgrades.add(new KE_YZ_Upgrade_MS());
        }else {
            upgrades.remove(new KE_YZ_Upgrade_MS());
        }
        if (ship.getHullSpec().hasTag("mem_mech")) {
            upgrades.add(new KE_YZ_Upgrade_MEM());
        }else {
            upgrades.remove(new KE_YZ_Upgrade_MEM());
        }
        int slot = state.availableSlots.pop();
        MissileAPI missile = null;
        if(ship.getHullSpec().hasTag("moci_ms")){
            missile = (MissileAPI) engine.spawnProjectile(
                    ship, null, "KE_YZ_MS_EnergyParticle",
                    ship.getLocation(), (float) Math.random()*360f, new Vector2f(1f,2f)
            );
        }
        if(ship.getHullSpec().hasTag("mem_mech")){
            missile = (MissileAPI) engine.spawnProjectile(
                    ship, null, "KE_YZ_MEM_EnergyParticle",
                    ship.getLocation(), (float) Math.random()*360f, new Vector2f(1f,2f)
            );
        }
        if(!ship.getHullSpec().hasTag("moci_ms")&&!ship.getHullSpec().hasTag("mem_mech")){
            missile = (MissileAPI) engine.spawnProjectile(
                    ship, null, "KE_YZ_EnergyParticle",
                    ship.getLocation(), (float) Math.random()*360f, new Vector2f(1f,2f)
            );
        }
        while (state.children.size() <= slot) state.children.add(null);
        state.children.set(slot, missile);

        KE_YZ_EnergyParticleState ps = new KE_YZ_EnergyParticleState();
        ps.setRole(KE_YZ_EnergyParticleState.ParticleRole.DEFENDER);
        missile.setCustomData("KE_YZ_SourceShip", ship);
        missile.setCustomData("KE_YZ_EnergyParticle", ps.getRole());
        missile.setCustomData("KE_YZ_EnergyParticle_TargetShip", ship);
        missile.setCustomData("KE_YZ_EnergyParticle_Upgrades", upgrades);
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) { return true; }
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) { return null; }
    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {return false;}
    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;
        Color h = Misc.getHighlightColor();
        Color f = new Color(139, 115, 85, 255);
        Color FabriqueOrbitale = new Color(25, 73, 151, 255);
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        /*Level 4: Coarse food (139, 115, 85),
        Regular meals (107, 142, 35),
        Fine food (210, 105, 30),
        Hua Yan (184, 134, 11)*/
        tooltip.addPara("Yunze General Ordnance built this core from classified swarm-control research and half-trusted battlefield data.\n" +
                        "Once installed, the ship maintains a defensive cloud of energy swarms that intercept threats and harass nearby targets. Compatible hulls and systems can retune the swarm package into specialized patterns.\n" +
                        "Yunze accepted the contract under pressure: in the modern Sector, %s is no longer optional.",
                pad, bad,
                "survivability");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara(" %sMaximum maintained energy swarms scale with hull size%s ;",
                pad,
                good,
                " #","25/40/65/90");
        tooltip.addPara(" %sS-mod%s: increases the maintained swarm limit by %s.",
                pad,
                good,
                " #", "20%");
    }
}
