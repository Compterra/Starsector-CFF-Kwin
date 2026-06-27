package data.combat.KE_YZ_EnergyParticle;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MissileAPI;

public interface KE_YZ_EnergyParticleUpgrade {
    void apply(float amount, CombatEntityAPI target, MissileAPI missile, CombatEngineAPI engine);
}
