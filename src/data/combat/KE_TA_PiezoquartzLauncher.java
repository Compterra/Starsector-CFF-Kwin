package data.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.combat.dweller.DwellerShroud;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KE_TA_PiezoquartzLauncher implements OnHitEffectPlugin, OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
    protected java.util.List<FiredLightningProjectile> fired = new ArrayList<>();
    public static Color RIFT_LIGHTNING_COLOR = new Color(0, 128, 255,255);
    public static float RIFT_LIGHTNING_SPEED = 10000f;

    //	public static String RIFT_LIGHTNING_PROJ_TAG = "rift_lightning_proj_tag";
    public static String RIFT_LIGHTNING_DAMAGE_REMOVER = "KE_TA_rift_lightning_damage_remover";
    public static String RIFT_LIGHTNING_FIRED_TAG = "KE_TA_rift_lightning_fired_tag";
    public static String RIFT_LIGHTNING_SOURCE_WEAPON = "KE_TA_rift_lightning_source_weapon";

    public static class FiredLightningProjectile {
        public DamagingProjectileAPI projectile;
    }
    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
//		if (!fired.isEmpty()) {
//			System.out.println("FIRED");
//		}
        List<FiredLightningProjectile> remove = new ArrayList<>();

        float maxRange = weapon.getRange();
        for (FiredLightningProjectile data : fired) {
            float dist = Misc.getDistance(data.projectile.getSpawnLocation(), data.projectile.getLocation());
            boolean firedAlready = data.projectile.getCustomData().containsKey(RIFT_LIGHTNING_FIRED_TAG);
            if (dist > maxRange || firedAlready) {
                remove.add(data);
                if (!firedAlready) {
                    fireArc(data.projectile, weapon, null, null);
                }
            }
        }
        fired.removeAll(remove);
    }

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

        WeaponAPI weapon = (WeaponAPI) projectile.getCustomData().get(RIFT_LIGHTNING_SOURCE_WEAPON);
        if (weapon == null) return;

        fireArc(projectile, weapon, point, target);
    }
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
//		if (weapon.getShip() != null &&
//				!weapon.getShip().hasListenerOfClass(RiftLightningBaseDamageNegator.class)) {
//			weapon.getShip().addListener(new RiftLightningBaseDamageNegator());
//		}
        //projectile.setCustomData(RIFT_LIGHTNING_PROJ_TAG, true);

        projectile.getDamage().getModifier().modifyMult(RIFT_LIGHTNING_DAMAGE_REMOVER, 0f);
        projectile.setCustomData(RIFT_LIGHTNING_SOURCE_WEAPON, weapon);

        FiredLightningProjectile data = new FiredLightningProjectile();
        data.projectile = projectile;
        fired.add(data);
    }
    public static void fireArc(DamagingProjectileAPI projectile, WeaponAPI weapon, Vector2f point, CombatEntityAPI target) {
        boolean firedAlready = projectile.getCustomData().containsKey(RIFT_LIGHTNING_FIRED_TAG);
        if (firedAlready) return;

        projectile.setCustomData(RIFT_LIGHTNING_FIRED_TAG, true);

        CombatEngineAPI engine = Global.getCombatEngine();

        ShipAPI ship = weapon.getShip();
        if (ship == null) return;

        //Vector2f from = weapon.getFirePoint(0);
        Vector2f from = weapon.getFirePoint(0);


        float dist = Float.MAX_VALUE;
        if (point != null) dist = Misc.getDistance(from, point);

        float maxRange = weapon.getRange();
        if (dist > maxRange || point == null) {
            dist = maxRange * (0.5f + 0.5f * (float) Math.random());
            if (projectile.didDamage()) {
                dist = maxRange;
            }
            point = Misc.getUnitVectorAtDegreeAngle(projectile.getFacing());
            point.scale(dist);
            Vector2f.add(point, from, point);
        }

        float arcSpeed = RIFT_LIGHTNING_SPEED;



        EmpArcEntityAPI.EmpArcParams params = new EmpArcEntityAPI.EmpArcParams();
        params.segmentLengthMult = 0.15f;
        params.zigZagReductionFactor = 0.15f;
        params.fadeOutDist = 10f;
        params.minFadeOutMult = 0.1f;
//		params.flickerRateMult = 0.7f;
        params.flickerRateMult = 0.3f;
//		params.flickerRateMult = 0.05f;
//		params.glowSizeMult = 3f;
//		params.brightSpotFullFraction = 0.5f;


        params.movementDurOverride = Math.max(0.05f, dist / arcSpeed);

        //Color color = weapon.getSpec().getGlowColor();
        Color color = RIFT_LIGHTNING_COLOR;
        EmpArcEntityAPI arc = (EmpArcEntityAPI)engine.spawnEmpArcVisual(from, ship, point, null,
                4f, // thickness
                color,
                new Color(255,255,255,255),
                params
        );
//        arc.setCoreWidthOverride(40f);

        arc.setRenderGlowAtStart(false);
        arc.setFadedOutAtStart(true);
        arc.setSingleFlickerMode(true);
        createDamage(ship, point, projectile,target);
//        spawnMine(ship, point, params.movementDurOverride * 0.8f); // - 0.05f);


//        if (shroud != null) {
//            DwellerShroud.DwellerShroudParams shroudParams = shroud.getShroudParams();
//            params = new EmpArcEntityAPI.EmpArcParams();
//            params.segmentLengthMult = 4f;
//            params.glowSizeMult = 4f;
//            params.flickerRateMult = 0.5f + (float) Math.random() * 0.5f;
//            params.flickerRateMult *= 1.5f;
//
//            //Color fringe = shroudParams.overloadArcFringeColor;
//            Color fringe = color;
//            Color core = Color.white;
//
//            float thickness = shroudParams.overloadArcThickness;
//
//            //Vector2f to = Misc.getPointAtRadius(from, 1f);
//
//            float angle = Misc.getAngleInDegrees(from, ship.getLocation());
//            angle = angle + 90f * ((float) Math.random() - 0.5f);
//            Vector2f dir = Misc.getUnitVectorAtDegreeAngle(angle);
//            dist = shroudParams.maxOffset;
//            dist = dist * 0.5f + dist * 0.5f * (float) Math.random();
//            //dist *= 1.5f;
//            dist *= 0.5f;
//            dir.scale(dist);
//            Vector2f to = Vector2f.add(from, dir, new Vector2f());
//
//            arc = (EmpArcEntityAPI)engine.spawnEmpArcVisual(
//                    from, ship, to, ship, thickness, fringe, core, params);
//
//            arc.setCoreWidthOverride(shroudParams.overloadArcCoreThickness);
//            arc.setSingleFlickerMode(false);
//            //arc.setRenderGlowAtStart(false);
//        }

    }
    public static void createDamage(ShipAPI source, Vector2f Loc, DamagingProjectileAPI proj,CombatEntityAPI target) {
        if (target == null) {
            return;
        }
        CombatEngineAPI engine = Global.getCombatEngine();
        engine.applyDamage(target, Loc,proj.getBaseDamageAmount(),proj.getDamageType(), proj.getEmpAmount(), false, false, source);
//        CombatEngineAPI engine = Global.getCombatEngine();
//
//
//        //Vector2f currLoc = mineLoc;
//        MissileAPI mine = (MissileAPI) engine.spawnProjectile(source, null,
//                "rift_lightning_minelayer",
//                mineLoc,
//                (float) Math.random() * 360f, null);
//        if (source != null) {
//            Global.getCombatEngine().applyDamageModifiersToSpawnedProjectileWithNullWeapon(
//                    source, WeaponAPI.WeaponType.ENERGY, false, mine.getDamage());
//        }
//
//
//        float fadeInTime = 0.05f;
//        mine.getVelocity().scale(0);
//        mine.fadeOutThenIn(fadeInTime);
//
//        float liveTime = Math.max(delay, 0f);
//        mine.setFlightTime(mine.getMaxFlightTime() - liveTime);
//        mine.addDamagedAlready(source);
//        mine.setNoMineFFConcerns(true);
//        if (liveTime <= 0.016f) {
//            mine.explode();
//        }
    }
}
