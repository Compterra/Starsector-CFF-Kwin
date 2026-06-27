package data.hullmods.KE_Delicacy_Fleet_Manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;

public class DelicacyCalculator {
    private CampaignFleetAPI fleet;
    private Integer MaxDelicacyLevel = 0;
    private Integer ActuallyDelicacyLevel = 0;

    private Integer FEAST = 0;
    private Integer FINEMEAL = 0;
    private Integer STANDARDMEAL = 0;
    private Integer BASICMEAL = 0;

    public DelicacyCalculator(CampaignFleetAPI PlayerFleet) {
        if (Global.getSector().getPlayerFleet() == PlayerFleet) {
            this.fleet = PlayerFleet;
            refreshAllMealCounts();
        } else {
            this.fleet = null;
        }
    }

    // Refresh all meal quantities
    private void refreshAllMealCounts() {
        if (fleet == null) return;

        FEAST = fleet.getCustomData().containsKey("Feast") ?
                (Integer) fleet.getCustomData().get("Feast") : 0;
        FINEMEAL = fleet.getCustomData().containsKey("FineMeal") ?
                (Integer) fleet.getCustomData().get("FineMeal") : 0;
        STANDARDMEAL = fleet.getCustomData().containsKey("StandardMeal") ?
                (Integer) fleet.getCustomData().get("StandardMeal") : 0;
        BASICMEAL = fleet.getCustomData().containsKey("BasicMeal") ?
                (Integer) fleet.getCustomData().get("BasicMeal") : 0;
    }

    // Get maximum support level
    private Integer getMaxLevelFromData() {
        if (fleet == null) return 0;

        if (FEAST > 0) return 4;
        if (FINEMEAL > 0) return 3;
        if (STANDARDMEAL > 0) return 2;
        if (BASICMEAL > 0) return 1;

        return 0;
    }

    public Integer CalculateDelicacyLevel() {
        if (fleet == null) return 0;

        // Refresh all data before each calculation
        refreshAllMealCounts();

        int Crews = fleet.getCargo().getTotalCrew();

        // Get maximum level
        int maxLevel = getMaxLevelFromData();
        MaxDelicacyLevel = maxLevel;

        // Starting from the highest level and working downwards, find the first satisfying level
        for (int level = maxLevel; level >= 1; level--) {
            int supportNum = calculateSupportForLevel(level);

            // Determine whether the current level is met
            if (supportNum >= Crews) {
                ActuallyDelicacyLevel = level;
                return level;
            }

            // Check whether the number of supported people exceeds the current level and is within 50%
            // If yes, and it is not the lowest level (level 1), then lower it by one level
            if (level > 1 && supportNum >= Crews * 0.5) {
                ActuallyDelicacyLevel = level - 1;
                return level - 1;
            }
        }

        // If neither is satisfied, return to level 0
        ActuallyDelicacyLevel = 0;
        return 0;
    }

    /**
     * Calculate the number of supporters for a specified level
     * Rules based on requirement description:
     * - When the meal level is equal to the target level, the weight is 1
     * - When the meal level is lower than the target level, each lower level is multiplied by a factor of 1.5
     * - When the meal level is higher than the target level, each higher level is multiplied by a coefficient of 0.75
     */
    private int calculateSupportForLevel(int targetLevel) {
        double totalSupport = 0.0;

        // Calculate the contribution of Level 4 Feast
        if (FEAST > 0) {
            int levelDiff = 4 - targetLevel; // 4 is the level of Huayan
            totalSupport += FEAST * getConversionFactor(levelDiff);
        }

        // Calculate the contribution of Level 3 FineMeal
        if (FINEMEAL > 0) {
            int levelDiff = 3 - targetLevel; // 3 is the level of fine cuisine
            totalSupport += FINEMEAL * getConversionFactor(levelDiff);
        }

        // Calculate the contribution of Level 2 Standard Meal
        if (STANDARDMEAL > 0) {
            int levelDiff = 2 - targetLevel; // 2 is the level of regular meals
            totalSupport += STANDARDMEAL * getConversionFactor(levelDiff);
        }

        // Calculate the contribution of Level 1 BasicMeal
        if (BASICMEAL > 0) {
            int levelDiff = 1 - targetLevel; // 1 is the level of coarse food
            totalSupport += BASICMEAL * getConversionFactor(levelDiff);
        }

        return (int) Math.floor(totalSupport); // Round down
    }

    /**
     * Calculate conversion factor based on grade difference
     * @param levelDiff level difference (meal level - target level)
     * @return conversion coefficient
     */
    private double getConversionFactor(int levelDiff) {
        if (levelDiff == 0) {
            return 1.0; // Same level, weight is 1
        } else if (levelDiff > 0) {
            // The meal level is higher than the target level, multiplied by 0.75 for each higher level
            // For example: 1 level higher: 0.75, 2 levels higher: 0.75^2=0.5625, 3 levels higher: 0.75^3=0.421875
            return Math.pow(0.75, levelDiff);
        } else {
            // If the meal level is lower than the target level, each lower level will be multiplied by 1.5
            // For example: 1 level lower: 1.5, 2 levels lower: 2.0, 3 levels lower: 2.5
            return 1.0 + 0.5 * Math.abs(levelDiff);
        }
    }

    // Get the maximum support level (public method)
    public Integer GetMaxSupportLevel() {
        if (fleet == null) return 0;
        refreshAllMealCounts();
        MaxDelicacyLevel = getMaxLevelFromData();
        return MaxDelicacyLevel;
    }

    // Get the current actual level
    public Integer GetActuallyDelicacyLevel() {
        return ActuallyDelicacyLevel;
    }

    // Check if the calculator is valid
    public boolean isValid() {
        return fleet != null;
    }

    // Get meal quantity statistics
    public String getMealSummary() {
        return String.format("feast:%d, gourmet food:%d, standard meal:%d, basic meal:%d",
                FEAST, FINEMEAL, STANDARDMEAL, BASICMEAL);
    }

    // Debugging method: display detailed calculation process
    public String getCalculationDetails() {
        if (fleet == null) return "Fleet is invalid";

        refreshAllMealCounts();
        int crewCount = fleet.getCargo().getTotalCrew();
        int maxLevel = getMaxLevelFromData();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total crew:%d\n", crewCount));
        sb.append(String.format("Maximum meal level:%d\n", maxLevel));
        sb.append(String.format("Number of meals:%s\n", getMealSummary()));

        // Calculate each level in detail
        for (int level = maxLevel; level >= 1; level--) {
            sb.append(String.format("\n--- Check level%d ---\n", level));

            int supportNum = calculateSupportForLevel(level);
            sb.append(String.format("Calculate the number of supporters:%d(need:%d) - %s\n",
                    supportNum, crewCount,
                    supportNum >= crewCount ? "✓ Satisfy" : "✗ Not satisfied"));

            if (supportNum >= crewCount) {
                sb.append(String.format("→Achievable level:%d\n", level));
                break;
            } else if (level > 1 && supportNum >= crewCount * 0.5) {
                sb.append(String.format("→ over 50%%Within 1 level, lower one level:%d\n", level - 1));
                break;
            } else if (level == 1) {
                sb.append("→ Unable to meet minimum level\n");
                break;
            }
        }

        return sb.toString();
    }
}