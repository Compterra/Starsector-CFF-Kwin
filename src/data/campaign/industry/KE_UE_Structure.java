package data.campaign.industry;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SubmarketPlugin;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.intel.events.ht.HyperspaceTopographyEventIntel;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.impl.campaign.submarkets.LocalResourcesSubmarketPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

import java.awt.*;

public class KE_UE_Structure extends BaseIndustry implements MarketImmigrationModifier {

    public static float BASE_BONUS = 20f;
    public static float ALPHA_CORE_BONUS = 15f;
    public static float IMPROVE_BONUS = 15f;

    public static float ALPHA_UPKEEP_REDUCTION = 20f;
    public static float BETA_UPKEEP_REDUCTION = 10f;
    public static float GAMMA_UPKEEP_REDUCTION = 5f;

    public static float STABILITY_PENALTY = 1f;

    public static String SPECIALMARKET = "KE_UEMarket";;
    public static String CORPORATE_FACTION = "KE_UECorporation";

    //protected transient CargoAPI savedCargo = null;
    protected transient SubmarketAPI saved = null;

    public void apply() {
        super.apply(true);


        if (isFunctional() && market.isPlayerOwned()) {
            SubmarketAPI open = market.getSubmarket(SPECIALMARKET);
            if (open == null) {
                if (saved != null) {
                    market.addSubmarket(saved);
                } else {
                    market.addSubmarket(SPECIALMARKET);
                    SubmarketAPI sub = market.getSubmarket(SPECIALMARKET);
                    sub.setFaction(Global.getSector().getFaction(CORPORATE_FACTION));
                    Global.getSector().getEconomy().forceStockpileUpdate(market);
                }

//				if (savedCargo != null) {
//					open = market.getSubmarket(Submarkets.SUBMARKET_OPEN);
//					if (open != null) {
//						open.getCargo().clear();
//						open.getCargo().addAll(savedCargo);
//						if (open.getPlugin() instanceof BaseSubmarketPlugin) {
//							BaseSubmarketPlugin base = (BaseSubmarketPlugin) open.getPlugin();
//							base.setSinceLastCargoUpdate(0);
//							base.setSinceSWUpdate(0);
//						}
//					}
//				}
            }
        } else if (market.isPlayerOwned()) {
            market.removeSubmarket(SPECIALMARKET);
        }

        //modifyStabilityWithBaseMod();
        market.getStability().modifyFlat(getModId(), STABILITY_PENALTY, getNameForModifier());

        market.getIncomeMult().modifyPercent(getModId(0), BASE_BONUS, getNameForModifier());

        if (!isFunctional()) {
            unapply();
        }
    }


    @Override
    public void unapply() {
        super.unapply();

        if (market.isPlayerOwned()) {
            SubmarketAPI open = market.getSubmarket(SPECIALMARKET);
            saved = open;
//			if (open.getPlugin() instanceof BaseSubmarketPlugin) {
//				BaseSubmarketPlugin base = (BaseSubmarketPlugin) open.getPlugin();
//				if (base.getSinceLastCargoUpdate() < 30) {
//					savedCargo = open.getCargo();
//				}
//			}
            market.removeSubmarket(SPECIALMARKET);
        }



        market.getStability().unmodifyFlat(getModId());
        market.getIncomeMult().unmodifyPercent(getModId(0));
    }

    protected void addStabilityPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        Color h = Misc.getHighlightColor();
        float opad = 10f;

        float a = BASE_BONUS;
        String aStr = "+" + (int)Math.round(a * 1f) + "%";
        tooltip.addPara("Colonial income:%s", opad, h, aStr);

        tooltip.addPara("Stability:%s", opad, h, "+" + (int)STABILITY_PENALTY);
    }

    @Override
    protected void addRightAfterDescriptionSection(TooltipMakerAPI tooltip, IndustryTooltipMode mode) {
        if (market.isPlayerOwned() || currTooltipMode == IndustryTooltipMode.ADD_INDUSTRY) {
            tooltip.addPara("Establishes a chartered Zenith Era liaison office and opens the Crown Trade Center, a procurement channel focused on luxury hulls, expedition craft, civilian logistics ships, and service equipment.", 10f);
        }
    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
            addStabilityPostDemandSection(tooltip, hasDemand, mode);
        }
    }

    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        incoming.add(CORPORATE_FACTION, 10f);
    }


    public boolean isAvailableToBuild() {
        return market.hasSpaceport();
    }

    public String getUnavailableReason() {
        return "Requires an operational spaceport";
    }

    @Override
    public String getCurrentImage() {
        return getSpec().getImageName();
    }


    @Override
    protected void applyAICoreToIncomeAndUpkeep() {
        if (aiCoreId == null) {
            getUpkeep().unmodifyMult("ind_core");
            return;
        }

        if (Commodities.ALPHA_CORE.equals(aiCoreId)) {
            getUpkeep().modifyMult("ind_core", 1f - ALPHA_UPKEEP_REDUCTION / 100f, "Alpha Core assigned");
        } else if (Commodities.BETA_CORE.equals(aiCoreId)) {
            getUpkeep().modifyMult("ind_core", 1f - BETA_UPKEEP_REDUCTION / 100f, "Beta Core assigned");
        } else if (Commodities.GAMMA_CORE.equals(aiCoreId)) {
            getUpkeep().modifyMult("ind_core", 1f - GAMMA_UPKEEP_REDUCTION / 100f, "Gamma Core assigned");
        } else {
            getUpkeep().unmodifyMult("ind_core");
        }
    }
    //market.getIncomeMult().modifyMult(id, INCOME_MULT, "Industrial planning");
    @Override
    protected void applyAlphaCoreModifiers() {
        market.getIncomeMult().modifyPercent(getModId(1), ALPHA_CORE_BONUS, "Alpha Core (" + getNameForModifier() + ")");
    }
    @Override
    protected void applyBetaCoreModifiers() {
        applyNoAICoreModifiers();
    }

    @Override
    protected void applyGammaCoreModifiers() {
        applyNoAICoreModifiers();
    }

    @Override
    protected void applyNoAICoreModifiers() {
        market.getIncomeMult().unmodifyPercent(getModId(1));
    }

    @Override
    protected void applyAlphaCoreSupplyAndDemandModifiers() {
        demandReduction.modifyFlat(getModId(0), DEMAND_REDUCTION, "Alpha core");
    }

    @Override
    protected void applyGammaCoreSupplyAndDemandModifiers() {
    }

    protected void addAlphaCoreDescription(TooltipMakerAPI tooltip, AICoreDescriptionMode mode) {
        float opad = 10f;
        Color highlight = Misc.getHighlightColor();

        String pre = "Currently allocated Alpha-level AI cores. ";
        if (mode == AICoreDescriptionMode.MANAGE_CORE_DIALOG_LIST || mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            pre = "Alpha-level AI core. ";
        }
        float a = ALPHA_CORE_BONUS;
        String str = "" + (int) Math.round(a) + "%";

        if (mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            CommoditySpecAPI coreSpec = Global.getSettings().getCommoditySpec(aiCoreId);
            TooltipMakerAPI text = tooltip.beginImageWithText(coreSpec.getIconName(), 48);
            text.addPara(pre + "reduces upkeep cost by %s, reduces demand by %s unit, and increases colonial income by %s.", 0f, highlight,
                    "" + (int) ALPHA_UPKEEP_REDUCTION + "%", "" + DEMAND_REDUCTION,
                    str);
            tooltip.addImageWithText(opad);
            return;
        }

        tooltip.addPara(pre + "reduces upkeep cost by %s, reduces demand by %s unit, and increases colonial income by %s.", opad, highlight,
                "" + (int) ALPHA_UPKEEP_REDUCTION + "%", "" + DEMAND_REDUCTION,
                str);

    }
    @Override
    protected void addBetaCoreDescription(TooltipMakerAPI tooltip, AICoreDescriptionMode mode) {
        float opad = 10f;
        Color highlight = Misc.getHighlightColor();

        String pre = "Currently allocated Beta-level AI cores. ";
        if (mode == AICoreDescriptionMode.MANAGE_CORE_DIALOG_LIST || mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            pre = "Beta-level AI core. ";
        }

        if (mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            CommoditySpecAPI coreSpec = Global.getSettings().getCommoditySpec(aiCoreId);
            TooltipMakerAPI text = tooltip.beginImageWithText(coreSpec.getIconName(), 48);
            text.addPara(pre + "reduces upkeep cost by %s and reduces demand by %s unit.", 0f, highlight,
                    "" + (int) BETA_UPKEEP_REDUCTION + "%", "" + DEMAND_REDUCTION);
            tooltip.addImageWithText(opad);
            return;
        }

        tooltip.addPara(pre + "reduces upkeep cost by %s and reduces demand by %s unit.", opad, highlight,
                "" + (int) BETA_UPKEEP_REDUCTION + "%", "" + DEMAND_REDUCTION);
    }

    @Override
    protected void addGammaCoreDescription(TooltipMakerAPI tooltip, AICoreDescriptionMode mode) {
        float opad = 10f;
        Color highlight = Misc.getHighlightColor();

        String pre = "Currently allocated Gamma-level AI cores. ";
        if (mode == AICoreDescriptionMode.MANAGE_CORE_DIALOG_LIST || mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            pre = "Gamma-level AI core. ";
        }

        if (mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            CommoditySpecAPI coreSpec = Global.getSettings().getCommoditySpec(aiCoreId);
            TooltipMakerAPI text = tooltip.beginImageWithText(coreSpec.getIconName(), 48);
            text.addPara(pre + "reduces upkeep cost by %s.", 0f, highlight,
                    "" + (int) GAMMA_UPKEEP_REDUCTION + "%");
            tooltip.addImageWithText(opad);
            return;
        }

        tooltip.addPara(pre + "reduces upkeep cost by %s.", opad, highlight,
                "" + (int) GAMMA_UPKEEP_REDUCTION + "%");
    }


    @Override
    public boolean canImprove() {
        return true;
    }

    protected void applyImproveModifiers() {
        if (isImproved()) {
            market.getIncomeMult().modifyPercent(getModId(2), IMPROVE_BONUS,
                    getImprovementsDescForModifiers() + " (" + getNameForModifier() + ")");
        } else {
            market.getIncomeMult().unmodifyPercent(getModId(2));
        }
    }

    public void addImproveDesc(TooltipMakerAPI info, ImprovementDescriptionMode mode) {
        float opad = 10f;
        Color highlight = Misc.getHighlightColor();

        float a = IMPROVE_BONUS;
        String aStr = "" + (int)Math.round(a * 1f) + "%";

        if (mode == ImprovementDescriptionMode.INDUSTRY_TOOLTIP) {
            info.addPara("Increases colonial income by %s.", 0f, highlight, aStr);
        } else {
            info.addPara("Increases colonial income by %s.", 0f, highlight, aStr);
        }

        info.addSpacer(opad);
        super.addImproveDesc(info, mode);
    }
}

