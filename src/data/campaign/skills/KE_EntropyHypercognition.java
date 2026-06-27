package data.campaign.skills;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class KE_EntropyHypercognition {

    public static float ACCESS = 0.1f;
    public static float FLEET_SIZE = 20f;
    public static int DEFEND_BONUS = 50;
    public static float STABILITY_BONUS = 1;


    public static class Level1 implements MarketSkillEffect {
        private float TRUEACCESS = 1f;
        public void apply(MarketAPI market, String id, float level) {
            if(market.hasCondition(Conditions.HOT)){
                TRUEACCESS = 1.25f;
            }
            if(market.hasCondition(Conditions.VERY_HOT)){
                TRUEACCESS = 1.75f;
            }
            market.getAccessibilityMod().modifyFlat(id, ACCESS*TRUEACCESS, "High-Temperature Adaptation");
        }

        public void unapply(MarketAPI market, String id) {
            market.getAccessibilityMod().unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)Math.round(ACCESS*TRUEACCESS * 100f) + "% Liquidity";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    public static class Level2 implements MarketSkillEffect {
        private float TRUE_FLEET_SIZE = 1f;
        public void apply(MarketAPI market, String id, float level) {
            if(market.hasCondition(Conditions.HOT)){
                TRUE_FLEET_SIZE = 1.25f;
            }
            if(market.hasCondition(Conditions.VERY_HOT)){
                TRUE_FLEET_SIZE = 1.75f;
            }
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat(id, FLEET_SIZE*TRUE_FLEET_SIZE / 100f, "High-Temperature Adaptation");
        }

        public void unapply(MarketAPI market, String id) {
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            //return "" + (int)Math.round(FLEET_SIZE) + "% larger fleets";
            return "+" + (int)Math.round(FLEET_SIZE*TRUE_FLEET_SIZE) + "% fleet size";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    public static class Level3 implements MarketSkillEffect {
        private float TRUE_DEFEND_BONUS = 1f;
        public void apply(MarketAPI market, String id, float level) {
            if(market.hasCondition(Conditions.HOT)){
                TRUE_DEFEND_BONUS = 1.25f;
            }
            if(market.hasCondition(Conditions.VERY_HOT)){
                TRUE_DEFEND_BONUS = 1.75f;
            }
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).modifyMult(id, 1f + DEFEND_BONUS*TRUE_DEFEND_BONUS * 0.01f, "High-Temperature Adaptation");
        }

        public void unapply(MarketAPI market, String id) {
            //market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyPercent(id);
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyMult(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)(DEFEND_BONUS*TRUE_DEFEND_BONUS) + "% Ground defense effective strength";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    public static class Level4 implements MarketSkillEffect {
        private float TRUE_STABILITY_BONUS = 0f;
        public void apply(MarketAPI market, String id, float level) {
            if(market.hasCondition(Conditions.HOT)){
                TRUE_STABILITY_BONUS = 1f;
            }
            if(market.hasCondition(Conditions.VERY_HOT)){
                TRUE_STABILITY_BONUS = 2f;
            }
            market.getStability().modifyFlat(id, STABILITY_BONUS+TRUE_STABILITY_BONUS, "High-Temperature Adaptation");
        }

        public void unapply(MarketAPI market, String id) {
            market.getStability().unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)(STABILITY_BONUS+TRUE_STABILITY_BONUS) + " stability";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }
}
