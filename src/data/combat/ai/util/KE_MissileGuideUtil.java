package data.combat.ai.util;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import org.lwjgl.util.vector.Vector2f;

public class KE_MissileGuideUtil {
    public static boolean isTargetInFront(MissileAPI missile, CombatEntityAPI target, float angleThreshold) {
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
        if(toTarget.length() <=0){
            return false;
        }
        toTarget.normalise();

        // Use dot product to determine whether the target is in front of the missile
        float dot = missileDir.x * toTarget.x + missileDir.y * toTarget.y;

        // A dot product close to 1 means the target is directly in front, and a dot product close to -1 means it is directly behind.
        // Set a threshold here to determine whether steering is needed
        return dot > angleThreshold;
    }

    public static float getTurnDirection(MissileAPI missile, CombatEntityAPI target) {
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
        if(toTarget.length() <=0){
            toTarget.set(1f,1f);
        }
        toTarget.normalise();

        // Use the cross product to determine the steering direction (in 2D, the z component of the cross product can determine the left and right)
        float cross = missileDir.x * toTarget.y - missileDir.y * toTarget.x;

        return -cross; // A positive number means the target is on the right, a negative number means it is on the left
    }

    public static float AngleDetect (MissileAPI missile, CombatEntityAPI targetship){
        Vector2f missileLoc = missile.getLocation();
        Vector2f targetLoc = targetship.getLocation();
        float CurrAngle = missile.getFacing();
        float TargetAngle = (float) Math.toDegrees(Math.atan2(targetLoc.y - missileLoc.y, targetLoc.x - missileLoc.x));
        return CurrAngle - TargetAngle;
    }
    public static float getDotProduct(MissileAPI missile, CombatEntityAPI target) {
        Vector2f missileDir = new Vector2f(
                (float) Math.cos(Math.toRadians(missile.getFacing())),
                (float) Math.sin(Math.toRadians(missile.getFacing()))
        );
        Vector2f toTarget = new Vector2f(
                target.getLocation().x - missile.getLocation().x,
                target.getLocation().y - missile.getLocation().y
        );
        toTarget.normalise();
        return missileDir.x * toTarget.x + missileDir.y * toTarget.y;
    }
}
