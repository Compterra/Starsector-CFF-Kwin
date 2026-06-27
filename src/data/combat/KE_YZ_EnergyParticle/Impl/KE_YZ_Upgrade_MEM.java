package data.combat.KE_YZ_EnergyParticle.Impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleUpgrade;
import org.boxutil.base.api.InstanceDataAPI;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.CombatRenderingManager;
import org.boxutil.units.standard.attribute.Instance2Data;
import org.boxutil.units.standard.entity.FlareEntity;
import org.boxutil.units.standard.entity.SpriteEntity;
import org.boxutil.util.TrigUtil;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;

public class KE_YZ_Upgrade_MEM implements KE_YZ_EnergyParticleUpgrade {
    private float time = 0f;
    private float num = 0f;
    @Override
    public void apply(float amount, CombatEntityAPI target, MissileAPI missile, CombatEngineAPI engine) {
        CombatEntityAPI enemy = AIUtils.getNearestEnemy(missile);
        Vector2f loc = missile.getLocation();
        time += amount;
        if (enemy != null && enemy instanceof ShipAPI) {
            if (Misc.getDistance(missile.getLocation(), enemy.getLocation()) <= 1500f && time >= 0.2f) {
                for (int i = 0; i < 1; i++) {
                    // ---- Emit flash (brown) ----
                    FlareEntity spawnFlare = new FlareEntity();
                    spawnFlare.setSmoothDisc();
                    spawnFlare.setSize(28f, 8f);
                    spawnFlare.setAspect(1f);
                    spawnFlare.setDiscRatio(0.15f);
                    spawnFlare.setCoreColor(new Color(255, 220, 140, 220));
                    spawnFlare.setFringeColor(new Color(200, 150, 50, 180));
                    spawnFlare.setAdditiveBlend();
                    spawnFlare.setGlowPower(2f);
                    spawnFlare.setLocation(loc.x, loc.y);
                    spawnFlare.setGlobalTimer(0.003f, 0.02f, 0.08f);
                    spawnFlare.setLayer(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);
                    CombatRenderingManager.addEntity(BoxEnum.ENTITY_FLARE, spawnFlare);

                    // ---- Micro particle fragments ----
                    byte pc = 25;
                    List<InstanceDataAPI> plist = new ArrayList<>(pc);
                    for (byte j = 0; j < pc; j++) {
                        Instance2Data inst = new Instance2Data();
                        float a = (float) (Math.random() * TrigUtil.PI2_F);
                        float c = (float) Math.cos(a);
                        float s = TrigUtil.sinFormCosRadiansF(c, a);
                        inst.setLocation(loc);
                        float spd = (float) Math.random() * 40f + 15f;
                        inst.setVelocity(c * spd, s * spd);
                        float sc = (float) Math.random() * 0.15f + 0.05f;
                        inst.setScale(sc, sc);
                        inst.setTimer(0f, 0.06f, 0.1f);
                        inst.setHighColor(new Color(220, 180, 80, 180));
                        inst.setEmissiveColor(new Color(180, 130, 40, 130));
                        plist.add(inst);
                    }
                    SpriteEntity spe = new SpriteEntity();
                    spe.setEmissiveSprite(Global.getSettings().getSprite("graphics/fx/particlealpha32sq.png"));
                    spe.getMaterialData().setColor(new Color(0, 0, 0, 0));
                    spe.getMaterialData().setEmissiveColor(new Color(160, 120, 30, 70));
                    spe.getMaterialData().setAlphaToEmissive(0f);
                    spe.setBaseSizePerTiles(3f, 3f);
                    spe.setAdditiveBlend();
                    spe.setLocation(new Vector2f(0f, 0f));
                    spe.setInstanceData(plist, 0f, 0.06f, 0.1f);
                    spe.setInstanceDataRefreshAllFromCurrentIndex();
                    spe.setUseInstanceData2D();
                    spe.mallocInstanceData(pc);
                    spe.submitInstanceData();
                    spe.setRenderingCount(pc);
                    spe.setAlwaysRefreshInstanceData(true);
                    spe.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                    CombatRenderingManager.addEntity(BoxEnum.ENTITY_SPRITE, spe);
                    MissileAPI missile2 = (MissileAPI) engine.spawnProjectile(
                            missile.getSource() != null ? missile.getSource() : null,
                            missile.getWeapon(),
                            "KE_YZ_MEM_EnergyParticle_Rocket",
                            missile.getLocation(),
                            Misc.getAngleInDegrees(missile.getLocation(), enemy.getLocation()),
                            new Vector2f(0f, 0f)
                    );
                    num += 2f;
                }
            }
        }
        if (num >= 6f) {
            // ---- Remove the pre-burst special effect (brown) ----
            FlareEntity removeFlare = new FlareEntity();
            removeFlare.setSmoothDisc();
            removeFlare.setSize(36f, 10f);
            removeFlare.setAspect(1f);
            removeFlare.setDiscRatio(0.1f);
            removeFlare.setCoreColor(new Color(255, 230, 160, 240));
            removeFlare.setFringeColor(new Color(210, 160, 40, 200));
            removeFlare.setAdditiveBlend();
            removeFlare.setGlowPower(2.5f);
            removeFlare.setLocation(loc.x, loc.y);
            removeFlare.setGlobalTimer(0.003f, 0.03f, 0.1f);
            removeFlare.setLayer(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);
            CombatRenderingManager.addEntity(BoxEnum.ENTITY_FLARE, removeFlare);

            byte rpc = 30;
            List<InstanceDataAPI> rlist = new ArrayList<>(rpc);
            for (byte j = 0; j < rpc; j++) {
                Instance2Data inst = new Instance2Data();
                float a = (float) (Math.random() * TrigUtil.PI2_F);
                float c = (float) Math.cos(a);
                float s = TrigUtil.sinFormCosRadiansF(c, a);
                inst.setLocation(loc);
                float spd = (float) Math.random() * 50f + 20f;
                inst.setVelocity(c * spd, s * spd);
                float sc = (float) Math.random() * 0.18f + 0.06f;
                inst.setScale(sc, sc);
                inst.setTimer(0f, 0.08f, 0.12f);
                inst.setHighColor(new Color(232, 171, 22, 200));
                inst.setEmissiveColor(new Color(179, 151, 91, 140));
                rlist.add(inst);
            }
            SpriteEntity rpe = new SpriteEntity();
            rpe.setEmissiveSprite(Global.getSettings().getSprite("graphics/fx/particlealpha32sq.png"));
            rpe.getMaterialData().setColor(new Color(0, 0, 0, 0));
            rpe.getMaterialData().setEmissiveColor(new Color(170, 130, 20, 80));
            rpe.getMaterialData().setAlphaToEmissive(0f);
            rpe.setBaseSizePerTiles(3f, 3f);
            rpe.setAdditiveBlend();
            rpe.setLocation(new Vector2f(0f, 0f));
            rpe.setInstanceData(rlist, 0f, 0.08f, 0.12f);
            rpe.setInstanceDataRefreshAllFromCurrentIndex();
            rpe.setUseInstanceData2D();
            rpe.mallocInstanceData(rpc);
            rpe.submitInstanceData();
            rpe.setRenderingCount(rpc);
            rpe.setAlwaysRefreshInstanceData(true);
            rpe.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
            CombatRenderingManager.addEntity(BoxEnum.ENTITY_SPRITE, rpe);

            engine.removeEntity(missile);
        }
    }
}
