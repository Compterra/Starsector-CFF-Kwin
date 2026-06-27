package data.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.impl.campaign.ExplosionEntityPlugin;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.combat.entities.DamagingExplosion;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class KE_TA_BiomassErosion implements OnHitEffectPlugin,OnFireEffectPlugin {
    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        ShipAPI source=null;
        source = projectile.getSource();
        if(projectile.getProjectileSpecId().equals("KE_TA_AcheBiomassMG_shot")){
            engine.addHitParticle(point,
                    new Vector2f(),
                    15f,
                    1f,
                    0.15f,
                    new Color(216, 236, 34, 115));
            engine.applyDamage(
                    target,
                    point,
                    15f,
                    DamageType.FRAGMENTATION,
                    0f,
                    false,
                    false,
                    projectile.getSource() != null? projectile.getSource() : null
            );
        }
        if(target instanceof ShipAPI && !shieldHit){
            ShipAPI ship = (ShipAPI) target;
            BiomassErosionEffect_Listener listener;
            if(!ship.hasListenerOfClass(BiomassErosionEffect_Listener.class)){
                listener = new BiomassErosionEffect_Listener(ship, source);
                ship.addListener(listener);
            } else {
                listener = ship.getListeners(BiomassErosionEffect_Listener.class).get(0);
            }
            listener.addFromProjectile(projectile);
        }
    }

    @Override
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        String weaponID = weapon.getId();
        Vector2f SL = new Vector2f(0,0);
        switch (weaponID){
            case "KE_TA_AcheBiomassMG":
                SL.set(0.05f,0.05f);
                break;
            case "KE_TA_ErosionBiomassMissile_fighter":
                SL.set(2,6);
                break;
            case "KE_TA_ErosionBiomassMissile_bomber":
                SL.set(3,15);
                break;
            case "KE_TA_ErosionBiomassMissile_S_fighter":
                SL.set(2,4);
                break;
        }
        if(!projectile.getCustomData().containsKey("BiomassErosion")){
            projectile.setCustomData("BiomassErosion", SL);
        }
    }

    public static class BiomassErosionEffect_Listener implements AdvanceableListener {

        ShipAPI ship;
        ShipAPI source;
        public BiomassErosionEffect_Listener(ShipAPI ship, ShipAPI source) {
            this.ship = ship;
            this.source = source;
        }
        private float Stacks = 0;
        private float Level = 0;
        private IntervalUtil clock = new IntervalUtil(1f, 1f);
        private IntervalUtil endclock = new IntervalUtil(20f, 20f);
        private String id = "KE_TA_BiomassErosion";

        public void addFromProjectile(DamagingProjectileAPI projectile) {
            if (projectile == null || !projectile.getCustomData().containsKey("BiomassErosion")) return;
            Vector2f SL = (Vector2f) projectile.getCustomData().get("BiomassErosion");
            Stacks = Math.min(150, Stacks + SL.x);
            Level = Math.min(300, Level + SL.y);
            if (projectile.getSource() != null) {
                source = projectile.getSource();
            }
        }

        private void cleanup() {
            ship.getMutableStats().getArmorDamageTakenMult().unmodify(id);
            ship.removeListener(this);
        }

        @Override
        public void advance(float amount) {
            CombatEngineAPI engine = Global.getCombatEngine();
            MutableShipStatsAPI stats = ship.getMutableStats();
            float r = ship.getCollisionRadius() * 0.5f;
            DamagingExplosionSpec spec = new DamagingExplosionSpec(
                    0.15f,
                    15f,
                    5f,
                    Level*2f,
                    Level*0.5f,
                    CollisionClass.PROJECTILE_FF,
                    CollisionClass.PROJECTILE_FF,
                    0.5f,
                    10f,
                    0.2f,
                    25,
                    new Color(236, 105, 34, 200),
                    new Color(234, 83, 83, 200)
            );
            spec.setDamageType(DamageType.FRAGMENTATION);

            if(Stacks <=0){
                stats.getArmorDamageTakenMult().unmodify(id);
                endclock.advance(amount);
                if(endclock.intervalElapsed()){
                    cleanup();
                }
            }
            if(Stacks >0){
                stats.getArmorDamageTakenMult().modifyPercent(id, Level*0.1f);
                endclock.setElapsed(0);
                clock.advance(amount);
                if(clock.intervalElapsed()){
                    engine.spawnDamagingExplosion(spec,
                            source,
                            new Vector2f(
                                    ship.getLocation().x + (float)(Math.random() - 0.5) * 2 * r,
                                    ship.getLocation().y + (float)(Math.random() - 0.5) * 2 * r
                            ));
                    Stacks--;
                }
            }
//            if(Stacks >= 2) {
//                engine.addFloatingText(
//                        ship.getLocation(),
//                        "Biomass Erosion: " + Stacks + " stacks, " + Level + " level",
//                        30f,
//                        Color.WHITE,
//                        ship,
//                        1f,
//                        0.05f);
//            }
            if(!ship.isAlive()||ship.isHulk()||ship.isExpired()){
                cleanup();
            }
        }
    }
}
