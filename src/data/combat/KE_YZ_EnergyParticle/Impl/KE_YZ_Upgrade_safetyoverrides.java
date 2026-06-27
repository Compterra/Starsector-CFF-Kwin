package data.combat.KE_YZ_EnergyParticle.Impl;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleUpgrade;

public class KE_YZ_Upgrade_safetyoverrides implements KE_YZ_EnergyParticleUpgrade {
    private boolean applied = false;
    private final String id = "KE_YZ_Upgrade_safetyoverrides";
    @Override
    public void apply(float amount, CombatEntityAPI target, MissileAPI missile, CombatEngineAPI engine) {
//        if (!applied) {
//            missile.getVelocity().scale(1.5f);
//            applied = true;
//        }
        DamageAPI damage = missile.getDamage();
        damage.getModifier().modifyPercent(id, 25f);
    }
}
