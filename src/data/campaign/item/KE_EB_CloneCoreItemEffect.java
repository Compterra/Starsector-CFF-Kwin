package data.campaign.item;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.InstallableIndustryItemPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseInstallableItemEffect;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class KE_EB_CloneCoreItemEffect extends BaseInstallableItemEffect {
    public static final String ID = "KE_EB_CloneCore";

    private static final int FOOD_DEMAND_REDUCTION = -2;
    private static final int ORGANICS_DEMAND_INCREASE = 3;
    private static final int CREW_OUTPUT_INCREASE = 2;
    private static final int ORGANS_OUTPUT_INCREASE = 2;
    private static final int STABILITY_PENALTY = -4;
    private static final int MARINE_DEMAND_REDUCTION = -2;
    private static final float GROUND_DEFENSE_MULT = 1.3f;
    private static final int FLEET_DEMAND_INCREASE = 2;
    private static final float FLEET_SIZE_MULT = 1.25f;
    private static final int FOOD_OUTPUT_INCREASE = 3;

    public KE_EB_CloneCoreItemEffect(String id) {
        super(id);
    }

    @Override
    public void apply(Industry industry) {
        if (!isValid(industry)) {
            return;
        }

        MarketAPI market = industry.getMarket();
        if (isPopulationIndustry(industry)) {
            industry.getDemand(Commodities.FOOD).getQuantity().modifyFlat(ID, FOOD_DEMAND_REDUCTION);
            industry.getDemand(Commodities.ORGANICS).getQuantity().modifyFlat(ID, ORGANICS_DEMAND_INCREASE);
            industry.getSupply(Commodities.CREW).getQuantity().modifyFlat(ID, CREW_OUTPUT_INCREASE);
            industry.getSupply(Commodities.ORGANS).getQuantity().modifyFlat(ID, ORGANS_OUTPUT_INCREASE);
            market.getStability().modifyFlat(ID, STABILITY_PENALTY, "The existence of artificial humans");
        }
        if (isGroundDefenseIndustry(industry)) {
            industry.getDemand(Commodities.MARINES).getQuantity().modifyFlat(ID, MARINE_DEMAND_REDUCTION);
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD)
                    .modifyMult(ID, GROUND_DEFENSE_MULT, "clone army");
        }
        if (isMilitaryCommandIndustry(industry)) {
            industry.getDemand(Commodities.SUPPLIES).getQuantity().modifyFlat(ID, FLEET_DEMAND_INCREASE);
            industry.getDemand(Commodities.FUEL).getQuantity().modifyFlat(ID, FLEET_DEMAND_INCREASE);
            industry.getDemand(Commodities.SHIPS).getQuantity().modifyFlat(ID, FLEET_DEMAND_INCREASE);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT)
                    .modifyMult(ID, FLEET_SIZE_MULT, "clone army");
        }
        if (isFoodIndustry(industry)) {
            industry.getSupply(Commodities.FOOD).getQuantity().modifyFlat(ID, FOOD_OUTPUT_INCREASE);
        }
    }

    @Override
    public void addItemDescriptionImpl(Industry industry, TooltipMakerAPI text, SpecialItemData data,
                                       InstallableIndustryItemPlugin.InstallableItemDescriptionMode mode, String pre, float pad) {
        Color h = Misc.getHighlightColor();
        if (!isValid(industry)) {
            return;
        }

        if (isPopulationIndustry(industry)) {
            text.addPara("Food needs:%s ", pad, h, Integer.toString(FOOD_DEMAND_REDUCTION));
            text.addPara("Organic matter requirements:%s ", pad, h, "+" + ORGANICS_DEMAND_INCREASE);
            text.addPara("Crew output:%s ", pad, h, "+" + CREW_OUTPUT_INCREASE);
            text.addPara("Organ output:%s ", pad, h, "+" + ORGANS_OUTPUT_INCREASE);
            text.addPara("Stability:%s ", pad, h, Integer.toString(STABILITY_PENALTY));
        }
        if (isGroundDefenseIndustry(industry)) {
            text.addPara("Ground defense strength:%s ", pad, h, "x " + GROUND_DEFENSE_MULT);
            text.addPara("Marine requirements:%s ", pad, h, Integer.toString(MARINE_DEMAND_REDUCTION));
        }
        if (isMilitaryCommandIndustry(industry)) {
            text.addPara("Supply requirements:%s ", pad, h, "+" + FLEET_DEMAND_INCREASE);
            text.addPara("Fuel requirements:%s ", pad, h, "+" + FLEET_DEMAND_INCREASE);
            text.addPara("Hull requirements:%s ", pad, h, "+" + FLEET_DEMAND_INCREASE);
            text.addPara("Fleet size:%s ", pad, h, "x" + FLEET_SIZE_MULT);
        }
        if (isFoodIndustry(industry)) {
            text.addPara("Food output:%s ", pad, h, "+" + FOOD_OUTPUT_INCREASE);
        }
    }

    @Override
    public void unapply(Industry industry) {
        if (!isValid(industry)) {
            return;
        }

        MarketAPI market = industry.getMarket();
        if (isPopulationIndustry(industry)) {
            industry.getDemand(Commodities.FOOD).getQuantity().unmodify(ID);
            industry.getDemand(Commodities.ORGANICS).getQuantity().unmodify(ID);
            industry.getSupply(Commodities.CREW).getQuantity().unmodify(ID);
            industry.getSupply(Commodities.ORGANS).getQuantity().unmodify(ID);
            market.getStability().unmodify(ID);
        }
        if (isGroundDefenseIndustry(industry)) {
            industry.getDemand(Commodities.MARINES).getQuantity().unmodify(ID);
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodify(ID);
        }
        if (isMilitaryCommandIndustry(industry)) {
            industry.getDemand(Commodities.SUPPLIES).getQuantity().unmodify(ID);
            industry.getDemand(Commodities.FUEL).getQuantity().unmodify(ID);
            industry.getDemand(Commodities.SHIPS).getQuantity().unmodify(ID);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodify(ID);
        }
        if (isFoodIndustry(industry)) {
            industry.getSupply(Commodities.FOOD).getQuantity().unmodify(ID);
        }
    }

    private static boolean isValid(Industry industry) {
        return industry != null
                && industry.getMarket() != null
                && Global.getSector() != null
                && Global.getSector().getListenerManager() != null;
    }

    private static boolean isPopulationIndustry(Industry industry) {
        return Industries.POPULATION.equals(industry.getId()) || hasTag(industry, "population");
    }

    private static boolean isGroundDefenseIndustry(Industry industry) {
        return Industries.GROUNDDEFENSES.equals(industry.getId())
                || Industries.HEAVYBATTERIES.equals(industry.getId())
                || hasTag(industry, "grounddefenses");
    }

    private static boolean isMilitaryCommandIndustry(Industry industry) {
        return Industries.PATROLHQ.equals(industry.getId())
                || Industries.MILITARYBASE.equals(industry.getId())
                || Industries.HIGHCOMMAND.equals(industry.getId())
                || hasTag(industry, "patrol");
    }

    private static boolean isFoodIndustry(Industry industry) {
        return Industries.FARMING.equals(industry.getId())
                || Industries.AQUACULTURE.equals(industry.getId())
                || hasTag(industry, "farming")
                || hasTag(industry, "aquaculture");
    }

    private static boolean hasTag(Industry industry, String tag) {
        IndustrySpecAPI spec = industry.getSpec();
        return spec != null && spec.hasTag(tag);
    }
}