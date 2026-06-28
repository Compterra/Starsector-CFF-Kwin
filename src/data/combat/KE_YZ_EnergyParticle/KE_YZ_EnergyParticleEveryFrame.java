package data.combat.KE_YZ_EnergyParticle;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;


import java.awt.*;
import java.util.List;

public class KE_YZ_EnergyParticleEveryFrame extends BaseEveryFrameCombatPlugin {
    private final String ROLE_KEY = "KE_YZ_EnergyParticle";
    private final String SOURCE_KEY = "KE_YZ_SourceShip";
    private final String TARGET_KEY = "KE_YZ_EnergyParticle_TargetShip";

    private final IntervalUtil clock = new IntervalUtil(0.5f,0.5f);
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine == null||engine.isPaused()) return;
        clock.advance(amount);
        if(clock.intervalElapsed()) {
            for (MissileAPI missile : engine.getMissiles()) {
                String specId = missile.getProjectileSpecId();
                if (!"KE_YZ_EnergyParticle_shell".equals(specId) &&
                        !"KE_YZ_MS_EnergyParticle_shell".equals(specId) &&
                        !"KE_YZ_MEM_EnergyParticle_shell".equals(specId)) {
                    continue;
                }

                CombatEntityAPI target = null;
                if (missile.getAI() instanceof GuidedMissileAI) {
                    GuidedMissileAI ai = (GuidedMissileAI) missile.getAI();
                    target = ai.getTarget();
                }

                Object upgradeData = missile.getCustomData().get("KE_YZ_EnergyParticle_Upgrades");
                if (!(upgradeData instanceof List)) {
                    continue;
                }

                List upgrades = (List) upgradeData;
                if (upgrades.isEmpty()) {
                    continue;
                }

                for (Object upgradeDataEntry : upgrades) {
                    if (upgradeDataEntry instanceof KE_YZ_EnergyParticleUpgrade) {
                        KE_YZ_EnergyParticleUpgrade upgrade = (KE_YZ_EnergyParticleUpgrade) upgradeDataEntry;
                        upgrade.apply(amount, target, missile, engine);
                    }
                }
            }
        }
    }
}
