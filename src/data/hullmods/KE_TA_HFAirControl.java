package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_TA_HFAirControl extends BaseHullMod {
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFighterWingRange().modifyPercent(id, 50f);
        stats.getFighterRefitTimeMult().modifyPercent(id, -50f);
    }

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
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
        tooltip.addPara("A Flicker Aviation Group dispatch suite designed for the Amagi-class carrier.\n" +
                        "It accelerates wing replacement while keeping fighter command links responsive at extended ranges.",
                pad, accent, "");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara(" %sFighter combat range increased%s ;\n" +
                        " %sFighter replacement time reduced%s ;",
                pad,
                good,
                " #", "50%",
                " #", "50%");
    }
}