package data.scripts;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.input.InputEventAPI;

import java.util.List;

/**
 * Legacy plugin kept so old saves and config references can resolve the class.
 * Dustcloud submunitions are now handled by KE_YZ_DustCloud's explosion hook.
 */
public class KE_YZ_CombatDataPlugin extends BaseEveryFrameCombatPlugin {
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
    }
}