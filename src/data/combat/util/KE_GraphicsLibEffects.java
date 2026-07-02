package data.combat.util;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import java.awt.Color;
import org.dark.shaders.distortion.DistortionShader;
import org.dark.shaders.distortion.RippleDistortion;
import org.dark.shaders.distortion.WaveDistortion;
import org.dark.shaders.light.LightShader;
import org.dark.shaders.light.StandardLight;
import org.lwjgl.util.vector.Vector2f;

public final class KE_GraphicsLibEffects {
    private KE_GraphicsLibEffects() {
    }

    public static void addSoftLight(Vector2f location, Vector2f velocity, Color color, float size, float intensity,
                                    float lifetime, float fadeOut) {
        if (location == null || color == null) return;
        StandardLight light = new StandardLight(new Vector2f(location),
                velocity == null ? new Vector2f() : new Vector2f(velocity),
                new Vector2f(), null);
        light.setColor(color);
        light.setSize(size);
        light.setIntensity(intensity);
        light.setLifetime(lifetime);
        light.setAutoFadeOutTime(fadeOut);
        LightShader.addLight(light);
    }

    public static void addProjectileLight(DamagingProjectileAPI projectile, Color color, float size, float intensity,
                                          float lifetime, float fadeOut) {
        if (projectile == null) return;
        addSoftLight(projectile.getLocation(), projectile.getVelocity(), color, size, intensity, lifetime, fadeOut);
    }

    public static void addRipple(Vector2f location, Color lightColor, float lightSize, float lightIntensity,
                                 float rippleSize, float rippleIntensity) {
        if (location == null) return;
        addSoftLight(location, null, lightColor, lightSize, lightIntensity, 0.04f, 0.35f);

        RippleDistortion ripple = new RippleDistortion(new Vector2f(location), new Vector2f());
        ripple.setSize(rippleSize);
        ripple.setIntensity(rippleIntensity);
        ripple.setLifetime(0.02f);
        ripple.setAutoFadeSizeTime(0.35f);
        ripple.setAutoFadeIntensityTime(0.35f);
        ripple.setFrameRate(120f);
        DistortionShader.addDistortion(ripple);
    }

    public static void addWave(Vector2f location, float size, float intensity, float lifetime, float fadeTime,
                               boolean inverted) {
        if (location == null) return;
        WaveDistortion wave = new WaveDistortion(new Vector2f(location), new Vector2f());
        wave.setSize(size);
        wave.setIntensity(intensity);
        wave.setLifetime(lifetime);
        wave.setAutoFadeSizeTime(fadeTime);
        wave.setAutoFadeIntensityTime(fadeTime);
        wave.flip(inverted);
        DistortionShader.addDistortion(wave);
    }

    public static boolean isInViewport(CombatEngineAPI engine, CombatEntityAPI entity, float margin) {
        return engine != null && entity != null && engine.getViewport().isNearViewport(entity.getLocation(), margin);
    }
}
