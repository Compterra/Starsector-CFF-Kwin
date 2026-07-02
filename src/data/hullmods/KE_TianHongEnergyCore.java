package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_TianHongEnergyCore extends BaseHullMod {
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {

    }
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
//        ship.setOwner(1);
        String id = ship.getId()+"_KE_TianHongEnergyCore";
        MutableShipStatsAPI stats = ship.getMutableStats();
        float value = 50f;
        if (ship.getOwner() ==1) {
            stats.getMaxTurnRate().modifyPercent(id, value);
            stats.getAcceleration().modifyPercent(id, value);
            stats.getDeceleration().modifyPercent(id, value);
            stats.getTurnAcceleration().modifyPercent(id, value);
            stats.getMaxSpeed().modifyPercent(id, value);
            stats.getFluxDissipation().modifyPercent(id, 300f);
            stats.getFluxCapacity().modifyPercent(id, 150f);
            stats.getEmpDamageTakenMult().modifyPercent(id, -85f);
        }
        if (ship.getOwner() !=1) {
            stats.getMaxTurnRate().modifyPercent(id, value*0.1f);
            stats.getAcceleration().modifyPercent(id, value*0.1f);
            stats.getDeceleration().modifyPercent(id, value*0.1f);
            stats.getTurnAcceleration().modifyPercent(id, value*0.1f);
            stats.getMaxSpeed().modifyPercent(id, value*0.1f);
            stats.getFluxDissipation().modifyPercent(id, 300f*0.1f);
            stats.getFluxCapacity().modifyPercent(id, 150f*0.1f);
            stats.getEmpDamageTakenMult().modifyPercent(id, -85f*0.1f);
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
        Color Valkyrie = new Color(195, 36, 15, 255);
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        /*Level 4: Coarse food (139, 115, 85),
        Regular meals (107, 142, 35),
        Fine food (210, 105, 30),
        Hua Yan (184, 134, 11)*/
        tooltip.addPara("This Tianhong legacy energy furnace provides the foundation for Earth Deity-class operations.\n" +
                        "It is the corporation's final fury, still burning long after Tianhong itself vanished.",
                pad, Valkyrie,
                "");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("Ship maneuverability improved by %s.", pad, good, "50%");
        tooltip.addPara("Flux capacity increased by %s.", 3f, good, "150%");
        tooltip.addPara("Flux dissipation increased by %s.", 3f, good, "300%");
        tooltip.addPara("EMP damage taken reduced by %s.", 3f, good, "85%");
        tooltip.addPara("When operated outside its intended control architecture, only %s of the listed performance remains.",
                pad,
                bad,
                "10%");
    }
}
