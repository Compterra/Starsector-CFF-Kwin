package data.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class KE_EntropyCombatSkill {

    public static float DAMAGE_TO_MODULES_BONUS = 100;
//	public static float DAMAGE_TO_SHIELDS_BONUS = 15;
//	public static float HIT_STRENGTH_BONUS = 50;

    public static float DAMAGE_TO_FRIGATES = 5;
    public static float DAMAGE_TO_DESTROYERS = 7.5f;
    public static float DAMAGE_TO_CRUISERS = 10;
    public static float DAMAGE_TO_CAPITALS = 12.5f;


    public static class Level1 implements ShipSkillEffect {
        public void apply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id, float level) {
            stats.getDamageToDestroyers().modifyPercent(id, DAMAGE_TO_DESTROYERS);
            stats.getFluxCapacity().modifyPercent(id, 50f);
            stats.getFluxDissipation().modifyPercent(id, 25f);
        }

        public void unapply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id) {
            stats.getDamageToDestroyers().unmodifyPercent(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)(DAMAGE_TO_DESTROYERS) + "% damage to destroyers\n" +
                    "+" + 50 + "% energy capacity\n" +
                    "+"+ 25 + "% flux dissipation";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.PILOTED_SHIP;
        }
    }

    public static class Level1A implements ShipSkillEffect {
        public void apply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id, float level) {
            stats.getDamageToFrigates().modifyPercent(id, DAMAGE_TO_FRIGATES);
        }

        public void unapply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id) {
            stats.getDamageToFrigates().unmodifyPercent(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)(DAMAGE_TO_FRIGATES) + "% damage to frigates";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.PILOTED_SHIP;
        }
    }

    public static class Level2 implements ShipSkillEffect {
        public void apply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id, float level) {
            stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_CRUISERS);
        }

        public void unapply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id) {
            stats.getDamageToCruisers().unmodifyPercent(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)(DAMAGE_TO_CRUISERS) + "% damage to cruiser";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.PILOTED_SHIP;
        }
    }

    public static class Level3 implements ShipSkillEffect {
        public void apply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id, float level) {
            stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_CAPITALS);
        }

        public void unapply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id) {
            stats.getDamageToCapital().unmodifyPercent(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)(DAMAGE_TO_CAPITALS) + "% damage to battleships";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.PILOTED_SHIP;
        }
    }




    public static class Level4 implements ShipSkillEffect {

        public void apply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id, float level) {
            stats.getDamageToTargetEnginesMult().modifyPercent(id, DAMAGE_TO_MODULES_BONUS);
            stats.getDamageToTargetWeaponsMult().modifyPercent(id, DAMAGE_TO_MODULES_BONUS);
        }

        public void unapply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id) {
            stats.getDamageToTargetEnginesMult().unmodify(id);
            stats.getDamageToTargetWeaponsMult().unmodify(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)(DAMAGE_TO_MODULES_BONUS) + "% damage to weapon engine";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.PILOTED_SHIP;
        }
    }

}
