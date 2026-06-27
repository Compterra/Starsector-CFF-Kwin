package data.combat;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class KE_YZ_MicroEMPOnHit implements OnHitEffectPlugin {
    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        ShipAPI source = projectile.getSource();
        engine.spawnEmpArc(
                source,
                point,
                target,
                target,
                DamageType.FRAGMENTATION,
                projectile.getDamageAmount(),
                projectile.getEmpAmount(),
                100000f,
                "tachyon_lance_emp_impact",
                7f,
                new Color(0, 148, 253, 255),
                new Color(255, 255, 255, 255));
    }
}