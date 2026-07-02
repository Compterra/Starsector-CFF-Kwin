package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_EW_LuddicAirControl extends BaseHullMod {
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize,
                                               MutableShipStatsAPI stats, String id) {
        stats.getFighterRefitTimeMult().modifyPercent(id, -30f);
    }
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        if(!fighter.hasListenerOfClass(KE_EW_LuddicAirControlListener.class)&&fighter.getHullSpec().getMinCrew()>0){
            fighter.addListener(new KE_EW_LuddicAirControlListener(fighter, id));
        }
    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        ExtraDeploy(ship);
    }
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
    }

    public void ExtraDeploy(ShipAPI ship) {
        String ID = ship.getId() + "_KE_EW_LuddicAirControl";
        if(ship.getOriginalOwner()==-1){
            return; //supress in refit
        }

        boolean allDeployed=true, ranOnce=false;

        for(FighterLaunchBayAPI bay : ship.getLaunchBaysCopy()) {
            if (bay.getWing() != null) {
                ranOnce = true;
                if (bay.getWing().getSpec().getNumFighters() >=3&&bay.getWing().getLeader()!=null&&bay.getWing().getLeader().getHullSpec().getMinCrew()>0) {

                    FighterWingSpecAPI wingSpec = bay.getWing().getSpec();
                    int deployed = bay.getWing().getWingMembers().size();
                    int maxTotal = (int) (wingSpec.getNumFighters()*1.5f);
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

                    if (ship.getMutableStats().getFighterRefitTimeMult().getPercentStatMod(ID) == null && actualAdd != 0) {
                        //instantly add all the required fighters upon deployment
                        bay.setFastReplacements(actualAdd);
                    }

                    //debug
//                    Global.getCombatEngine().addFloatingText(
//                            bay.getWeaponSlot().computePosition(ship),
//                            "add= "+bay.getExtraDeployments()+" max= "+bay.getExtraDeploymentLimit()+" fast= "+bay.getFastReplacements(),
//                            10, Color.ORANGE, ship, 1, 1);
                }
                if (ship.getMutableStats().getFighterRefitTimeMult().getPercentStatMod(ID)==null && allDeployed && ranOnce){
                    //used as a check to add all the extra fighters upon deployment
                    ship.getMutableStats().getFighterRefitTimeMult().modifyPercent(ID, 1);
                }
            }
        }
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
        tooltip.addPara("This forge can assemble replacement strike craft faster than standard fabrication systems.\n" +
                        "Pushing deployments beyond normal command limits causes predictable problems, but Church crews accept the risk.",
                pad, Valkyrie,
                "");

        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("Manned wings with three or more craft deploy %s additional fighters.", pad, good, "50%");
        tooltip.addPara("Fighter replacement time reduced by %s.", 3f, good, "30%");
        tooltip.addPara("Manned fighters take %s increased damage.", pad, bad, "25%");
        tooltip.addPara("Manned fighters deal %s reduced damage.", 3f, bad, "30%");
    }
    public static class KE_EW_LuddicAirControlListener implements AdvanceableListener{

        ShipAPI ship;
        String id;
        public KE_EW_LuddicAirControlListener(ShipAPI fighter, String id) {
            this.ship = fighter;
            this.id = id;
        }
        @Override
        public void advance(float amount) {
            if(!ship.isAlive()||ship.isHulk()||ship.isExpired()){
                cleanup();
                return;
            }
            MutableShipStatsAPI stats = ship.getMutableStats();
//            Global.getCombatEngine().addFloatingText(ship.getLocation(),
//                    "listener success",
//                    100f,
//                    Color.WHITE,
//                    ship,
//                    1f,
//                    5f);
            stats.getShieldDamageTakenMult().modifyPercent(id, 25f);
            stats.getArmorDamageTakenMult().modifyPercent(id, 25f);
            stats.getHullDamageTakenMult().modifyPercent(id, 25f);

            stats.getDamageToCapital().modifyPercent(id, -30f);
            stats.getDamageToCruisers().modifyPercent(id, -30f);
            stats.getDamageToDestroyers().modifyPercent(id, -30f);
            stats.getDamageToFrigates().modifyPercent(id, -30f);
            stats.getDamageToFighters().modifyPercent(id, -30f);
        }

        private void cleanup() {
            MutableShipStatsAPI stats = ship.getMutableStats();
            stats.getShieldDamageTakenMult().unmodify(id);
            stats.getArmorDamageTakenMult().unmodify(id);
            stats.getHullDamageTakenMult().unmodify(id);
            stats.getDamageToCapital().unmodify(id);
            stats.getDamageToCruisers().unmodify(id);
            stats.getDamageToDestroyers().unmodify(id);
            stats.getDamageToFrigates().unmodify(id);
            stats.getDamageToFighters().unmodify(id);
            ship.removeListener(this);
        }
    }
}
