package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_EW_Valkyrie extends BaseHullMod {
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        stats.getDamageToCapital().modifyPercent(id, 20f);
        stats.getDamageToCruisers().modifyPercent(id, 20f);

        stats.getFluxDissipation().modifyPercent(id, 30f);
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
        Color Valkyrie = new Color(195, 36, 15, 255);
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        /*Level 4: Coarse food (139, 115, 85),
        Regular meals (107, 142, 35),
        Fine food (210, 105, 30),
        Hua Yan (184, 134, 11)*/
        tooltip.addPara(" %sThe most outstanding hunting kit.\n"+
                        "After a new round of improvements, it will become more threatening to large ships.\n" +
                        "They never forgot that humiliating battle.",
                pad, Valkyrie,
                "Valkyrie Mercenary Group");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara(" %sImproved flux dissipation efficiency%s ;\n"+
                        " %sright%sCauses increased damage%s ;",
                pad,
                good,
                " #","30%",
                " #","Cruiser/Battleship","20%");
    }
}
