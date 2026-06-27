package data.combat;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class KE_YZ_IonPDPulse_fighter_OnHit implements OnHitEffectPlugin {
    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (!(target instanceof ShipAPI)) return;

        ShipAPI ship = (ShipAPI) target;
        if (ship.getHullSize() != ShipAPI.HullSize.FIGHTER) return;

        engine.spawnEmpArc(
                projectile.getSource(),
                point,
                target,
                target,
                DamageType.ENERGY,
                projectile.getDamageAmount(),
                projectile.getEmpAmount(),
                100000f,
                "tachyon_lance_emp_impact",
                3f,
                new Color(85, 123, 235, 255),
                new Color(255, 255, 255, 255)
        );
    }
}