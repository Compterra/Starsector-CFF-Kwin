package data.hullmods.KE_UE_DelicacyBuff;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class KE_UE_Delicacy_StandardMeal_H extends BaseHullMod {
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        // 1. Maintain combat readiness - reduce the rate of CR decline
        stats.getCRLossPerSecondPercent().modifyMult(id, 0.925f); // reduced by 7.5%

        // 2. Failure prevention - reduce failure rate
        stats.getWeaponMalfunctionChance().modifyMult(id, 0.85f); // 15% reduction
        stats.getEngineMalfunctionChance().modifyMult(id, 0.85f);

        // 3. Basic efficiency - increase the rate of fire
        stats.getBallisticRoFMult().modifyMult(id, 1.1f); // Improved by 8%
        stats.getEnergyRoFMult().modifyMult(id, 1.1f);
        stats.getMissileRoFMult().modifyMult(id, 1.1f);

        // 4. Survivability - Reduce crew losses
        stats.getCrewLossMult().modifyMult(id, 0.9f); // 10% reduction

        // 5. Defense efficiency
        stats.getShieldDamageTakenMult().modifyMult(id, 0.95f);// Defense efficiency increased by 5%
        stats.getHullDamageTakenMult().modifyMult(id, 0.95f);
        stats.getArmorDamageTakenMult().modifyMult(id, 0.95f);

        // 5. Maintenance efficiency
        stats.getCombatWeaponRepairTimeMult().modifyMult(id, 0.88f); // Maintenance efficiency increased by 12.5%
        stats.getCombatEngineRepairTimeMult().modifyMult(id, 0.88f);

        // 6. Slightly improve sensor performance (crew’s concentration)
        stats.getSensorStrength().modifyMult(id, 1.05f); // Increase by 5%
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
