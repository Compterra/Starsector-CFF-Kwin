package data.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;

public class KE_UE_ExtraIncomePerDay implements EveryFrameScript {

    public static KE_UE_ExtraIncomePerDay getInstance() {
        for (EveryFrameScript script : Global.getSector().getScripts()) {
            if (script instanceof KE_UE_ExtraIncomePerDay) {
                return (KE_UE_ExtraIncomePerDay) script;
            }
        }
        return null;
    }

    public static void register() {
        if (getInstance() != null) {
            return;
        }
        Global.getSector().addScript(new KE_UE_ExtraIncomePerDay());
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
        // Zenith hospitality is handled by KE_UE_LuxuryMod as a ship-local maintenance effect.
        // This legacy script remains registered for save compatibility, but no longer pays direct credits.
    }
}