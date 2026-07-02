package data.combat.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import data.combat.util.KE_GraphicsLibEffects;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class KE_YZ_SunChaserAI implements MissileAIPlugin , GuidedMissileAI{

    private final MissileAPI missile;
    private CombatEntityAPI target;
    private final ShipAPI launchingShip;
    private CombatEngineAPI engine;

    // state variables
    private float timer = 0f;
    private float visualTimer = 0f;
    private float checkInterval = 0.2f;
    private boolean launchPhase = true;
    private boolean cruiseMode = true; // Cruise mode
    private boolean attackMode = false; // attack mode

    // Performance parameters
    private final float MAX_SPEED;
    private float fuel = 40f; // Engine working time (seconds)
    private final float CRUISE_SPEED; // Cruising speed (percentage of maximum speed)
    private final float FUEL_EFFICIENCY = 0.3f; // Cruise mode fuel efficiency

    // distance threshold
    private static final float CRUISE_DISTANCE = 1500f; // Cruise distance threshold
    private static final float ATTACK_DISTANCE = 800f;  // Attack distance threshold

    // boot parameters
    private Vector2f leadPoint = new Vector2f();
    private Vector2f offset = new Vector2f();
    private float coastingTimer = 0f;

    // Velocity vector correction related
    private Vector2f lastVelocity = new Vector2f();
    private float velocityDeviationThreshold = 45f; // Velocity vector deviation threshold (degrees)
    private boolean correctingVelocity = false;
    private float velocityCorrectionTimer = 0f;

    public KE_YZ_SunChaserAI(MissileAPI missile, ShipAPI launchingShip) {
        this.missile = missile;
        this.launchingShip = launchingShip;
        this.MAX_SPEED = missile.getMaxSpeed();
        this.CRUISE_SPEED = MAX_SPEED * 0.1f;

        this.engine = Global.getCombatEngine();

        if (launchingShip != null && launchingShip.getVariant() != null && launchingShip.getVariant().getHullMods().contains("eccm")) {
            fuel += 5f;
        }
    }

    @Override
    public void advance(float amount) {
        if (engine == null) {
            engine = Global.getCombatEngine();
        }
        if (engine == null || engine.isPaused() || missile.isFading() || missile.isFizzling()) {
            return;
        }

        if (fuel < 0) {
            missile.flameOut();
            return;
        }

        timer += amount;
        visualTimer += amount;
        velocityCorrectionTimer += amount;

        updateTarget();
        addCruiseVisuals();

        if (target == null) {
            maintainCruiseSpeed();
            return;
        }
        if(target != null && launchingShip != null && target.getOwner() == launchingShip.getOwner()){
            target = AIUtils.getNearestEnemy(missile);
        }
        if (target == null) {
            maintainCruiseSpeed();
            return;
        }
        float distance = 3000;
        if (target != null) {
            distance = MathUtils.getDistance(missile, target);
        }

        // Check whether the speed vector direction needs to be corrected
        boolean needsVelocityCorrection = checkVelocityDeviation(target);

        if (needsVelocityCorrection) {
            // Prioritize speed vector correction
            correctVelocityVector(amount, target);
            correctingVelocity = true;
        } else {
            correctingVelocity = false;

            if (distance > CRUISE_DISTANCE) {
                cruiseMode = true;
                attackMode = false;
                cruiseGuidance(amount, distance);
            } else if (distance > ATTACK_DISTANCE) {
                cruiseMode = false;
                attackMode = false;
                transitionGuidance(amount, distance);
            } else {
                cruiseMode = false;
                attackMode = true;
                attackGuidance(amount, distance);
            }
        }

        dampAngularVelocity();
        lastVelocity = new Vector2f(missile.getVelocity());
        if(missile.getVelocity().length()<=5f){
            missile.giveCommand(ShipCommand.ACCELERATE);
        }
        if(Misc.getDistance(missile.getLocation(), target.getLocation())<=1000f){
            if (target != null&&AngleWithTarget(missile, target) >= 0.9961946f) {
                    missile.giveCommand(ShipCommand.ACCELERATE);
            } else if(target != null&&AngleWithTarget(missile, target) < 0.9961946f) {
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
            missile.giveCommand(ShipCommand.ACCELERATE);
        }
    }

    private void addCruiseVisuals() {
        if (visualTimer < 0.12f) return;
        visualTimer = 0f;
        if (!KE_GraphicsLibEffects.isInViewport(engine, missile, 250f)) return;

        Color color;
        float size;
        float intensity;
        if (attackMode) {
            color = new Color(255, 96, 80, 180);
            size = 115f;
            intensity = 0.65f;
        } else if (cruiseMode) {
            color = new Color(190, 45, 255, 120);
            size = 80f;
            intensity = 0.38f;
        } else {
            color = new Color(255, 150, 90, 150);
            size = 95f;
            intensity = 0.5f;
        }
        KE_GraphicsLibEffects.addProjectileLight(missile, color, size, intensity, 0.05f, 0.18f);
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

    private boolean checkVelocityDeviation(CombatEntityAPI target) {
        if (target == null) return false;

        Vector2f missileLoc = missile.getLocation();
        Vector2f targetLoc = target.getLocation();

        // Calculate the direction of the current velocity vector
        Vector2f velocity = missile.getVelocity();
        if (velocity.length() < 10f) return false; // The speed is too low and no correction is considered.

        float velocityAngle = VectorUtils.getFacing(velocity);

        // Calculate missile's direction to target
        Vector2f toTarget = Vector2f.sub(targetLoc, missileLoc, null);
        float targetAngle = VectorUtils.getFacing(toTarget);

        // Calculate the angular difference between the velocity vector and the target direction
        float angleDiff = Math.abs(MathUtils.getShortestRotation(velocityAngle, targetAngle));

        // If the speed vector direction deviates too much from the target direction, it needs to be corrected
        return angleDiff > velocityDeviationThreshold;
    }

    private void correctVelocityVector(float amount, CombatEntityAPI target) {
        // Velocity vector correction mode: slow down and realign the target
        Vector2f missileLoc = missile.getLocation();
        Vector2f targetLoc = target.getLocation();

        // Calculate missile's direction to target
        Vector2f toTarget = Vector2f.sub(targetLoc, missileLoc, null);
        float targetAngle = VectorUtils.getFacing(toTarget);

        // Calculate the angle between the missile's current heading and the target direction
        float aimAngle = MathUtils.getShortestRotation(missile.getFacing(), targetAngle);

        // Turn to target direction
        turnTowardAngle(aimAngle);

        // Calculate the direction of the current velocity vector
        Vector2f velocity = missile.getVelocity();
        float velocityAngle = VectorUtils.getFacing(velocity);
        float velocityAngleDiff = Math.abs(MathUtils.getShortestRotation(velocityAngle, targetAngle));

        // Deceleration intensity is determined based on the degree of velocity vector deviation.
        float decelerationFactor = Math.min(1f, velocityAngleDiff / 90f);

        // If the speed vector deviation is large, decelerate forcefully
        if (velocityAngleDiff > 30f) {
            missile.giveCommand(ShipCommand.DECELERATE);

            // If the speed is still very fast, continue the deceleration command
            if (missile.getVelocity().length() > CRUISE_SPEED * 0.5f) {
                // Additional deceleration logic can be added, such as multiple decelerations in a short period of time
            }
        }

        // When the velocity vector is basically aligned with the target and the speed is moderate, start accelerating
        float currentSpeed = missile.getVelocity().length();
        if (velocityAngleDiff < 15f && aimAngle < 30f) {
            if (currentSpeed < CRUISE_SPEED * 0.8f) {
                missile.giveCommand(ShipCommand.ACCELERATE);
            }
        }

        // If correction takes too long, try more aggressive deceleration
        if (velocityCorrectionTimer > 2f) {
            velocityCorrectionTimer = 0f;
            // Execute rapid deceleration
            for (int i = 0; i < 3; i++) {
                missile.giveCommand(ShipCommand.DECELERATE);
            }
        }

        // Fuel consumption (corrected mode consumes more fuel)
        fuel -= amount * 0.5f;
    }

    private void updateTarget() {
        target = launchingShip != null ? launchingShip.getShipTarget() : null;

        if (target == null) {
            target = AIUtils.getNearestEnemy(missile);
        }

        if (target == null && launchingShip != null) {
            target = AIUtils.getNearestEnemy(launchingShip);
        }

        if (target != null) {
            offset = MathUtils.getRandomPointInCircle(new Vector2f(),
                    Math.min(150f, target.getCollisionRadius() * 1.5f));
        }
    }

    private void cruiseGuidance(float amount, float distance) {
        fuel -= amount * FUEL_EFFICIENCY;

        if (launchPhase || timer >= 0.15f) {
            launchPhase = false;
            timer = 0f;

            Vector2f targetLoc = target.getLocation();

            // Check speed vector direction
            Vector2f velocity = missile.getVelocity();
            float velocityAngle = VectorUtils.getFacing(velocity);
            Vector2f toTarget = Vector2f.sub(targetLoc, missile.getLocation(), null);
            float targetAngle = VectorUtils.getFacing(toTarget);
            float velocityToTargetAngleDiff = Math.abs(MathUtils.getShortestRotation(velocityAngle, targetAngle));

            // If the direction of the speed vector is basically consistent with the target direction, use the prediction point
            if (velocityToTargetAngleDiff < 30f) {
                // Calculate simple guide points
                float leadDistance = Math.min(400f, distance * 0.2f);
                Vector2f targetDir = new Vector2f(toTarget);
                targetDir.normalise();
                leadPoint = Vector2f.add(missile.getLocation(),
                        new Vector2f(targetDir.x * leadDistance, targetDir.y * leadDistance), null);
            } else {
                // The direction deviation of the velocity vector is large and points directly to the target.
                leadPoint = targetLoc;
            }
        }

        // Calculate steering angle
        float leadAngle = VectorUtils.getAngle(missile.getLocation(), leadPoint);
        float aimAngle = MathUtils.getShortestRotation(missile.getFacing(), leadAngle);

        // turn to target
        turnTowardAngle(aimAngle);

        // Check current velocity vector direction
        Vector2f velocity = missile.getVelocity();
        float velocityAngle = VectorUtils.getFacing(velocity);
        float velocityToLeadAngleDiff = Math.abs(MathUtils.getShortestRotation(velocityAngle, leadAngle));

        // speed control logic
        float currentSpeed = missile.getVelocity().length();
        float desiredSpeed = CRUISE_SPEED;

        // If the speed vector direction deviation is large, slow down to help correct
        if (velocityToLeadAngleDiff > 30f) {
            desiredSpeed = CRUISE_SPEED * 0.5f;
            missile.giveCommand(ShipCommand.DECELERATE);
        }
        // If the missile is basically aligned in the direction and the direction of the velocity vector is also basically aligned, it can accelerate
        else if (Math.abs(aimAngle) < 30f && velocityToLeadAngleDiff < 20f) {
            desiredSpeed = CRUISE_SPEED * 1.2f;
        }

        // Application speed control
        if (currentSpeed > desiredSpeed * 1.1f) {
            missile.giveCommand(ShipCommand.DECELERATE);
        } else if (currentSpeed < desiredSpeed * 0.9f && Math.abs(aimAngle) < 90f) {
            missile.giveCommand(ShipCommand.ACCELERATE);
        }

        // If the speed is too low, force acceleration
        if (currentSpeed < 50f && Math.abs(aimAngle) < 120f) {
            missile.giveCommand(ShipCommand.ACCELERATE);
        }
    }

    private void transitionGuidance(float amount, float distance) {
        fuel -= amount * 0.75f;

        if (timer >= 0.1f) {
            timer = 0f;

            // Use predictive interception
            leadPoint = AIUtils.getBestInterceptPoint(
                    missile.getLocation(),
                    MAX_SPEED * 0.8f,
                    target.getLocation(),
                    target.getVelocity()
            );

            if (leadPoint == null) {
                leadPoint = target.getLocation();
            }

            float offsetScale = (distance - ATTACK_DISTANCE) / (CRUISE_DISTANCE - ATTACK_DISTANCE);
            offsetScale = Math.max(0.1f, offsetScale);
            leadPoint = new Vector2f(
                    leadPoint.x + offset.x * offsetScale,
                    leadPoint.y + offset.y * offsetScale
            );
        }

        // Check speed vector direction
        Vector2f velocity = missile.getVelocity();
        float velocityAngle = VectorUtils.getFacing(velocity);
        float leadAngle = VectorUtils.getAngle(missile.getLocation(), leadPoint);
        float velocityToLeadAngleDiff = Math.abs(MathUtils.getShortestRotation(velocityAngle, leadAngle));

        float aimAngle = MathUtils.getShortestRotation(missile.getFacing(), leadAngle);
        turnTowardAngle(aimAngle);

        // If the speed vector direction deviation is large, deceleration correction
        if (velocityToLeadAngleDiff > 45f) {
            missile.giveCommand(ShipCommand.DECELERATE);
        } else {
            // Normal acceleration logic
            float lerpFactor = (CRUISE_DISTANCE - distance) / (CRUISE_DISTANCE - ATTACK_DISTANCE);
            lerpFactor = Math.max(0, Math.min(1, lerpFactor));
            float targetSpeed = CRUISE_SPEED + (MAX_SPEED * 0.8f - CRUISE_SPEED) * lerpFactor;

            if (missile.getVelocity().length() < targetSpeed && Math.abs(aimAngle) < 120f) {
                missile.giveCommand(ShipCommand.ACCELERATE);
            }
            // Add trigger conditions for acceleration command
            else if (missile.getVelocity().length() < CRUISE_SPEED * 1.1f && Math.abs(aimAngle) < 90f) {
                missile.giveCommand(ShipCommand.ACCELERATE);
            }
        }
    }


    private void attackGuidance(float amount, float distance) {
        fuel -= amount;

        if (timer >= 0.05f) {
            timer = 0f;

            leadPoint = AIUtils.getBestInterceptPoint(
                    missile.getLocation(),
                    MAX_SPEED,
                    target.getLocation(),
                    target.getVelocity()
            );

            if (leadPoint == null) {
                leadPoint = target.getLocation();
            }
        }

        float targetAngle = VectorUtils.getAngle(missile.getLocation(), leadPoint);
        float aimAngle = MathUtils.getShortestRotation(missile.getFacing(), targetAngle);

        // Check speed vector direction
        Vector2f velocity = missile.getVelocity();
        float velocityAngle = VectorUtils.getFacing(velocity);
        float velocityToTargetAngleDiff = Math.abs(MathUtils.getShortestRotation(velocityAngle, targetAngle));

        turnTowardAngle(aimAngle);

        // Attack mode: Accelerate with full force, but if the direction of the speed vector deviates greatly, correct it first
        if (velocityToTargetAngleDiff > 60f) {
            // Slow down during sharp turns to improve maneuverability
            missile.giveCommand(ShipCommand.DECELERATE);
        } else if (Math.abs(aimAngle) < 90f) {
            missile.giveCommand(ShipCommand.ACCELERATE);
        }

//        if (missile.getVelocity().length() > 300f && Math.abs(aimAngle) > 60f) {
//            missile.giveCommand(ShipCommand.DECELERATE);
//        }
    }

    private void maintainCruiseSpeed() {
        float currentSpeed = missile.getVelocity().length();

        if (currentSpeed > CRUISE_SPEED * 1.2f) {
            missile.giveCommand(ShipCommand.DECELERATE);
        } else if (currentSpeed < CRUISE_SPEED * 0.8f) {
            missile.giveCommand(ShipCommand.ACCELERATE);
        }
    }

    private void turnTowardAngle(float aimAngle) {
        if (aimAngle < 0) {
            missile.giveCommand(ShipCommand.TURN_RIGHT);
        } else {
            missile.giveCommand(ShipCommand.TURN_LEFT);
        }
    }

    private void dampAngularVelocity() {
        float angularDamping = 0.1f;
        float currentAngularVelocity = missile.getAngularVelocity();

        if (attackMode) {
            angularDamping = 0.05f;
        }

        if (Math.abs(currentAngularVelocity) > 150f) {
            missile.setAngularVelocity(currentAngularVelocity * (1f - angularDamping));
        }
    }

    @Override
    public CombatEntityAPI getTarget() {
        return target;
    }

    @Override
    public void setTarget(CombatEntityAPI target) {
        this.target = target;
    }

    public void init(CombatEngineAPI engine) {
        this.engine = engine;
    }
}