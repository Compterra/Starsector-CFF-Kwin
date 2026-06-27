package data.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MonthlyReport;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class KE_UE_Service implements EconomyTickListener, TooltipMakerAPI.TooltipCreator,EveryFrameScript {

    public static float DURATION = 365 + 365 + 365 + 20;
    public static int STIPEND = 300000;

//    protected long startTime = 0;
    public KE_UE_Service() {
        Global.getSector().getListenerManager().addListener(this);
//        startTime = Global.getSector().getClock().getTimestamp();
        Global.getSector().getMemoryWithoutUpdate().set("$KE_UE_Service", true);
    }
    public static KE_UE_Service getInstance() {
        for (EveryFrameScript script : Global.getSector().getScripts()) {
            if (script instanceof KE_UE_Service) {
                return (KE_UE_Service) script;
            }
        }
        return null;
    }

    public static void register() {
        // Check if it is already registered
        if (getInstance() != null) {
//            if (ENABLE_LOGGING && log != null) {
//                log.info("listener1 already registered");
//            }
            return;
        }

        // Register new listener
        KE_UE_Service listener = new KE_UE_Service();
        Global.getSector().addScript(listener);

//        if (ENABLE_LOGGING && log != null) {
//            log.info("listener1 registered");
//        }
    }
    public void reportEconomyTick(int iterIndex) {
//        if (!Global.getSettings().getBoolean("enableServiceFee")) return;

        int lastIterInMonth = (int) Global.getSettings().getFloat("economyIterPerMonth") - 1;
        if (iterIndex != lastIterInMonth) return;

//        float daysActive = Global.getSector().getClock().getElapsedDaysSince(startTime);
        MarketAPI ancyra = Global.getSector().getEconomy().getMarket("KE_UE_Service_market");
//        if (daysActive > DURATION || ancyra == null) {
//            Global.getSector().getListenerManager().removeListener(this);
//            Global.getSector().getMemoryWithoutUpdate().unset("$playerReceivingGAStipend");
//            return;
//        }



        MonthlyReport report = SharedData.getData().getCurrentReport();
        int ServiceFee = getServiceFee();
        MonthlyReport.FDNode fleetNode = report.getNode(MonthlyReport.FLEET);

        MonthlyReport.FDNode ServiceFeeNode = report.getNode(fleetNode, "KE_UE_Service");
        ServiceFeeNode.upkeep = ServiceFee;
        ServiceFeeNode.name = "Monthly Custom Service Expense";
        ServiceFeeNode.icon = Global.getSettings().getSpriteName("income_report", "generic_expense");
        ServiceFeeNode.tooltipCreator =this;
    }

    protected int getServiceFee() {
        int Capitalnum = 0;
        int Cruisernum = 0;
        int Destroyernum = 0;
        int Frigatenum = 0;
        if(Global.getSector().getPlayerFleet() !=null){
            CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
            for(FleetMemberAPI member: fleet.getFleetData().getMembersListCopy()){
                if(member.getHullSpec().getHullId().startsWith("KE_UE_")) {
                    if (member.getHullSpec().getHullSize().equals(ShipAPI.HullSize.CAPITAL_SHIP)) {
                        Capitalnum++;
                    }
                    if (member.getHullSpec().getHullSize().equals(ShipAPI.HullSize.CRUISER)) {
                        Cruisernum++;
                    }
                    if (member.getHullSpec().getHullSize().equals(ShipAPI.HullSize.DESTROYER)) {
                        Destroyernum++;
                    }
                    if (member.getHullSpec().getHullSize().equals(ShipAPI.HullSize.FRIGATE)) {
                        Frigatenum++;
                    }
                }
            }
//            return -(Capitalnum*30000+Cruisernum*25000+Destroyernum*15000+Frigatenum*7500);
        }
        return (Capitalnum*3000+Cruisernum*2500+Destroyernum*1500+Frigatenum*750);
    }


    public void reportEconomyMonthEnd() {
    }

    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
//        float daysActive = Global.getSector().getClock().getElapsedDaysSince(startTime);
        tooltip.addPara("Zenith Era Corporation confirms that " +
                        "your exclusive service fee for this month, %s, has been deducted.\n" +
                        "Zenith Era appreciates your continued trust and support.",
                0f, Misc.getHighlightColor(), Misc.getDGSCredits(getServiceFee()));

//        float rem = DURATION - daysActive;
//        int months = (int) (rem / 30f);
//        //if (months > 0 && months <= 12) {
//        if (months > 0) {
//            tooltip.addPara("You should continue receiving the stipend for another %s months.", 10f,
//                    Misc.getHighlightColor(), "" + months);
//        } else if (months <= 0) {
//            tooltip.addPara("This month's payment was the last.", 10f);
//        }
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
