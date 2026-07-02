package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_UE_Delicacy_FineMeal extends BaseHullMod {
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
        Color f = new Color(210, 105, 30, 255);
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        /*Level 4: Coarse food (139, 115, 85),
        Regular meals (107, 142, 35),
        Fine food (210, 105, 30),
        Hua Yan (184, 134, 11)*/
        tooltip.addPara("Delicacy-class ships maintain fleetwide catering programs, but the quality of support depends on the best kitchens currently in service.\n" +
                        "This ship currently provides %s.",
                pad, f,
                "fine cuisine");
        tooltip.addPara("If a higher-tier Delicacy vessel is present, this ship adopts that standard for fleet provisioning.",
                pad, h,
                "");
        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("A stabilized crew diet improves endurance, morale, and the practical rhythm of shipboard work.", pad);
        tooltip.addPara("Combat readiness degradation reduced by %s.", 3f, good, "12.5%");
        tooltip.addPara("Weapon rate of fire increased by %s.", 3f, good, "15%");
        tooltip.addPara("Crew survival rate increased by %s.", 3f, good, "20%");
        tooltip.addPara("Maintenance efficiency improved by %s.", 3f, good, "17.5%");
        tooltip.addPara("Defensive efficiency improved by %s.", 3f, good, "7.5%");
        tooltip.addPara("Malfunction rate reduced by %s.", 3f, good, "20%");
        tooltip.addSectionHeading("Additional Benefits",Misc.getTextColor(),f, Alignment.MID, pad);
        tooltip.addPara("Higher catering tiers inherit lower-tier benefits and add their own refinements.", pad);
        tooltip.addPara("Mobility increased by %s.", 3f, good, "10%");
        tooltip.addPara("Damage increased by %s.", 3f, good, "5%");
    }
}
