package data.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.FactionDoctrineAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.submarkets.BaseSubmarketPlugin;

import java.util.List;

public class KE_UEMarket extends BaseSubmarketPlugin {

    @Override
    public void init(SubmarketAPI submarket) {
        this.submarket = submarket;
        this.market = submarket.getMarket();
    }
    public static String txt(String id) {
        return Global.getSettings().getString("campaign", id);
    }

    @Override
    public float getTariff() {
        return 0.05f;
    }

    @Override
    public void updateCargoPrePlayerInteraction() {
        sinceLastCargoUpdate = 0f;
        //all
        if (okToUpdateShipsAndWeapons()) {
            sinceSWUpdate = 0f;
            pruneWeapons(0f);

            int size = Math.max(3, market.getSize());
            int weapons = 5 + size * 4;
            int fighterNum = 2 + size;
            int hullmods = 2 + size;

//            FactionAPI TDB_Market = null;
//            List<FactionAPI> Factions = Global.getSector().getAllFactions();
//            for (FactionAPI F : Factions) {
//                if (F.getId().equals("PastItem")) {
//                    TDB_Market = F;
//                }
//            }


            addFighters(fighterNum, fighterNum + 2, 3, "KE_UECorporation"); //min number, max number, max tier, faction id
            addWeapons(weapons, weapons + 2, 3, Factions.INDEPENDENT);
            addHullMods(hullmods, hullmods);

            getCargo().getMothballedShips().clear();
            FactionDoctrineAPI doctrineOverrided = submarket.getFaction().getDoctrine().clone();
            doctrineOverrided.setCombatFreighterProbability(0.25f);
            doctrineOverrided.setShipSize(3);

            // Product ship generation
            addShips("KE_UECorporation",
                    70f + size * 15f, // combat
                    60f + size * 20f, // freighter
                    30f + size * 10f, // tanker
                    45f + size * 15f, // transport
                    45f + size * 15f, // liner
                    45f + size * 15f, // utilityPts
                    null, // qualityOverride
                    30f, // qualityMod
                    FactionAPI.ShipPickMode.PRIORITY_THEN_ALL,//FactionAPI.ShipPickMode modeOverride, at what priority to pick ship in all availables
                    doctrineOverrided);// FactionDoctrineAPI doctrineOverride, at what fraction to pick ship among all availables


        }

        getCargo().sort();
    }

    // Judgment relationship and corresponding display
    @Override
    public boolean isIllegalOnSubmarket(CargoStackAPI stack, TransferAction action) {

        return false;
//        FactionAPI player = Global.getSector().getPlayerFaction();
//        RepLevel FSD_Level = Global.getSector().getFaction("Farsight_Drive").getRelationshipLevel(player);
//
//        if (action == TransferAction.PLAYER_SELL) return true;
//        if (action == TransferAction.PLAYER_BUY && !FSD_Level.isAtWorst(RepLevel.WELCOMING)) return true;
//        //if(action == TransferAction.PLAYER_BUY && !hegeLevel.isAtWorst(RepLevel.SUSPICIOUS)) return true;
//        return action == TransferAction.PLAYER_BUY && !FSD_Level.isAtWorst(RepLevel.WELCOMING);
    }

    @Override
    public boolean isIllegalOnSubmarket(FleetMemberAPI member, TransferAction action) {
        return false;
//        FactionAPI player = Global.getSector().getPlayerFaction();
//        RepLevel FSD_Level = Global.getSector().getFaction("Farsight_Drive").getRelationshipLevel(player);
//
//        if (action == TransferAction.PLAYER_SELL) return true;
//        if (action == TransferAction.PLAYER_BUY && !FSD_Level.isAtWorst(RepLevel.WELCOMING)) return true;
//        return action == TransferAction.PLAYER_BUY && !FSD_Level.isAtWorst(RepLevel.WELCOMING);
    }


    @Override
    public String getIllegalTransferText(CargoStackAPI stack, TransferAction action) {

        return null;
//        FactionAPI player = Global.getSector().getPlayerFaction();
//        RepLevel FSD_Level = Global.getSector().getFaction("Farsight_Drive").getRelationshipLevel(player);
//
////        if (action == TransferAction.PLAYER_SELL) return txt("MARKET_1");
////        if (!FSD_Level.isAtWorst(RepLevel.WELCOMING)) return txt("MARKET_2");
////        return txt("MARKET_3");
//        if (action == TransferAction.PLAYER_SELL) return "Not for sale";
//        if (!FSD_Level.isAtWorst(RepLevel.WELCOMING)) return "Relationships need to be met and welcomed!";
//        return "If the conditions are not met, you will not have the right to enter the market.";

    }

    @Override
    public String getIllegalTransferText(FleetMemberAPI member, TransferAction action) {
//        FactionAPI player = Global.getSector().getPlayerFaction();
//        RepLevel FSD_Level = Global.getSector().getFaction("Farsight_Drive").getRelationshipLevel(player);
//
//        if (action == TransferAction.PLAYER_SELL) return "Not for sale";
//        if (!FSD_Level.isAtWorst(RepLevel.WELCOMING)) return "Relationships need to be met and welcomed!";
//        return "If the conditions are not met, you will not have the right to enter the market.";

        return null;
    }

    // Hide her when not under the control of the force
    @Override
    public boolean isHidden() {
        return false;
    }

}
