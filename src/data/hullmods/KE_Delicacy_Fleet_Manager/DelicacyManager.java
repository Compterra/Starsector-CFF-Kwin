package data.hullmods.KE_Delicacy_Fleet_Manager;

import com.fs.starfarer.api.Global;

public class DelicacyManager {
    private static DelicacyCalculator calculator;

    // Private constructor prevents external instantiation
    private DelicacyManager() {}

    // Get singleton instance
    public static DelicacyCalculator getCalculator() {
        if (calculator == null) {
            calculator = new DelicacyCalculator(Global.getSector().getPlayerFleet());
        }
        return calculator;
    }

    // Refresh calculator (called when fleet changes)
    public static void refreshCalculator() {
        calculator = new DelicacyCalculator(Global.getSector().getPlayerFleet());
    }

    // Quick method: Get the current effect level directly
    public static int getCurrentDelicacyLevel() {
        return getCalculator().CalculateDelicacyLevel();
    }
}
