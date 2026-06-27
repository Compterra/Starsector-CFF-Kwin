package data.hullmods.KE_UE_DelicacyBuff;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class KE_UE_Delicacy_FineMeal_H extends BaseHullMod {
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        // 1. Maintain combat readiness - reduce the rate of CR decline
        stats.getCRLossPerSecondPercent().modifyMult(id, 0.875f); // 12.5% ​​reduction

        // 2. Failure prevention - reduce failure rate
        stats.getWeaponMalfunctionChance().modifyMult(id, 0.8f); // 20% reduction
        stats.getEngineMalfunctionChance().modifyMult(id, 0.8f);

        // 3. Basic efficiency - increase the rate of fire
        stats.getBallisticRoFMult().modifyMult(id, 1.15f); // Improved by 15%
        stats.getEnergyRoFMult().modifyMult(id, 1.15f);
        stats.getMissileRoFMult().modifyMult(id, 1.15f);

        // 4. Survivability - Reduce crew losses
        stats.getCrewLossMult().modifyMult(id, 0.8f); // 20% reduction

        // 5. Defense efficiency
        stats.getShieldDamageTakenMult().modifyMult(id, 0.93f);// Defense efficiency increased by 7.5%
        stats.getHullDamageTakenMult().modifyMult(id, 0.93f);
        stats.getArmorDamageTakenMult().modifyMult(id, 0.93f);

        // 6. Maintenance efficiency
        stats.getCombatWeaponRepairTimeMult().modifyMult(id, 0.85f); // Maintenance time reduced by 15%
        stats.getCombatEngineRepairTimeMult().modifyMult(id, 0.85f);

        // 7. Special gains
        stats.getSensorStrength().modifyMult(id, 1.05f); // Sensor range increased by 5%
        stats.getAcceleration().modifyMult(id, 1.1f); // Mobility increased by 10%
        stats.getDeceleration().modifyMult(id, 1.1f);
        stats.getMaxTurnRate().modifyMult(id, 1.1f);
        stats.getMaxSpeed().modifyMult(id, 1.1f);
        stats.getTurnAcceleration().modifyMult(id, 1.1f);
        stats.getDamageToMissiles().modifyMult(id, 1.05f);// Damage increased by 5%
        stats.getDamageToFighters().modifyMult(id, 1.05f);
        stats.getDamageToFrigates().modifyMult(id, 1.05f);
        stats.getDamageToDestroyers().modifyMult(id, 1.05f);
        stats.getDamageToCruisers().modifyMult(id, 1.05f);
        stats.getDamageToCapital().modifyMult(id, 1.05f);
//        //Just for testing
//        stats.getAcceleration().modifyMult(id, 999f);
//        stats.getMaxSpeed().modifyMult(id, 999f);
//        stats.getMaxTurnRate().modifyMult(id, 999f);
//        stats.getTurnAcceleration().modifyMult(id, 999f);
//        stats.getDeceleration().modifyMult(id, 999f);
    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
//        Global.getCombatEngine().addFloatingText(ship.getLocation(),
//                "KE_UE_Delicacy_Destoryer_H: Test successful",
//                10f,
//                Color.CYAN,
//                ship,
//                5f,
//                10f);
    }
}
