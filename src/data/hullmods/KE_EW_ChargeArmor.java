package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class KE_EW_ChargeArmor extends BaseHullMod {
    private static final float EMP_RESISTANCE = 75f;
    private static final float ARMOR_RESISTANCE = 25f;
    private static final float DAMAGE_REDUCTION = 25f;
    private static final float BASE_CHARGE_RECOVERY_PER_SECOND = 0.005f;
    private static final float SMOD_CHARGE_RECOVERY_PER_SECOND = 0.0075f;
    private static final float BASE_CHARGE_LOSS_PER_SECOND = 0.025f;
    private static final float SMOD_CHARGE_LOSS_PER_SECOND = 0.015f;

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        if (!ship.hasListenerOfClass(KE_EW_ChargeArmorListener.class)) {
            boolean sMod = ship.getVariant() != null && ship.getVariant().getSMods().contains(id);
            ship.addListener(new KE_EW_ChargeArmorListener(ship, sMod));
        }
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        /*It has no effect and is mainly used to detect whether the plug-in is included.*/
    }

    public void advanceInCampaign(FleetMemberAPI member, float amount) {
    }

    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return false;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;
        float pads = 5f;
        Color h = Misc.getHighlightColor();
        Color g = Misc.getGrayColor();
        Color Element = new Color(165, 63, 44, 255);
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        /*Level 4: Coarse food (139, 115, 85),
        Regular meals (107, 142, 35),
        Fine food (210, 105, 30),
        Hua Yan (184, 134, 11)*/
        tooltip.addPara("Monolith Ordnance Group developed this armor package from charged plating studies, rail-factory hardening methods, and Hegemony battle records.\n" +
                        "The installed lattice stores a defensive charge across the armor skin. As long as the charge holds, incoming hits lose force before they can fully bite into the hull.",
                pad, Element,
                "Monolith Ordnance Group");
        tooltip.addPara("Recovered Hegemony maintenance traffic describes the system as expensive, temperamental, and worth every credit after the third ambush of the month.",
                pads, g);

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara(" %sAdds an armor charge system%s ;",
                pad,
                h,
                " #", " ");
        tooltip.addPara(" %sAll defensive effects scale with current charge strength.%s If charge reaches 0, the system shuts down briefly before recovering.",
                pad,
                bad,
                " #", "");
        tooltip.addPara(" %sAt full charge, EMP damage taken is reduced by %s and armor damage taken is reduced by %s.%s Charge recovery is %s per second.",
                pad,
                good,
                " #",
                "75%","25%"," ","0.5%");
        tooltip.addPara(" %sAt full charge, hull and armor hits deal %s less damage.%s Taking armor or hull hits drains charge.",
                pad,
                good,
                " #","25%"," ");
        tooltip.addPara("S-mod effect: charge recovery improves to %s per second, and charge loss from hits is reduced.",
                pad,
                good,
                "0.75%");
    }

    public class KE_EW_ChargeArmorListener
            implements AdvanceableListener, DamageTakenModifier, DamageDealtModifier {
        public final String id = "KE_EW_ChargeArmor_listener_effect";

        private boolean hasattacked = false;
        private boolean ShouldCooldown = false;
        private ShipAPI ship;
        private float ChargeLevel = 1f;
        private boolean sMod = false;
        private IntervalUtil Cooldown = new IntervalUtil(3f,3f);

        public KE_EW_ChargeArmorListener(ShipAPI ship, boolean sMod) {
            this.ship = ship;
            this.sMod = sMod;
        }
        @Override
        public void advance(float amount) {
            CombatEngineAPI engine = Global.getCombatEngine();
            MutableShipStatsAPI stats = ship.getMutableStats();
            stats.getEmpDamageTakenMult().modifyPercent(id, -ChargeLevel * EMP_RESISTANCE);
            stats.getArmorDamageTakenMult().modifyPercent(id, -ChargeLevel * ARMOR_RESISTANCE);
//            engine.addFloatingText(
//                    ship.getLocation(),
//                    ""+amount+"\n"+ChargeLevel*100f+"%\n",
//                    100f,
//                    Color.WHITE,
//                    ship,
//                    0.01f,
//                    0.01f
//            );
            if(!ShouldCooldown) {
                if (hasattacked) {
                    float chargeLoss = sMod ? SMOD_CHARGE_LOSS_PER_SECOND : BASE_CHARGE_LOSS_PER_SECOND;
                    ChargeLevel = Math.max(ChargeLevel - chargeLoss * amount, 0f);
                    hasattacked = false;
                }
                float chargeRecovery = sMod ? SMOD_CHARGE_RECOVERY_PER_SECOND : BASE_CHARGE_RECOVERY_PER_SECOND;
                ChargeLevel = Math.min(ChargeLevel + chargeRecovery * amount, 1f);

                if(ChargeLevel <= 0){
                    ShouldCooldown = true;
                }
            }
            if(ShouldCooldown) {
                Cooldown.advance(amount);
                if(Cooldown.intervalElapsed()){
                    ShouldCooldown = false;
                    ChargeLevel += 0.05f;
                }
            }
            if (ship == Global.getCombatEngine().getPlayerShip()) {
                String statusIcon =
                        ChargeLevel > 0
                                ? "graphics/icons/hullsys/fortress_shield.png"
                                : "graphics/icons/hullsys/damper_field.png";
                String statusTitle = "Rechargeable armor system";
                StringBuilder statusDesc = new StringBuilder();
                if (ChargeLevel > 0) {
                    statusDesc
                            .append("Charging strength: ")
                            .append((int)( ChargeLevel * 100))
                            .append("%");
                    if (sMod) {
                        statusDesc.append(" (stabilized)");
                    }
                }
                if(ChargeLevel <= 0){
                    statusDesc
                            .append("The charging system is offline");
                }
                Global.getCombatEngine()
                        .maintainStatusForPlayerShip(
                                "KE_EW_ChargeArmor", statusIcon, statusTitle, statusDesc.toString(), false);
            }
        }

        @Override
        public String modifyDamageDealt(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
            return "";
        }

        @Override
        public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
            if(param instanceof DamagingProjectileAPI){
                ShipAPI source = null;
                if(((DamagingProjectileAPI) param).getSource() != null){
                    source = ((DamagingProjectileAPI) param).getSource();
                }
                if(source != null){
                    hasattacked = source.getOwner() != ship.getOwner()&&!shieldHit;
                }
            }
            if(param instanceof BeamAPI){
                if(((BeamAPI) param).getSource() != null){
                    hasattacked = ((BeamAPI) param).getSource().getOwner() != ship.getOwner()&&!shieldHit;
                }
            }
            if(param instanceof ShipAPI){
                hasattacked = ((ShipAPI) param).getOwner() != ship.getOwner()&&!shieldHit;
            }
            if(shieldHit||ChargeLevel <= 0f)return null;
            damage.getModifier().modifyPercent(id, -ChargeLevel * DAMAGE_REDUCTION);
            addDamageVisuals(point, damage);
            return null;
        }
        private void addDamageVisuals(Vector2f point, DamageAPI damage) {
            CombatEngineAPI engine = Global.getCombatEngine();
            if (engine == null) return;
            Color hitCore = new Color(9, 236, 228, 255);
            Color hitFringe = new Color(66, 156, 211, 255);
            engine.addFloatingDamageText(point, damage.getDamage() * 0.25f, new Color(97, 251, 226), ship, null);
            engine.addHitParticle(point, new Vector2f(0, 0), 12f, 15f, 0.25f, hitCore);
            engine.addHitParticle(point, new Vector2f(0, 0), 18f, 10f, 0.5f, hitFringe);
            engine.addHitParticle(
                    point, new Vector2f(0, 0), 25f, 7.5f, 0.75f, new Color(117, 135, 253, 255));

            Color CoreColor = new Color(255, 255, 255, 89);
            Color FringeColor = new Color(101, 251, 251, 74);
            for (int i = 0; i < 3; i++) {
                Vector2f start = point;  // Starting point (hit point)

                Vector2f originalTarget = ship.getLocation();
                if(damage.getStats() != null){
                    originalTarget = damage.getStats().getEntity() != null
                            ? damage.getStats().getEntity().getLocation()
                            : ship.getLocation();
                }

                // Calculate the original direction (from the starting point to the original target)
                float dx = originalTarget.x - start.x;
                float dy = originalTarget.y - start.y;
                float originalAngle = (float) Math.atan2(dy, dx);

                // Random offset ±30° (radians)
                float randomOffset = (float) ((Math.random() - 0.5) * Math.toRadians(60));
                float newAngle = originalAngle + randomOffset;

                // Fixed target distance is 10
                float fixedDistance = (float) (ship.getCollisionRadius()*0.5f*Math.min(1f,Math.random()));
                float targetX = start.x + fixedDistance * (float) Math.cos(newAngle);
                float targetY = start.y + fixedDistance * (float) Math.sin(newAngle);
                Vector2f newTarget = new Vector2f(targetX, targetY);

                EmpArcEntityAPI arc = (EmpArcEntityAPI) engine.spawnEmpArcVisual(
                        point,
                        ship,
                        newTarget,
                        null,
                        0.1f,
                        CoreColor,
                        FringeColor);
                arc.setWarping(0.01f);
                arc.setSingleFlickerMode(true);

//                // Generate arc effects
//                EmpArcEntityAPI arc = (EmpArcEntityAPI) engine.spawnEmpArcVisual(
//                        start, // starting point
//                        ship, // launch entity
//                        newTarget, // New target point (fixed 10 from the starting point)
//                        null, // fourth parameter
//                        0.5f, // thickness
//                        color, // main color
//                        new Color(255, 255, 255, 255), // Edge color
//                        params
//                );
////            arc.setCoreWidthOverride(40f);
//                arc.setRenderGlowAtStart(false);
//                arc.setFadedOutAtStart(true);
//                arc.setSingleFlickerMode(false);

            }
        }
    }
}
