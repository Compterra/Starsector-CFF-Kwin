package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class KE_EntropyHullmod extends BaseHullMod {
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
        String id = ship.getId()+"_entropy";
        MutableShipStatsAPI stats = ship.getMutableStats();
        stats.getFluxDissipation().modifyPercent(id, ship.getFluxLevel()*100f);
        stats.getTimeMult().modifyPercent(id, ship.getFluxLevel()*200f);
        if(stats.getTimeMult().getModifiedValue()>1f){
            ship.addAfterimage(new Color(139, 245, 250, 115),
                    0,
                    0,
                    -ship.getVelocity().x,
                    -ship.getVelocity().y,
                    1.5f,
                    0.05f,
                    0.1f,
                    0.05f,
                    true,
                    false,
                    false);
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
        tooltip.addPara("Current instruments can only sketch the outline of this relic system. The clearest readings come from entropy spikes left in its wake.",
                pad, Valkyrie,
                "");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara(" %sFlux dissipation rises with current flux, up to%s ;\n" +
                        " %sTime flow accelerates with current flux, up to%s ;",
                pad,
                good,
                " #","100%",
                " #","200%");
//        tooltip.addPara(" %sManned fighters take increased damage%s ;\n" +
//                        " %sManned fighters deal reduced damage%s .",
//                pad,
//                bad,
//                " #","25%",
//                " #","30%");
    }
}
