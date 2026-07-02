package data.hullmods;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BuffManagerAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KE_UE_HarmonicResonanceCore extends BaseHullMod {

    protected static final String KE_UE_BUFF_ID = "HarmonicResonanceCore";// Any ID you want is only used to mark stats. It is best to change it to suit your mod.
    protected static Map<ShipAPI.HullSize, Float> BUFF_MAP = new HashMap<>();// buff value

    static {
        BUFF_MAP.put(ShipAPI.HullSize.FRIGATE, 0.95f);
        BUFF_MAP.put(ShipAPI.HullSize.DESTROYER, 0.9f);
        BUFF_MAP.put(ShipAPI.HullSize.CRUISER, 0.85f);
        BUFF_MAP.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.8f);
    }

    public class HarmonicResonanceCoreBuff implements BuffManagerAPI.Buff {
        // private FleetMemberAPI source;
        private String buffId;
        private float time = 0;// initialization

        public HarmonicResonanceCoreBuff(String buffId) {
            this.buffId = buffId;
            // this.source = member;
        }

        public boolean isExpired() {
            return time > 1;// It will disappear automatically after one day at most.
        }

        public String getId() {
            return buffId;
        }

        public void apply(FleetMemberAPI member) {
            if (member.getHullSpec() != null) {
                member.getStats().getFuelUseMod().modifyMult(buffId, BUFF_MAP.get(member.getHullSpec().getHullSize()));
            }
        }

        public void advance(float days) {
            time += days;
        }
    }

    private static boolean isCampaign() {
        try {
            return Global.getCurrentState() == GameState.CAMPAIGN && Global.getSector() != null;
        } catch (Throwable t) {
            return false;
        }
    }

    public void advanceInCampaign(FleetMemberAPI member, float amount) {

        if (member.getFleetData() == null)
            return;
        if (member.getFleetData().getFleet() == null)
            return;
        if (!member.getFleetData().getFleet().isPlayerFleet())
            return;

        boolean shouldAddBuff = true;

        if (!member.canBeDeployedForCombat())
            shouldAddBuff = false;// Check whether the dock can be deployed. If not, cancel the buff.

        FleetDataAPI data = member.getFleetData();
        List<FleetMemberAPI> all = data.getMembersListCopy();

        for (FleetMemberAPI curr : all) {

            //if (curr == member)
            //    continue; //<-Remove these two lines
            if (curr.getHullSpec().getHullSize() == ShipAPI.HullSize.FIGHTER)
                continue;
            if (curr.getBuffManager().getBuff(KE_UE_BUFF_ID) == null) {
                if (shouldAddBuff) {
                    curr.getBuffManager().addBuff(new HarmonicResonanceCoreBuff(KE_UE_BUFF_ID));// There is no buff and buffs can be added
                }
            } else {
                if (shouldAddBuff) {
                    HarmonicResonanceCoreBuff buff = (HarmonicResonanceCoreBuff) curr.getBuffManager()
                            .getBuff(KE_UE_BUFF_ID);// There is a buff and the time is extended
                    buff.time = 0;
                } else {
                    curr.getBuffManager().removeBuff(KE_UE_BUFF_ID);// There is a buff, clear the buff
                }
            }
        }
    }
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        if (stats.getVariant() == null) return;
        if (!isCampaign()) return;
    }
    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {return false;}
    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;

        Color h = Misc.getHighlightColor();
        Color f = new Color(50, 229, 246, 255);
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        tooltip.addPara("Zenith Era's Star-series liners compete with Mbaye-Gogol designs through more than passenger capacity and service quality.\n" +
                        "Their real advantage is the company's proprietary %s.",
                pad, f,
                "'Resonance' Common Navigation Core");
        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("The resonance navigation core links fleet navigation systems through a shared quantum network, improving fuel efficiency without sacrificing cruising speed.",
                pad);
        tooltip.addPara("Fleet fuel consumption reduced by hull size: %s.", pad, good, "10%/15%/20%/25%");
    }
}
