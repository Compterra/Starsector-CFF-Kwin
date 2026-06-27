package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_EW_PlasmaGenerator extends BaseHullMod {
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        stats.getFluxDissipation().modifyPercent(id, 15f);
        stats.getFluxCapacity().modifyPercent(id, 15f);
        stats.getEnergyWeaponFluxCostMod().modifyPercent(id, -10f);
    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
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
        Color FabriqueOrbitale = new Color(25, 73, 151, 255);
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        /*Level 4: Coarse food (139, 115, 85),
        Regular meals (107, 142, 35),
        Fine food (210, 105, 30),
        Hua Yan (184, 134, 11)*/
        tooltip.addPara("Monolith Ordnance Group developed this hull kit for Energy Body Focus conversions.\n" +
                        "It combines residual %s technology with plasma-generator principles, improving flux routing across the ship.\n" +
                        "Recovered components suggest that high-energy piezoelectric crystals are central to the system.",
                pad, FabriqueOrbitale,
                "Orbital Factory Manufacturing Company");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara(" %sShip flux grid optimization%s ;\n"+
                        " %sEnergy weapon firing cost reduced%s ;",
                pad,
                good,
                " #","15%",
                " #","10%");
    }
}
