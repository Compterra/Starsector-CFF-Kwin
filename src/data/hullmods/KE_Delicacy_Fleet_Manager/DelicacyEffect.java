package data.hullmods.KE_Delicacy_Fleet_Manager;

import java.util.HashMap;
import java.util.Map;
/*
*If you don’t need it at the moment, keep it for now and replenish it if needed later.
*/
public class DelicacyEffect {
    private DelicacyCalculator calculator;
    public static DelicacyEffectType getDelicacyEffectType(int level){
        // It is necessary to implement the logic of returning the corresponding effect type according to the level.
        switch(level){
            case 0: return DelicacyEffectType.NONE;
            case 1: return DelicacyEffectType.LEVEL1;
            case 2: return DelicacyEffectType.LEVEL2;
            case 3: return DelicacyEffectType.LEVEL3;
            case 4: return DelicacyEffectType.LEVEL4;
            default: return DelicacyEffectType.NONE;
        }
    }
    // If you need to apply specific game effects, you can add this mapping method
    public static Map<String, Float> getEffectStats(int level) {
        Map<String, Float> stats = new HashMap<>();

        switch (level) {
            case 4: // feast effect

                break;
            case 3: // Gourmet effect

                break;
            case 2: // Standard meal effect

                break;
            case 1: // Basic meal effect

                break;
            case 0: // No meals
            default:
                break;
        }

        return stats;
    }

}
enum DelicacyEffectType{
    NONE,
    LEVEL1,
    LEVEL2,
    LEVEL3,
    LEVEL4,
}