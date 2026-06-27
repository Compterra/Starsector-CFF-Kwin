package data.combat.KE_YZ_EnergyParticle.Impl;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleUpgrade;
import org.lazywizard.lazylib.combat.AIUtils;

import java.awt.*;

public class KE_YZ_Upgrade_MS implements KE_YZ_EnergyParticleUpgrade {
    private float time = 0f;
    @Override
    public void apply(float amount, CombatEntityAPI target, MissileAPI missile, CombatEngineAPI engine) {
        CombatEntityAPI enemy = AIUtils.getNearestEnemy(missile);
        time += amount;
        if(enemy!= null&&enemy instanceof ShipAPI){
            if (Misc.getDistance(missile.getLocation(), enemy.getLocation()) <= 450f&&time >=0.2f) {
                for(int i = 0; i < 3; i++) {
                    EmpArcEntityAPI empArc = engine.spawnEmpArc(
                            missile.getSource(),
                            missile.getLocation(),
                            enemy,
                            enemy,
                            DamageType.ENERGY,
                            10f,
                            5f,
                            100000f,
                            null,
                            0.25f,
                            new Color(244, 34, 128, 50),
                            new Color(220, 178, 244, 68)
                    );
                    empArc.setUpdateFromOffsetEveryFrame(true);
                    empArc.setSingleFlickerMode(true);
                }
            }
        }
    }
}
