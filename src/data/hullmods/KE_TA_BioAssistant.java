package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_TA_BioAssistant extends BaseHullMod {
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        stats.getTurnAcceleration().modifyMult(id, 1.25f);
        stats.getMaxSpeed().modifyMult(id, 1.25f);
        stats.getAcceleration().modifyMult(id, 1.25f);
        stats.getDeceleration().modifyMult(id, 1.25f);
        stats.getMaxTurnRate().modifyMult(id, 1.25f);
    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        String id = "KE_TA_BioAssistant";
        MutableShipStatsAPI stats = ship.getMutableStats();
        if(ship.getSystem().getEffectLevel()>=1){
            stats.getTimeMult().modifyPercent(id, 1.1f);
        }
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
        tooltip.addPara("An %s coordination system shared across compatible fighter wings.\n" +
                        "It improves baseline mobility, then unlocks additional performance while the tactical system is active.\n" +
                        "The Corporate War ended long ago, but some designs still refuse to stay buried.",
                pad, Exodyne,
                "Exodyne Biotechnology");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("Fighter mobility improved by %s.", pad, good, "25%");
        tooltip.addPara("While the tactical system is active, fighter performance improves by another %s.", 3f, good, "10%");
    }
}
