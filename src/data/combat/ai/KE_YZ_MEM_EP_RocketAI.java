package data.combat.ai;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.combat.ai.AI;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class KE_YZ_MEM_EP_RocketAI implements MissileAIPlugin, GuidedMissileAI {
    private final MissileAPI missile;
    private CombatEntityAPI target=null;
    private ShipAPI launchingShip;
    private float timer = 0;
    private boolean fire = false;
    private boolean allowGuided = false;
    private int fireCount = 0;

    public KE_YZ_MEM_EP_RocketAI(MissileAPI missile, ShipAPI launchingShip) {
        this.missile = missile;
        this.launchingShip = launchingShip;
    }

    @Override
    public void advance(float amount) {
        if (target == null){
            for(ShipAPI ship : AIUtils.getNearbyEnemies(missile, 1500f)){
                if(ship.getHullSize().ordinal() > ShipAPI.HullSize.FIGHTER.ordinal()){
                    target = ship;
                }
            }
//            if(AIUtils.getNearbyEnemies(missile, 1500f).isEmpty()){
//                target = AIUtils.getNearestEnemy(launchingShip);
//            }
        }

        timer += amount;
        if (target == null) {
            missile.giveCommand(ShipCommand.ACCELERATE);
        }
        // A version using dot product optimization
        if (target != null && AngleWithTarget(missile, target) >= 0.9961946f && timer >= 2f && timer <= 3.5f) {
            missile.giveCommand(ShipCommand.ACCELERATE);
        } else if (target != null && AngleWithTarget(missile, target) < 0.9961946f && timer <= 2f) {
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
        if (timer >= 2f && timer <= 3.5f) {
            missile.giveCommand(ShipCommand.ACCELERATE);
        }
        if (timer >= 3.5f) {
            if (target != null && AngleWithTarget(missile, target) < 0.9961946f && !allowGuided) {
                if (!allowGuided) {
                    missile.giveCommand(ShipCommand.DECELERATE);
                }
                // The target is not in front and needs to be turned
                float turnDirection = getTurnDirection(missile, target);
                if (turnDirection > 0) {
                    // The target is on the right, turn right
                    missile.giveCommand(ShipCommand.TURN_RIGHT);
                } else {
                    // Target is on the left, turn left
                    missile.giveCommand(ShipCommand.TURN_LEFT);
                }
            } else if (target != null && AngleWithTarget(missile, target) >= 0.9961946f) {
                if (timer >= 5f) {
                    allowGuided = true;
                }
            }
            if (allowGuided) {
                missile.giveCommand(ShipCommand.ACCELERATE);
            }

        }
    }

    public float AngleWithTarget(MissileAPI missile, CombatEntityAPI target) {
        Vector2f missileLoc = missile.getLocation();
        Vector2f targetLoc = target.getLocation();

        // Calculate missile heading vector
        float facing = missile.getFacing();
        Vector2f missileDir = new Vector2f(
                (float)Math.cos(Math.toRadians(facing)),
                (float)Math.sin(Math.toRadians(facing))
        );

        // Calculate the vector pointing to the target
        Vector2f toTarget = new Vector2f(targetLoc.x - missileLoc.x, targetLoc.y - missileLoc.y);
        toTarget.normalise();

        // Use dot product to determine whether the target is in front of the missile
        float dot = missileDir.x * toTarget.x + missileDir.y * toTarget.y;

        // A dot product close to 1 means the target is directly in front, and a dot product close to -1 means it is directly behind.
        // Set a threshold here to determine whether steering is needed
        return dot; // Target is ahead
    }

    public float getTurnDirection(MissileAPI missile, CombatEntityAPI target) {
        Vector2f missileLoc = missile.getLocation();
        Vector2f targetLoc = target.getLocation();

        // Calculate missile heading vector
        float facing = missile.getFacing();
        Vector2f missileDir = new Vector2f(
                (float)Math.cos(Math.toRadians(facing)),
                (float)Math.sin(Math.toRadians(facing))
        );

        // Calculate the vector pointing to the target
        Vector2f toTarget = new Vector2f(targetLoc.x - missileLoc.x, targetLoc.y - missileLoc.y);
        toTarget.normalise();

        // Use the cross product to determine the steering direction (in 2D, the z component of the cross product can determine the left and right)
        float cross = missileDir.x * toTarget.y - missileDir.y * toTarget.x;

        return -cross; // A positive number means the target is on the right, a negative number means it is on the left
    }

    @Override
    public CombatEntityAPI getTarget() {
        return target;
    }

    @Override
    public void setTarget(CombatEntityAPI target) {
        this.target = target;
    }
}
