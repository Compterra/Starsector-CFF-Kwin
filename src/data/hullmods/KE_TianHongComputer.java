package data.hullmods;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_TianHongComputer extends BaseHullMod {
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        if(!ship.hasListenerOfClass(KE_TianHong_RangeModifier.class)){
            ship.addListener(new KE_TianHong_RangeModifier(ship));
        }
    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {

    }
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {

    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        String id = ship.getId()+"_KE_TianHongComputer";
        MutableShipStatsAPI stats = ship.getMutableStats();
        if (ship.getOwner() ==1) {
            stats.getFighterRefitTimeMult().modifyPercent(id, -30f);
            stats.getDamageToTargetHullMult().modifyPercent(id, 100f);
            stats.getDamageToTargetShieldsMult().modifyPercent(id, 100f);
            stats.getNonBeamPDWeaponRangeBonus().modifyPercent(id, 75f);
            stats.getSystemUsesBonus().modifyMult(id, 2f);
        }
        if(ship.getOwner() !=1){
            stats.getFighterRefitTimeMult().modifyPercent(id, -30f*0.1f);
            stats.getDamageToTargetHullMult().modifyPercent(id, 100f*0.1f);
            stats.getDamageToTargetShieldsMult().modifyPercent(id, 100f*0.1f);
            stats.getNonBeamPDWeaponRangeBonus().modifyPercent(id, 75f*0.1f);
            stats.getSystemUsesBonus().modifyMult(id, 2f*0.1f);
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
        tooltip.addPara("This cluster computer is a crystallized remnant of Tianhong Group technology, built to unlock the full performance of the Earth Deity-class hull.\n" +
                        "Public records of the Corporate War are scarce; relics like this are sometimes the only honest testimony left.",
                pad, Valkyrie,
                "");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara(" %sNon-beam point defense range increased%s ;\n" +
                        " %sLarge weapon base range increased%s ;\n"+
                        " %sFighter preparation time reduced%s ;\n" +
                        " %sDamage to locked targets increased%s ;\n" +
                        " %sShip system charges increased%s ;",
                pad,
                good,
                " #","50%",
                " #","300",
                " #","30%",
                " #","100%",
                " #","100%");
        tooltip.addPara("When operated outside its intended control architecture, only %s of the listed performance remains.",
                pad,
                bad,
                "10%");
//        tooltip.addPara(" %sManned fighters take increased damage%s ;\n" +
//                        " %sManned fighters deal reduced damage%s .",
//                pad,
//                bad,
//                " #","25%",
//                " #","30%");
    }
    public static class KE_TianHong_RangeModifier implements WeaponBaseRangeModifier {
        private ShipAPI ship;

        public KE_TianHong_RangeModifier(ShipAPI ship){
            this.ship = ship;
        }
        @Override
        public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {return 0;}
        @Override
        public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {return 1f;}

        @Override
        public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
            //float bonus = 0;
            if(weapon.getSpec().getSize() == WeaponAPI.WeaponSize.LARGE){
                return  300f;
            }
            return 0;
        }
    }
}
