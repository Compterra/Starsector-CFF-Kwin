package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_UE_Delicacy_BasicMeal extends BaseHullMod {
    private boolean HaveHullmod = false;
    private Integer num =0;
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        /*It has no effect and is mainly used to detect whether the plug-in is included.*/
    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        /*It has no effect and is mainly used to detect whether the plug-in is included.*/
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
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        /*Level 4: Coarse food (139, 115, 85),
        Regular meals (107, 142, 35),
        Fine food (210, 105, 30),
        Hua Yan (184, 134, 11)*/
        tooltip.addPara("Delicacy-class ships maintain fleetwide catering programs, but the quality of support depends on the best kitchens currently in service.\n" +
                        "This ship currently provides %s.",
                pad, f,
                "basic rations");
        tooltip.addPara("If a higher-tier Delicacy vessel is present, this ship adopts that standard for fleet provisioning.",
                pad, h,
                "");
        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("A stabilized crew diet improves endurance, morale, and the practical rhythm of shipboard work.\n"+
                        " %sCombat readiness degradation reduced%s ;\n"+
                        " %sWeapon rate of fire increased%s ;\n"+
//                        " %sCrew survival rate increased%s ;\n"+
                        " %sMaintenance efficiency improved%s ;\n"+
//                        " %sImproved ship defense efficiency%s ;\n"+
                        " %sMalfunction rate reduced%s ;",
                pad,
                good,
                " #","5%",
                " #","10%",
//                " #","35%",
                " #","10%",
//                " #","12.5%",
                " #","10%");
        tooltip.addSectionHeading("Additional Benefits",Misc.getTextColor(),f, Alignment.MID, pad);
        tooltip.addPara("Higher catering tiers inherit lower-tier benefits and add their own refinements.\n"+
                        " %sThis tier has no additional benefit.",
                pad,
                bad,
                " #");
    }
}
