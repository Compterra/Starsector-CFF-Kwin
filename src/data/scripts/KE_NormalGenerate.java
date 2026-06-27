package data.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import data.scripts.starsystems.KE_EW_Starsystem;
import data.scripts.starsystems.KE_TA_Starsystem;
import data.scripts.starsystems.KE_UE_Starsystem;
import data.scripts.starsystems.KE_YZ_Starsystem;
import org.magiclib.util.MagicCampaign;

import java.util.ArrayList;
import java.util.List;

// Modify the file name and the prefix below it to_NormalGenerate
// GearIn_NormalGenerate in the tutorial example
public class KE_NormalGenerate implements SectorGeneratorPlugin {

    @Override
    public void generate(SectorAPI sector) {
        // Prepare to generate a galaxy here
        new KE_EW_Starsystem().generate(sector);
        new KE_TA_Starsystem().generate(sector);
        new KE_UE_Starsystem().generate(sector);
        new KE_YZ_Starsystem().generate(sector);

        relationAdj(sector);
    }

    protected void relationAdj(SectorAPI sector) {
        //FactionAPI faction = sector.getFaction("AutoBorderBattlegroup");

        // Find the faction id in the original data.world.factions
        // RepLevel is the relationship level, see the original data.scripts.world.SectorGen for details

        //faction.setRelationship("player", 0f);

    }
    public static MarketAPI addMarketplace (String factionID, SectorEntityToken primaryEntity, ArrayList<SectorEntityToken> connectedEntities, String name,
                                            int size, ArrayList<String> marketConditions, ArrayList<String> submarkets, ArrayList<String> industries, float tarrif,
                                            boolean freePort, boolean withJunkAndChatter) {
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        String planetID = primaryEntity.getId();
        String marketID = planetID + "_market";

        MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, size);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        newMarket.getTariff().modifyFlat("generator", tarrif);

        if (null != submarkets) {
            for (String market : submarkets) {
                newMarket.addSubmarket(market);
            }
        }

        for (String condition : marketConditions) {
            newMarket.addCondition(condition);
        }

        for (String industry : industries) {
            newMarket.addIndustry(industry);
        }

        newMarket.setFreePort(freePort);

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                newMarket.getConnectedEntities().add(entity);
            }
        }

        globalEconomy.addMarket(newMarket, withJunkAndChatter);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }

        return newMarket;
    }
    private MarketAPI addMarket(SectorEntityToken entity, String faction, float tarrif, List<String> conditions, List<String> industries, List<String> submarkets) {
        int size = 0;
        for (String condition : conditions) {
            if (condition.startsWith("population_")) {
                String sub = condition.replace("population_", "");
                size = Integer.parseInt(sub);
            }
        }

        MarketAPI market = MagicCampaign.addSimpleMarket(entity, entity.getId(), entity.getName(),
                size, faction, false, false,
                conditions, industries, false, false, false, false, false, false);

        if (conditions.contains("free_market")) market.setFreePort(true);
        for (String submarket : submarkets) {
            market.addSubmarket(submarket);
        }

        market.getTariff().modifyFlat("generator", tarrif);
        Global.getSector().getEconomy().addMarket(market, true);

        entity.setMarket(market);
        entity.setFaction(faction);
        return market;
    }

}
