package data.combat.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleUpgrade;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleState;
import org.magiclib.util.MagicTargeting;

import java.util.List;


public class KE_YZ_EnergyParticleAI implements MissileAIPlugin, GuidedMissileAI{
    private MissileAPI missile;
    public CombatEntityAPI target;
    private final ShipAPI launchingShip;
    private final String ROLE_KEY = "KE_YZ_EnergyParticle";
    private final String SOURCE_KEY = "KE_YZ_SourceShip";
    private final String TARGET_KEY = "KE_YZ_EnergyParticle_TargetShip";

    private KE_YZ_EnergyParticleState state = new KE_YZ_EnergyParticleState();
    public KE_YZ_EnergyParticleAI(MissileAPI missile, ShipAPI launchingShip) {
        this.missile = missile;
        this.launchingShip = launchingShip;
    }

    @Override
    public CombatEntityAPI getTarget() {
        return target;
    }

    @Override
    public void setTarget(CombatEntityAPI target) {
        this.target = target;
    }

    @Override
    public void advance(float amount) {
        CombatEngineAPI engine = Global.getCombatEngine();
        ShipAPI sourceship = launchingShip;
//        List<KE_YZ_EnergyParticleUpgrade> upgrades = (List<KE_YZ_EnergyParticleUpgrade>) missile.getCustomData().get("KE_YZ_EnergyParticle_Upgrades");
//        if(missile.getCustomData().containsKey(SOURCE_KEY)) {
//            sourceship = (ShipAPI) missile.getCustomData().get(SOURCE_KEY);
//        }
//        if(!upgrades.isEmpty()){
//            for(KE_YZ_EnergyParticleUpgrade upgrade : upgrades){
//                upgrade.apply(amount, target, missile, engine);
//            }
//        }
        // First level missile status
        // Second level goal setting
        // The third layer source ship
        if(missile.getCustomData().containsKey(ROLE_KEY)){
            if(missile.getCustomData().containsKey(TARGET_KEY)){
                setTarget((CombatEntityAPI) missile.getCustomData().get(TARGET_KEY));
            }
            if(target != sourceship){
                state.setRole(KE_YZ_EnergyParticleState.ParticleRole.ATTACKER);
                if(target != null){
                    state.getRole().run(amount, target, missile);
                }
//                engine.addFloatingText(((ShipAPI) missile.getCustomData().get(SOURCE_KEY)).getLocation(),
//                        "2222222",
//                        10f,
//                        Color.CYAN,
//                        (ShipAPI) missile.getCustomData().get(SOURCE_KEY),
//                        1f,
//                        1f);
            }
            if(target == sourceship){
                KE_YZ_EnergyParticleState.ParticleRole value = (KE_YZ_EnergyParticleState.ParticleRole) missile.getCustomData().get(ROLE_KEY);
                if (value != null) {
                    setTarget(sourceship);
                    state.setRole(value);
                    state.getRole().run(amount, target, missile);
                }
//                engine.addFloatingText(target.getLocation(),
//                        "11111111",
//                        10f,
//                        Color.CYAN,
//                        (ShipAPI) missile.getCustomData().get(SOURCE_KEY),
//                        1f,
//                        1f);
            }
        }
        if(!missile.getCustomData().containsKey(ROLE_KEY)){
            setTarget(MagicTargeting.pickMissileTarget(
                    missile,
                    MagicTargeting.targetSeeking.FULL_RANDOM,
                    1500,
                    360,
                    1,
                    3,
                    5,
                    7,
                    9));
            if(target!=null){
                state.setRole(KE_YZ_EnergyParticleState.ParticleRole.ATTACKER);
                state.getRole().run(amount, target, missile);
            }

        }

        if(missile.isExpired()|| missile.isFizzling()||missile.isFading()){
            missile.removeCustomData(SOURCE_KEY);
            missile.removeCustomData(ROLE_KEY);
            missile.removeCustomData(TARGET_KEY);
        }

    }


}
