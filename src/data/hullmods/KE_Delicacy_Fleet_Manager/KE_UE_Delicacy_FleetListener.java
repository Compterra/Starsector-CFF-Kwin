package data.hullmods.KE_Delicacy_Fleet_Manager;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.IntervalUtil;

import java.util.List;

public class KE_UE_Delicacy_FleetListener implements EveryFrameScript {
    private static final String FEAST = "KE_UE_Delicacy_Feast";
    private static final String FINE = "KE_UE_Delicacy_FineMeal";
    private static final String STANDARD = "KE_UE_Delicacy_StandardMeal";
    private static final String BASIC = "KE_UE_Delicacy_BasicMeal";

    private static final String FEAST_BUFF = "KE_UE_Delicacy_Feast_H";
    private static final String FINE_BUFF = "KE_UE_Delicacy_FineMeal_H";
    private static final String STANDARD_BUFF = "KE_UE_Delicacy_StandardMeal_H";
    private static final String BASIC_BUFF = "KE_UE_Delicacy_BasicMeal_H";

    private final IntervalUtil syncTimer = new IntervalUtil(1f, 1f);

    public static KE_UE_Delicacy_FleetListener getInstance() {
        for (EveryFrameScript script : Global.getSector().getScripts()) {
            if (script instanceof KE_UE_Delicacy_FleetListener) {
                return (KE_UE_Delicacy_FleetListener) script;
            }
        }
        return null;
    }

    public static void register() {
        if (getInstance() == null) {
            Global.getSector().addScript(new KE_UE_Delicacy_FleetListener());
        }
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
        syncTimer.advance(amount);
        if (!syncTimer.intervalElapsed()) {
            return;
        }

        syncPlayerFleet();
        syncNpcFleets();
    }

    private void syncNpcFleets() {
        for (StarSystemAPI system : Global.getSector().getStarSystems()) {
            for (CampaignFleetAPI fleet : system.getFleets()) {
                if (fleet.isPlayerFleet()) continue;
                List<FleetMemberAPI> members = fleet.getFleetData().getMembersListCopy();
                int level = getHighestProviderLevel(members);
                for (FleetMemberAPI member : members) {
                    applyOnlyBuffLevel(member, level);
                }
            }
        }
    }

    private void syncPlayerFleet() {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null) return;

        List<FleetMemberAPI> members = playerFleet.getFleetData().getMembersListCopy();
        int feastCapacity = 0;
        int fineCapacity = 0;
        int standardCapacity = 0;
        int basicCapacity = 0;

        for (FleetMemberAPI member : members) {
            int level = getProviderLevel(member);
            int capacity = getSupportCapacity(member);
            if (level == 4) feastCapacity += capacity;
            if (level == 3) fineCapacity += capacity;
            if (level == 2) standardCapacity += capacity;
            if (level == 1) basicCapacity += capacity;
        }

        putCapacity(playerFleet, "Feast", feastCapacity);
        putCapacity(playerFleet, "FineMeal", fineCapacity);
        putCapacity(playerFleet, "StandardMeal", standardCapacity);
        putCapacity(playerFleet, "BasicMeal", basicCapacity);
        DelicacyManager.refreshCalculator();

        int supportLevel = DelicacyManager.getCurrentDelicacyLevel();
        for (FleetMemberAPI member : members) {
            if (member.getHullSpec().getHullSize() == ShipAPI.HullSize.FIGHTER) {
                applyOnlyBuffLevel(member, supportLevel);
            }
        }
    }

    private int getHighestProviderLevel(List<FleetMemberAPI> members) {
        int level = 0;
        for (FleetMemberAPI member : members) {
            level = Math.max(level, getProviderLevel(member));
        }
        return level;
    }

    private int getProviderLevel(FleetMemberAPI member) {
        if (member == null || member.getVariant() == null) return 0;
        if (member.getVariant().hasHullMod(FEAST)) return 4;
        if (member.getVariant().hasHullMod(FINE)) return 3;
        if (member.getVariant().hasHullMod(STANDARD)) return 2;
        if (member.getVariant().hasHullMod(BASIC)) return 1;
        return 0;
    }

    private int getSupportCapacity(FleetMemberAPI member) {
        if (member == null || member.getHullSpec() == null) return 0;
        ShipAPI.HullSize size = member.getHullSpec().getHullSize();
        if (size == ShipAPI.HullSize.CAPITAL_SHIP) return 2500;
        if (size == ShipAPI.HullSize.CRUISER) return 750;
        if (size == ShipAPI.HullSize.DESTROYER) return 350;
        if (size == ShipAPI.HullSize.FRIGATE) return 65;
        return 0;
    }

    private void putCapacity(CampaignFleetAPI fleet, String key, int value) {
        if (value > 0) {
            fleet.getCustomData().put(key, value);
        } else {
            fleet.getCustomData().remove(key);
        }
    }

    private void applyOnlyBuffLevel(FleetMemberAPI member, int level) {
        if (member == null || member.getVariant() == null) return;
        removeBuff(member, FEAST_BUFF);
        removeBuff(member, FINE_BUFF);
        removeBuff(member, STANDARD_BUFF);
        removeBuff(member, BASIC_BUFF);

        if (level == 4) addBuff(member, FEAST_BUFF);
        if (level == 3) addBuff(member, FINE_BUFF);
        if (level == 2) addBuff(member, STANDARD_BUFF);
        if (level == 1) addBuff(member, BASIC_BUFF);
    }

    private void addBuff(FleetMemberAPI member, String id) {
        if (!member.getVariant().hasHullMod(id)) {
            member.getVariant().addMod(id);
        }
    }

    private void removeBuff(FleetMemberAPI member, String id) {
        if (member.getVariant().hasHullMod(id)) {
            member.getVariant().removeMod(id);
        }
    }
}