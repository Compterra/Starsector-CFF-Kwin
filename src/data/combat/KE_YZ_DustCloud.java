package data.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import com.fs.starfarer.api.combat.ShipAPI;
import org.lwjgl.util.vector.Vector2f;

public class KE_YZ_DustCloud implements ProximityExplosionEffect {
    private String flakWeaponID = "KE_YZ_DustFlakCannon_spread";
    @Override
    public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
        CombatEngineAPI engine = Global.getCombatEngine();
        ShipAPI source = originalProjectile.getSource();
        for (int i = 0; i < 5; i++) {
            engine.spawnProjectile(source,
                    explosion.getWeapon(),
                    flakWeaponID,
                    explosion.getLocation(),
                    (float) (Math.random()*360),
                    new Vector2f());
        }
    }
}
