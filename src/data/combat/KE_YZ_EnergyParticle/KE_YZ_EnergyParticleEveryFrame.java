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
                if (missile.getProjectileSpecId().equals("KE_YZ_EnergyParticle_shell")||
                        missile.getProjectileSpecId().equals("KE_YZ_MS_EnergyParticle_shell")||
                           missile.getProjectileSpecId().equals("KE_YZ_MEM_EnergyParticle_shell")) {
                    ShipAPI sourceship = missile.getSource() != null ? missile.getSource() : null;
                    GuidedMissileAI ai = (GuidedMissileAI) missile.getAI();
                    CombatEntityAPI target = ai.getTarget();
                    List<KE_YZ_EnergyParticleUpgrade> upgrades = (List<KE_YZ_EnergyParticleUpgrade>) missile.getCustomData().get("KE_YZ_EnergyParticle_Upgrades");
                    if (missile.getCustomData().containsKey(SOURCE_KEY)) {
                        sourceship = (ShipAPI) missile.getCustomData().get(SOURCE_KEY);
                    }
                    if (!upgrades.isEmpty()) {
                        for (KE_YZ_EnergyParticleUpgrade upgrade : upgrades) {
//                            engine.addFloatingText(
//                                    Global.getCombatEngine().getPlayerShip().getLocation(),
//                                    "not null",
//                                    10f,
//                                    Color.WHITE,
//                                    Global.getCombatEngine().getPlayerShip(),
//                                    1f,
//                                    1f
//                            );
                            upgrade.apply(amount, target, missile, engine);
                        }
                    }

                }
            }
        }
    }
}
