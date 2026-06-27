package data.combat.KE_YZ_EnergyParticle.Impl;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleUpgrade;
import org.lazywizard.lazylib.combat.AIUtils;

import java.awt.*;

public class KE_YZ_Upgrade_ecm implements KE_YZ_EnergyParticleUpgrade {
    private final IntervalUtil UpgradeClock = new IntervalUtil(0.25f, 0.25f);
    private boolean isEnabled = false;
    @Override
    public void apply(float amount, CombatEntityAPI target, MissileAPI missile, CombatEngineAPI engine) {
        UpgradeClock.advance(amount);
        MissileAPI enemyMissile = AIUtils.getNearestEnemyMissile(missile);
        if (enemyMissile != null) {
            if (Misc.getDistance(missile.getLocation(), enemyMissile.getLocation()) <= 200f) {
                for(int i = 0; i < 1; i++) {
                    EmpArcEntityAPI empArc = engine.spawnEmpArc(
                            missile.getSource(),
                            missile.getLocation(),
                            enemyMissile,
                            enemyMissile,
                            DamageType.ENERGY,
                            5f,
                            0f,
                            100000f,
                            null,
                            0.5f,
                            new Color(34, 188, 244, 50),
                            new Color(255, 255, 255, 68)
                    );
//                    empArc.setWarping(0.5f);
                    empArc.setUpdateFromOffsetEveryFrame(true);
                    empArc.setSingleFlickerMode(true);
                }
            }
        }
        if(UpgradeClock.intervalElapsed()) {
//            engine.addFloatingText(
//                    Global.getCombatEngine().getPlayerShip().getLocation(),
//                    "not null",
//                    10f,
//                    Color.WHITE,
//                    Global.getCombatEngine().getPlayerShip(),
//                    1f,
//                    1f
//            );
        }
//        for(int i = 0; i < 3; i++) {
//            engine.spawnEmpArc(
//                    missile.getSource(),
//                    missile.getLocation(),
//                    target,
//                    target,
//                    DamageType.ENERGY,
//                    1f,
//                    0f,
//                    100000f,
//                    null,
//                    1.5f,
//                    new Color(34, 188, 244, 255),
//                    Color.white
//            );
//        }
    }
}
