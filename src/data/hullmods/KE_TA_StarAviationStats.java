package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicRender;

import java.awt.*;

public class KE_TA_StarAviationStats extends BaseHullMod {
    private static final float REFIT_TIME_MULT = 0.8f;
    private static final float FIGHTER_RANGE_MULT = 1.5f;
    private static final float DEPLOYMENT_MULT = 1.5f;
    private static final String MOONLIGHT_HULL_ID = "KE_TA_Moonlight";
    private static final String MOONLIGHT_LIGHTS_SPRITE = "graphics/ships/KE_TA_Moonlight/KE_TA_Moonlight_lights.png";
    private static final Color MOONLIGHT_GLOW_COLOR = new Color(190, 245, 255, 255);

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFighterRefitTimeMult().modifyMult(id, REFIT_TIME_MULT);
        stats.getFighterWingRange().modifyMult(id, FIGHTER_RANGE_MULT);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        applyExtraDeployment(ship);
        renderMoonlightGlow(ship);
    }

    private void renderMoonlightGlow(ShipAPI ship) {
        if (ship == null || ship.getHullSpec() == null || !MOONLIGHT_HULL_ID.equals(ship.getHullSpec().getHullId())) {
            return;
        }
        if (ship.isHulk() || !ship.isAlive()) {
            return;
        }

        float alpha = 0.24f;
        if (ship.getFluxTracker() != null) {
            alpha += 0.18f * ship.getFluxTracker().getFluxLevel();
        }
        if (ship.getSystem() != null) {
            alpha += 0.28f * ship.getSystem().getEffectLevel();
        }
        if (ship.getEngineController() != null && ship.getEngineController().isFlamedOut()) {
            alpha *= 0.25f;
        }
        alpha *= ship.getExtraAlphaMult();
        alpha = Math.max(0f, Math.min(0.75f, alpha));
        if (alpha <= 0.02f) {
            return;
        }

        SpriteAPI sprite = Global.getSettings().getSprite(MOONLIGHT_LIGHTS_SPRITE);
        Color color = Misc.scaleAlpha(MOONLIGHT_GLOW_COLOR, alpha);
        MagicRender.objectspace(
                sprite,
                ship,
                new Vector2f(),
                new Vector2f(),
                new Vector2f(sprite.getWidth(), sprite.getHeight()),
                new Vector2f(),
                -180f,
                0f,
                true,
                color,
                true,
                0f,
                0f,
                0.04f,
                1f,
                0.016f,
                0f,
                0.05f,
                0.08f,
                true,
                CombatEngineLayers.ABOVE_SHIPS_LAYER
        );
    }
    private void applyExtraDeployment(ShipAPI ship) {
        String id = ship.getId() + "_KE_TA_StarAviationStats";
        if (ship.getOriginalOwner() == -1) {
            return;
        }

        boolean allDeployed = true;
        boolean ranOnce = false;
        for (FighterLaunchBayAPI bay : ship.getLaunchBaysCopy()) {
            if (bay.getWing() == null) {
                continue;
            }

            ranOnce = true;
            FighterWingSpecAPI wingSpec = bay.getWing().getSpec();
            int deployed = bay.getWing().getWingMembers().size();
            int maxTotal = (int) (wingSpec.getNumFighters() * DEPLOYMENT_MULT);
            int actualAdd = maxTotal - deployed;

            if (actualAdd > 0) {
                bay.setExtraDeployments(actualAdd);
                bay.setExtraDeploymentLimit(maxTotal);
                bay.setExtraDuration(9999999);
                allDeployed = false;
            } else {
                bay.setExtraDeployments(0);
                bay.setExtraDeploymentLimit(0);
                bay.setFastReplacements(0);
            }

            if (ship.getMutableStats().getFighterRefitTimeMult().getPercentStatMod(id) == null && actualAdd != 0) {
                bay.setFastReplacements(actualAdd);
            }
        }

        if (ship.getMutableStats().getFighterRefitTimeMult().getPercentStatMod(id) == null && allDeployed && ranOnce) {
            ship.getMutableStats().getFighterRefitTimeMult().modifyPercent(id, 1);
        }
    }

    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return false;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;
        Color f = new Color(248, 92, 23, 255);
        Color good = Misc.getPositiveHighlightColor();
        tooltip.addPara("The Moonlight-class carrier's aviation dispatch suite is the core of its combat power.\n" +
                        "Quantum tactical links connect the ship to each fighter, while an internal intelligence core coordinates wings beyond conventional scale. The blueprint may be widely traded, but few crews can make the system sing.",
                pad, f, "");
        tooltip.addSectionHeading("Effect", Alignment.MID, pad);
        tooltip.addPara("Fighter replacement time reduced by %s.", pad, good, "20%");
        tooltip.addPara("Fighter deployment capacity increased by %s.", 3f, good, "50%");
        tooltip.addPara("Fighter combat distance increased by %s.", 3f, good, "50%");
    }
}
