package data.hullmods.KE_UE_DelicacyBuff;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class KE_UE_Delicacy_Feast_H extends BaseHullMod {
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        // 1. Maintain combat readiness - reduce the rate of CR decline
        stats.getCRLossPerSecondPercent().modifyMult(id, 0.8f); // 12.5% ​​reduction

        // 2. Basic efficiency - increase the rate of fire
        stats.getBallisticRoFMult().modifyMult(id, 1.20f); // Improved by 15%
        stats.getEnergyRoFMult().modifyMult(id, 1.20f);
        stats.getMissileRoFMult().modifyMult(id, 1.20f);

        // 3. Survivability - Reduce crew losses
        stats.getCrewLossMult().modifyMult(id, 0.65f); // 20% reduction

        // 4. Maintenance efficiency
        stats.getCombatWeaponRepairTimeMult().modifyMult(id, 0.8f); // Maintenance time reduced by 20%
        stats.getCombatEngineRepairTimeMult().modifyMult(id, 0.8f);

        // 5. Defense efficiency
        stats.getShieldDamageTakenMult().modifyMult(id, 0.88f);// Defense efficiency increased by 12.5%
        stats.getHullDamageTakenMult().modifyMult(id, 0.88f);
        stats.getArmorDamageTakenMult().modifyMult(id, 0.88f);

        // 6. Failure prevention - reduce failure rate
        stats.getWeaponMalfunctionChance().modifyMult(id, 0.75f); // 20% reduction
        stats.getEngineMalfunctionChance().modifyMult(id, 0.75f);

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
        stats.getMaxArmorDamageReduction().modifyMult(id, 1.05f);// Maximum armor damage reduction efficiency increased by 5%
        stats.getMinArmorFraction().modifyMult(id, 1.10f);// Minimum armor increased by 10%
        stats.getFluxDissipation().modifyMult(id, 1.05f);// Discharge efficiency increased by 5%
//        //Just for testing
//        stats.getAcceleration().modifyMult(id, 999f);
//        stats.getMaxSpeed().modifyMult(id, 999f);
//        stats.getMaxTurnRate().modifyMult(id, 999f);
//        stats.getTurnAcceleration().modifyMult(id, 999f);
//        stats.getDeceleration().modifyMult(id, 999f);
    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
//        Global.getCombatEngine().maintainStatusForPlayerShip("KE_UE_Delicacy_Feast",
//                "");
//        Global.getCombatEngine().addFloatingText(ship.getLocation(),
//                "KE_UE_Delicacy_Destoryer_H: Test successful",
//                10f,
//                Color.CYAN,
//                ship,
//                5f,
//                10f);
    }
}
