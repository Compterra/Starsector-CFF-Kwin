package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_UE_LuxuryMod extends BaseHullMod {
    private static final float SUPPLY_UPKEEP_MULT = 0.85f;
    private static final float CR_RECOVERY_BONUS = 10f;
    private static final float REPAIR_RATE_BONUS = 10f;
    private static final float CREW_LOSS_MULT = 0.90f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getSuppliesPerMonth().modifyMult(id, SUPPLY_UPKEEP_MULT);
        stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, CR_RECOVERY_BONUS);
        stats.getRepairRatePercentPerDay().modifyPercent(id, REPAIR_RATE_BONUS);
        stats.getCrewLossMult().modifyMult(id, CREW_LOSS_MULT);
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return ship == null ? null : null;
    }

    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return false;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;
        Color h = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color accent = new Color(208, 139, 244, 255);

        tooltip.addPara("A Zenith Era hospitality package that turns spare shipboard volume into lounges, officer services, cleaner berthing, and maintenance support for this ship's own crew.",
                pad, accent, "Zenith Era");
        tooltip.addPara("The suite does not scale with the wider fleet. Additional luxury ships only provide their own onboard services.",
                pad);

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("%s monthly supply upkeep for this ship.", pad, good, "-15%");
        tooltip.addPara("%s CR recovery rate for this ship.", pad, h, "+10%");
        tooltip.addPara("%s repair rate for this ship.", pad, h, "+10%");
        tooltip.addPara("%s crew losses suffered by this ship.", pad, good, "-10%");
    }
}