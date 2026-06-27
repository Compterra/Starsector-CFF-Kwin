package data.combat.KE_YZ_EnergyParticle;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.util.MagicTargeting;


import static data.combat.ai.util.KE_MissileGuideUtil.*;

public class KE_YZ_EnergyParticleState {
    private ParticleRole role;

    public ParticleRole getRole() {
        return role;
    }
    public void setRole(ParticleRole role) {
        this.role = role;
    }
    public enum ParticleRole {
        ATTACKER {
            public void run(float amount, CombatEntityAPI target, MissileAPI missile) {

                if (target == null) target = MagicTargeting.pickMissileTarget(
                        missile,
                        MagicTargeting.targetSeeking.FULL_RANDOM,
                        15000,
                        360,
                        1,
                        3,
                        5,
                        7,
                        9);

                // Dynamic threshold: the closer the distance, the stricter the requirements; it can be relaxed when the distance is far
                float dotThreshold = 0.95f;

                boolean inFront = isTargetInFront(missile, target, dotThreshold);
                float turnDir = getTurnDirection(missile, target);
                float dot = getDotProduct(missile, target);
                if (inFront) {
                    missile.giveCommand(ShipCommand.ACCELERATE);
                } else {
                    if(dot > 0) {
                        if (turnDir > 0.2588) missile.giveCommand(ShipCommand.TURN_RIGHT);
                        if (turnDir < -0.2588) missile.giveCommand(ShipCommand.TURN_LEFT);
                        if (Math.abs(turnDir) < 0.2588) {
                            missile.giveCommand(ShipCommand.ACCELERATE);
                        }
                    }
                    if (dot < 0) {
                        // Added dead zone: When the target is almost directly behind (turnDir is close to 0), the target will turn right to prevent left and right shaking.
                        if (Math.abs(turnDir) < 0.1f) {
                            missile.giveCommand(ShipCommand.TURN_RIGHT);
                        } else if (turnDir > 0) {
                            missile.giveCommand(ShipCommand.TURN_RIGHT);
                        } else {
                            missile.giveCommand(ShipCommand.TURN_LEFT);
                        }
                        missile.giveCommand(ShipCommand.DECELERATE);
                    }
//                    // Key fix: Slow down when facing away from the target to avoid moving away
//                    if(!isTargetInFront(missile, target, 0.5f)) {
//                        missile.giveCommand(ShipCommand.DECELERATE);
//                    }
                }
                if (target != null && Misc.getDistance(missile.getLocation(), target.getLocation()) > 300) {
                    missile.giveCommand(ShipCommand.ACCELERATE);
                }
                // Optional: Slow down slightly when the distance is too close to prevent overshooting
//                if (Misc.getDistance(missile.getLocation(), target.getLocation()) < 200) {
//                    missile.giveCommand(ShipCommand.DECELERATE);
//                }
            }
        },
        DEFENDER{
            public void run(float amount, CombatEntityAPI target, MissileAPI missile) {
                // A version using dot product optimization
                if (isTargetInFront(missile, target, 0.45f)) {
                    // The target is in the cone area ahead, speed up the pursuit
                    missile.giveCommand(ShipCommand.ACCELERATE);
                } else {
                    // The target is not in front and needs to be turned
                    float turnDirection = getTurnDirection(missile, target);
                    if (turnDirection > 0) {
                        // The target is on the right, turn right
                        missile.giveCommand(ShipCommand.TURN_RIGHT);
                    } else {
                        // Target is on the left, turn left
                        missile.giveCommand(ShipCommand.TURN_LEFT);
                    }
                }
//                missile.giveCommand(ShipCommand.ACCELERATE);

                // Always accelerate until target is hit (if distance is greater than 1500 units)
                if (target != null && Misc.getDistance(missile.getLocation(), target.getLocation()) > target.getCollisionRadius()*0.5f) {
                    missile.giveCommand(ShipCommand.ACCELERATE);
                }
            }
        },
        OTHER{
            public void run(float amount, CombatEntityAPI target, MissileAPI missile) {
                if (target == null) return;

                // Dynamic threshold: the closer the distance, the stricter the requirements; it can be relaxed when the distance is far
                float dotThreshold = (Misc.getDistance(missile.getLocation(), target.getLocation()) > 1000) ? 0.94f : 0.9848f;

                boolean inFront = isTargetInFront(missile, target, dotThreshold);

                if (inFront) {
                    missile.giveCommand(ShipCommand.ACCELERATE);
                } else {
                    float turnDir = getTurnDirection(missile, target);
                    if (turnDir > 0) missile.giveCommand(ShipCommand.TURN_RIGHT);
                    else missile.giveCommand(ShipCommand.TURN_LEFT);

                    // Key Fix: Slow down when facing away from target, avoid moving away
                    missile.giveCommand(ShipCommand.DECELERATE);
                }

                // Optional: Slow down slightly when the distance is too close to prevent overshooting
//                if (Misc.getDistance(missile.getLocation(), target.getLocation()) < 200) {
//                    missile.giveCommand(ShipCommand.DECELERATE);
//                }
            }
        };
        public  abstract void run(float amount, CombatEntityAPI target, MissileAPI missile);
    }
}
