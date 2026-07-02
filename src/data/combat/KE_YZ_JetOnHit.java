package data.combat;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.combat.NegativeExplosionVisual;
import com.fs.starfarer.api.impl.combat.RiftCascadeMineExplosion;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.combat.util.KE_GraphicsLibEffects;
import org.boxutil.units.standard.entity.FlareEntity;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class KE_YZ_JetOnHit implements OnHitEffectPlugin {
    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        DamagingExplosionSpec spec = new DamagingExplosionSpec(0.1f,
                15f,
                7f,
                projectile.getDamage().getDamage(),
                projectile.getDamage().getDamage()*0.5f,
                CollisionClass.MISSILE_NO_FF,
                CollisionClass.MISSILE_NO_FF,
                0.1f,
                1f,
                0.1f,
                35,
                new Color(40, 3, 159, 97),
                new Color(127, 101, 218, 115));
        NegativeExplosionVisual.NEParams neEffect = new NegativeExplosionVisual.NEParams();
        neEffect.fadeOut = 0.25f;
        neEffect.radius = 15f;
        neEffect.thickness = 7.5f;
        neEffect.color = new Color(57, 14, 213, 255);
        neEffect.underglow = new Color(34, 3, 81, 255);
        CombatEntityAPI visual = engine.addLayeredRenderingPlugin(new NegativeExplosionVisual(neEffect));
        visual.getLocation().set(point);
        KE_GraphicsLibEffects.addRipple(point, new Color(92, 38, 255, 220), 260f, 1.4f, 140f, 45f);
        KE_GraphicsLibEffects.addWave(point, 90f, 25f, 0.03f, 0.28f, true);
        engine.spawnDamagingExplosion(spec, projectile.getSource(), point);
    }
}
