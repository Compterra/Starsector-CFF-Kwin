package data.dlicacy;


import com.fs.starfarer.api.combat.*;
import data.combat.KE_YZ_EnergyParticle.KE_YZ_EnergyParticleState;
import data.hullmods.KE_YZ_EnergySwarmCore;
import org.magiclib.util.MagicTargeting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KET_BaseEnergyMissileEffect implements EveryFrameWeaponEffectPlugin {
    private boolean lastFiring = false;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) return;
        ShipAPI ship = weapon.getShip();
        if (ship == null || !ship.isAlive()) return;

        // Get status
        KE_YZ_EnergySwarmCore.ShipState state = KE_YZ_EnergySwarmCore.getShipState(ship);
        if (state == null) return;

        // Only triggers when weapon fires
        boolean isFiring = weapon.isFiring();
        if (isFiring && !lastFiring) {
            List<MissileAPI> active = new ArrayList<>();
            for (MissileAPI m : state.getChildrenCopy()) {
                if (m != null && engine.isEntityInPlay(m) && !m.isExpired()) {
                    active.add(m);
                }
            }
            if (active.size() > 0) {
                Collections.shuffle(active);
                int toChange = Math.min(3, active.size());
                for (int i = 0; i < toChange; i++) {
                    active.get(i).setCustomData("KE_YZ_EnergyParticle",
                            KE_YZ_EnergyParticleState.ParticleRole.ATTACKER);
                    active.get(i).setCustomData("KE_YZ_EnergyParticle_TargetShip", ship.getShipTarget()==null ? MagicTargeting.pickMissileTarget(
                            active.get(i),
                            MagicTargeting.targetSeeking.FULL_RANDOM,
                            15000,
                            360,
                            1,
                            3,
                            5,
                            7,
                            9) : ship.getShipTarget());
                    engine.spawnEmpArcVisual(
                            weapon.getLocation(),
                            weapon.getShip(),
                            active.get(i).getLocation(),
                            active.get(i),
                            5f,
                            Color.BLUE,
                            Color.white
                            );
                }
            }
        }
        lastFiring = isFiring;
    }
}
