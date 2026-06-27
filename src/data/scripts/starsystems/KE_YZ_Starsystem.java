package data.scripts.starsystems;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.util.MagicCampaign;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import static data.scripts.KE_NormalGenerate.addMarketplace;

public class KE_YZ_Starsystem {
    public void generate(SectorAPI sector) {
        // Basic galaxy setting: YunTing galaxy (blue giant core, matching document)
        StarSystemAPI system = sector.createStarSystem("YunTing");
        system.getLocation().set(32000, -12000);
        system.setBackgroundTextureFilename("graphics/backgrounds/background2.jpg");
        system.setLightColor(new Color(127, 190, 255)); // blue giant light hue

        // Star: blue giant, the ID is set to KE_YZ_YunTing_star strictly according to the requirements
        PlanetAPI star = system.initStar("KE_YZ_YunTing_star", "star_blue_giant", 1200f, 600f, 12f, 0.6f, 2.5f);
        star.setCustomDescriptionId("KE_YZ_YunTing_star_desc");
        star.setName("YunTing Azure");

        // 1. Planet 1: YunTing-I (weapons testing site, desert planet)
        PlanetAPI yunTingI = system.addPlanet("KE_YZ_YunTing_I", star, "YunTing-I", "desert", 40, 150, 3000, 130);
        yunTingI.setCustomDescriptionId("KE_YZ_YunTing_I_desc");
        yunTingI.setFaction(Factions.INDEPENDENT);
        Misc.initConditionMarket(yunTingI);
        MarketAPI yunTingIMarket = addMarketplace(Factions.INDEPENDENT, yunTingI, null, "YunTing-I", 3,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_3,
                        Conditions.DESERT,
                        Conditions.ORE_MODERATE, // small deposits
                        Conditions.RARE_ORE_ULTRARICH // Abundant rare elements
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MINING, // mining industry
                        Industries.GROUNDDEFENSES, // Ground defense facilities
                        Industries.PATROLHQ // patrol headquarters
                )),
                0.28f, false, true
        );

        // [Rational Arrangement of the Asteroid Belt 1]: Located between YunTing-I and YunTing-II, the remains of the simulated weapons test range
        system.addAsteroidBelt(star, 80, 4200, 400, 70, 200, Terrain.ASTEROID_BELT, "YunTing Test Ruins Belt");

        // 2. Planet II: YunTing-II (Hegemony Garrison, Jungle Planet)
        PlanetAPI yunTingII = system.addPlanet("KE_YZ_YunTing_II", star, "YunTing-II", "jungle", 100, 200, 5500, 200);
        yunTingII.setCustomDescriptionId("KE_YZ_YunTing_II_desc");
        yunTingII.setFaction(Factions.HEGEMONY);
        Misc.initConditionMarket(yunTingII);
        MarketAPI yunTingIIMarket = addMarketplace(Factions.HEGEMONY, yunTingII, null, "YunTing-II", 5,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.JUNGLE,
                        Conditions.ORGANICS_ABUNDANT, // rich in organic matter
                        Conditions.FARMLAND_BOUNTIFUL, // fertile farmland
                        Conditions.ORE_MODERATE, // small deposits
                        Conditions.MILD_CLIMATE, // mild climate
                        Conditions.HABITABLE, // livable
                        Conditions.LOW_GRAVITY, // low gravity
                        Conditions.INIMICAL_BIOSPHERE // hostile biosphere
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.GENERIC_MILITARY)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MILITARYBASE, // military base
                        Industries.FARMING, // agriculture
                        Industries.BATTLESTATION, // Combat Base-Luke
                        Industries.MINING, // mining industry
                        Industries.GROUNDDEFENSES // Ground defense facilities
                )),
                0.2f, false, true
        );

        // 3. Planet Three: YunTing-III (pirate-controlled, desolate-bombarded planet)
        PlanetAPI yunTingIII = system.addPlanet("KE_YZ_YunTing_III", star, "YunTing-III", "barren-bombarded", 160, 180, 7200, 260);
        yunTingIII.setCustomDescriptionId("KE_YZ_YunTing_III_desc");
        yunTingIII.setFaction(Factions.PIRATES);
        Misc.initConditionMarket(yunTingIII);
        MarketAPI yunTingIIIMarket = addMarketplace(Factions.PIRATES, yunTingIII, null, "YunTing-III", 4,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_4,
                        Conditions.METEOR_IMPACTS, // Meteor impact
                        Conditions.POLLUTION, // pollute
                        Conditions.ORE_ABUNDANT, // massive mineral deposits
                        Conditions.VOLATILES_ABUNDANT // Rich in volatiles
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.PATROLHQ, // patrol headquarters
                        Industries.ORBITALSTATION_MID, // Orbital Station-Zhongke
                        Industries.GROUNDDEFENSES, // Ground defense facilities
                        Industries.MINING, // mining industry
                        Industries.HEAVYINDUSTRY // Heavy industry
                )),
                0.3f, true, false
        );

        // [Properly Arrange the Asteroid Belt 2]: Surround YunTing-III and strengthen the pirate haven setting
        system.addAsteroidBelt(star, 200, 8500, 480, 85, 230, Terrain.ASTEROID_BELT, "YunTing Pirate Haven Belt");

        // 4. Planet No. 4: YunTing-IV (headquarters location, gas giant planet)
        PlanetAPI yunTingIV = system.addPlanet("KE_YZ_YunTing_IV", star, "YunTing-IV", "gas_giant", 240, 300, 10000, 350);
        yunTingIV.setCustomDescriptionId("KE_YZ_YunTing_IV_desc");
        yunTingIV.setFaction(Factions.INDEPENDENT);
        Misc.initConditionMarket(yunTingIV);
        MarketAPI yunTingIVMarket = addMarketplace(Factions.INDEPENDENT, yunTingIV, null, "YunTing-IV", 5,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.EXTREME_WEATHER, // Extreme climate
                        Conditions.HIGH_GRAVITY, // high gravity
                        Conditions.VOLATILES_PLENTIFUL // Rich volatiles
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK,"KE_YZMarket")),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MINING, // mining industry
                        Industries.FUELPROD, // fuel production
                        Industries.ORBITALWORKS // Rail industrial facilities
                )),
                0.22f, false, true
        );

        // Planet IV Station: YunTing-IV-Station (Population Resettlement + Mining Hub)
        SectorEntityToken yunTingIVStation = system.addCustomEntity("KE_YZ_YunTing_IV_Station", "YunTing-IV-Station", "station_midline3", Factions.INDEPENDENT);
        yunTingIVStation.setCircularOrbitPointingDown(yunTingIV, 90, 600, 45);
        yunTingIVStation.setCustomDescriptionId("KE_YZ_YunTing_IV_Station_desc");
        addMarketplace(Factions.INDEPENDENT, yunTingIVStation, null, "YunTing-IV-Station", 6,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_6,
                        Conditions.VOLATILES_PLENTIFUL // Rich volatiles
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK,"KE_YZMarket")),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MINING, // mining industry
                        Industries.FUELPROD, // fuel production
                        Industries.MILITARYBASE, // military base
                        Industries.STARFORTRESS_MID, // Star Fortress-Zhongke
                        Industries.WAYSTATION // Interstellar Station
                )),
                0.25f, false, false
        );

        // Jump point: Located outside YunTing-IV, associated with the planet
        JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("KE_YZ_YunTing_jump", "YunTing Jump-point");
        jumpPoint.setCircularOrbit(star, 300, 12000, 450);
        jumpPoint.setRelatedPlanet(yunTingIV);
        jumpPoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint);

        system.autogenerateHyperspaceJumpPoints(true, true);
        MagicCampaign.hyperspaceCleanup(system);
    }
}