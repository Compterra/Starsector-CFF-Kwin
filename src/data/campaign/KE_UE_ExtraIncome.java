package data.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MonthlyReport;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class KE_UE_ExtraIncome implements EconomyTickListener, TooltipMakerAPI.TooltipCreator, EveryFrameScript {

    public KE_UE_ExtraIncome() {
        Global.getSector().getListenerManager().addListener(this);
        Global.getSector().getMemoryWithoutUpdate().set("$KE_UE_ExtraIncome", true);
    }

    public static KE_UE_ExtraIncome getInstance() {
        for (EveryFrameScript script : Global.getSector().getScripts()) {
            if (script instanceof KE_UE_ExtraIncome) {
                return (KE_UE_ExtraIncome) script;
            }
        }
        return null;
    }

    public static void register() {
        if (getInstance() != null) {
            return;
        }
        Global.getSector().addScript(new KE_UE_ExtraIncome());
    }

    public void reportEconomyTick(int iterIndex) {
        int extraIncome = getExtraIncome();
        if (extraIncome <= 0) {
            return;
        }

        int lastIterInMonth = (int) Global.getSettings().getFloat("economyIterPerMonth") - 1;
        if (iterIndex != lastIterInMonth) {
            return;
        }

        MonthlyReport report = SharedData.getData().getCurrentReport();
        MonthlyReport.FDNode fleetNode = report.getNode(MonthlyReport.FLEET);
        MonthlyReport.FDNode extraIncomeNode = report.getNode(fleetNode, "KE_UE_ExtraIncome");
        extraIncomeNode.income = extraIncome;
        extraIncomeNode.name = "Zenith hospitality income";
        extraIncomeNode.icon = Global.getSettings().getSpriteName("income_report", "generic_income");
        extraIncomeNode.tooltipCreator = this;
    }

    protected int getExtraIncome() {
        return 0;
    }

    public void reportEconomyMonthEnd() {
    }

    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
        tooltip.addPara("Zenith hospitality revenue is now handled as ship-local maintenance efficiency instead of a fleetwide cash payout. Current direct income: %s.",
                0f, Misc.getHighlightColor(), Misc.getDGSCredits(getExtraIncome()));
    }

    public float getTooltipWidth(Object tooltipParam) {
        return 450;
    }

    public boolean isTooltipExpandable(Object tooltipParam) {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
    }
}