package data.combat;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.CombatRenderingManager;
import org.boxutil.units.standard.entity.FlareEntity;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class KE_AuthorityLazerEffect implements BeamEffectPlugin {
    private IntervalUtil fireInterval = new IntervalUtil(0.25f, 0.3f);
    private boolean wasZero = true;
    @Override
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
        Color fringecolor = new Color(0, 166, 255, 255);
        Color coreColor = new Color(255, 255, 255, 255);
        Vector2f loc = new Vector2f((float) (beam.getFrom().x+Math.random()*50f), (float) (beam.getFrom().y+Math.random()*50f));
        CombatEntityAPI target = beam.getDamageTarget();
        if (target instanceof ShipAPI && beam.getBrightness() >= 1f) {
            float dur = beam.getDamage().getDpsDuration();
            // needed because when the ship is in fast-time, dpsDuration will not be reset every frame as it should be
            if (!wasZero) dur = 0;
            wasZero = beam.getDamage().getDpsDuration() <= 0;
            fireInterval.advance(dur);

            if (fireInterval.intervalElapsed()) {
                FlareEntity flareEntity = new FlareEntity();
                flareEntity.setLocation(loc);
                flareEntity.setSize(50, 10);
                flareEntity.setFlick(true);
                flareEntity.setFlickWhenPaused(false);
                flareEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                flareEntity.setSmoothDisc();
                flareEntity.setFringeColor(fringecolor);
                flareEntity.setCoreColor(coreColor);
                flareEntity.setCoreAlpha(1.0f);
                flareEntity.setFringeAlpha(1.0f);
                flareEntity.setAdditiveBlend();
                flareEntity.setNoisePower(0.33f);
                flareEntity.autoAspect();
                flareEntity.setGlobalTimer(0.05f, 0.05f, 0.05f);
                CombatRenderingManager.addEntity(BoxEnum.ENTITY_FLARE, flareEntity);
                CombatEntityAPI entity = engine.spawnProjectile(beam.getSource(),
                        beam.getWeapon(),
                        "KE_YZ_ZhengchenRailgun",
                        loc,
                        beam.getWeapon().getCurrAngle(),
                        new Vector2f());
                if (entity instanceof DamagingProjectileAPI) {
                    DamagingProjectileAPI proj = (DamagingProjectileAPI) entity;
                    proj.setDamageAmount(beam.getDamage().getDamage()*0.2f);
                }
            }
        }
    }
}
