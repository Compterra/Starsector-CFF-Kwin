package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_EW_BionicAutomation extends BaseHullMod {
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        stats.getBallisticRoFMult().modifyPercent(id, 15f);
        stats.getTurnAcceleration().modifyPercent(id, 10f);
        stats.getMaxTurnRate().modifyPercent(id, 10f);
    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        /*It has no effect and is mainly used to detect whether the plug-in is included.*/
    }
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
    }
    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {return false;}
    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;
        Color h = Misc.getHighlightColor();
        Color f = new Color(139, 115, 85, 255);
        Color Exodyne = new Color(201,219,4, 255);
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        /*Level 4: Coarse food (139, 115, 85),
        Regular meals (107, 142, 35),
        Fine food (210, 105, 30),
        Hua Yan (184, 134, 11)*/
        tooltip.addPara("Monolith Ordnance Group designed this automation suite around Luddic Church doctrine and battlefield needs.\n" +
                        "It integrates legacy systems from %s into a new control architecture.\n" +
                        "Judging by its deployment rate, Church authorities do not consider it blasphemous technology.",
                pad, Exodyne,
                "Exodyne");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("Ballistic weapon rate of fire increased by %s.", pad, good, "15%");
        tooltip.addPara("Ship turning maneuverability improved by %s.", 3f, good, "10%");
    }
}
