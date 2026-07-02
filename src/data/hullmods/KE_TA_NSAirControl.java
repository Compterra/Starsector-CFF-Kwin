package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_TA_NSAirControl extends BaseHullMod {
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFighterWingRange().modifyPercent(id, 30f);
    }

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        MutableShipStatsAPI fighterStats = fighter.getMutableStats();
        fighterStats.getMaxSpeed().modifyPercent(id, 30f);
        fighterStats.getAcceleration().modifyPercent(id, 30f);
        fighterStats.getDeceleration().modifyPercent(id, 30f);
        fighterStats.getTurnAcceleration().modifyPercent(id, 30f);
        fighterStats.getMaxTurnRate().modifyPercent(id, 30f);
        if (!fighter.getWing().getSpec().isBomber()) {
            float damageBonus = 15f;
            fighterStats.getDamageToTargetHullMult().modifyPercent(id, damageBonus);
            fighterStats.getDamageToTargetShieldsMult().modifyPercent(id, damageBonus);
            fighterStats.getDamageToTargetEnginesMult().modifyPercent(id, damageBonus);
            fighterStats.getDamageToTargetWeaponsMult().modifyPercent(id, damageBonus);
        }
        if (fighter.getWing().getSpec().isBomber()) {
            float damageReduce = -15f;
            fighterStats.getShieldDamageTakenMult().modifyPercent(id, damageReduce);
            fighterStats.getArmorDamageTakenMult().modifyPercent(id, damageReduce);
            fighterStats.getEmpDamageTakenMult().modifyPercent(id, damageReduce);
            fighterStats.getHullDamageTakenMult().modifyPercent(id, damageReduce);
            fighterStats.getEngineDamageTakenMult().modifyPercent(id, damageReduce);
            fighterStats.getWeaponDamageTakenMult().modifyPercent(id, damageReduce);
        }
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return null;
    }

    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return false;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;
        Color accent = new Color(195, 36, 15, 255);
        Color good = Misc.getPositiveHighlightColor();
        tooltip.addPara("The Night Star's core aviation command suite links layered fighter communications with dedicated control spaces.\n" +
                        "It turns superior coordination into range, maneuverability, and cleaner strike timing.",
                pad, accent, "");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("Fighter combat range and maneuverability improved by %s.", pad, good, "30%");
        tooltip.addPara("Non-bomber fighter damage to targets increased by %s.", 3f, good, "15%");
        tooltip.addPara("Bomber damage taken reduced by %s.", 3f, good, "15%");
    }
}