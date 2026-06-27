package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_TA_AAirControl extends BaseHullMod {
    private static final float DEPLOYMENT_MULT = 4f / 3f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        applyExtraDeployment(ship);
    }

    private void applyExtraDeployment(ShipAPI ship) {
        String id = ship.getId() + "_KE_TA_AAirControl";
        if (ship.getOriginalOwner() == -1) {
            return;
        }

        boolean allDeployed = true;
        boolean ranOnce = false;
        for (FighterLaunchBayAPI bay : ship.getLaunchBaysCopy()) {
            if (bay.getWing() == null) {
                continue;
            }

            ranOnce = true;
            FighterWingSpecAPI wingSpec = bay.getWing().getSpec();
            int deployed = bay.getWing().getWingMembers().size();
            int maxTotal = (int) (wingSpec.getNumFighters() * DEPLOYMENT_MULT);
            int actualAdd = maxTotal - deployed;

            if (actualAdd > 0) {
                bay.setExtraDeployments(actualAdd);
                bay.setExtraDeploymentLimit(maxTotal);
                bay.setExtraDuration(9999999);
                allDeployed = false;
            } else {
                bay.setExtraDeployments(0);
                bay.setExtraDeploymentLimit(0);
                bay.setFastReplacements(0);
            }

            if (ship.getMutableStats().getFighterRefitTimeMult().getPercentStatMod(id) == null && actualAdd != 0) {
                bay.setFastReplacements(actualAdd);
            }
        }

        if (ship.getMutableStats().getFighterRefitTimeMult().getPercentStatMod(id) == null && allDeployed && ranOnce) {
            ship.getMutableStats().getFighterRefitTimeMult().modifyPercent(id, 1);
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
        tooltip.addPara("A Flicker Aviation Group command suite designed for the Borealis-class carrier.\n" +
                        "It expands simultaneous wing deployment through reinforced deck control and secondary launch coordination.",
                pad, accent, "");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara(" %sAdditional fighter craft deployed%s ;",
                pad,
                good,
                " #", "33%");
    }
}