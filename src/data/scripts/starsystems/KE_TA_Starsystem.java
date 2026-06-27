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

public class KE_TA_Starsystem {
    public void generate(SectorAPI sector) {
        // Basic galaxy setting: Skyline galaxy (yellow dwarf core, matching document)
        StarSystemAPI system = sector.createStarSystem("Skyline");
        system.getLocation().set(20000, 5000);
        system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
        system.setLightColor(new Color(255, 255, 200)); // yellow dwarf star light hue

        // Star: yellow dwarf, the ID is strictly set to KE_TA_Skyline_star as required
        PlanetAPI star = system.initStar("KE_TA_Skyline_star", "star_yellow", 900f, 550f, 11f, 0.8f, 3f);
        star.setCustomDescriptionId("KE_TA_Skyline_star_desc");
        star.setName("Skyline Core");

        // 1. Planet One: Skyline-I (test/competition venue, tundra planet)
        PlanetAPI skylineI = system.addPlanet("KE_TA_Skyline_I", star, "Skyline-I", "tundra", 35, 160, 2800, 120);
        skylineI.setCustomDescriptionId("KE_TA_Skyline_I_desc");
        skylineI.setFaction(Factions.INDEPENDENT);
        Misc.initConditionMarket(skylineI);
        MarketAPI skylineIMarket = addMarketplace(Factions.INDEPENDENT, skylineI, null, "Skyline-I", 5,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.TECTONIC_ACTIVITY, // tectonic activity
                        Conditions.LOW_GRAVITY, // low gravity
                        Conditions.HABITABLE, // livable
                        Conditions.METEOR_IMPACTS, // Meteor impact
                        Conditions.ORE_RICH, // rich mineral deposits
                        Conditions.VOLATILES_DIFFUSE // small amounts of volatiles
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MINING, // mining industry
                        Industries.PATROLHQ, // patrol headquarters
                        Industries.HEAVYINDUSTRY, // Heavy industry
                        Industries.REFINING // smelter
                )),
                0.22f, false, true
        );

        // [Reasonable arrangement of the asteroid belt 1]: Located between Skyline-I and Skyline-II, serving as a natural waterway separation
        system.addAsteroidBelt(star, 70, 3800, 380, 60, 180, Terrain.ASTEROID_BELT, "Skyline Test Range Belt");

        // 2. Planet II: Skyline-II (Tri-Tachyon control, arid planet)
        PlanetAPI skylineII = system.addPlanet("KE_TA_Skyline_II", star, "Skyline-II", "arid", 90, 150, 4500, 180);
        skylineII.setCustomDescriptionId("KE_TA_Skyline_II_desc");
        skylineII.setFaction(Factions.TRITACHYON);
        Misc.initConditionMarket(skylineII);
        MarketAPI skylineIIMarket = addMarketplace(Factions.TRITACHYON, skylineII, null, "Skyline-II", 4,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_4,
                        Conditions.ARID,
                        Conditions.FARMLAND_POOR, // barren farmland
                        Conditions.ORGANICS_ABUNDANT, // rich in organic matter
                        Conditions.LOW_GRAVITY, // low gravity
                        Conditions.RUINS_WIDESPREAD, // common ruins
                        Conditions.HABITABLE // livable
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.WAYSTATION, // Interstellar Station
                        Industries.MINING, // mining industry
                        Industries.ORBITALSTATION_MID // Orbital Station-Hi-Tech
                )),
                0.25f, false, true
        );

        // 3. Planet Three: Skyline-III (Headquarters location, ocean planet)
        PlanetAPI skylineIII = system.addPlanet("KE_TA_Skyline_III", star, "Skyline-III", "water", 150, 220, 6200, 240);
        skylineIII.setCustomDescriptionId("KE_TA_Skyline_III_desc");
        skylineIII.setFaction(Factions.INDEPENDENT);
        Misc.initConditionMarket(skylineIII);
        MarketAPI skylineIIIMarket = addMarketplace(Factions.INDEPENDENT, skylineIII, null, "Skyline-III", 6,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_6,
                        Conditions.WATER,
                        Conditions.MILD_CLIMATE, // mild climate
                        Conditions.WATER_SURFACE, // ocean coverage
                        Conditions.RARE_ORE_RICH, // Rich in rare elements
                        Conditions.ORE_ULTRARICH, // rich mineral deposits
                        Conditions.ORGANICS_PLENTIFUL, // rich organic matter
                        Conditions.HABITABLE // livable
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK,"KE_TAMarket")),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT, // Very large port
                        Industries.HEAVYBATTERIES, // heavy defense array
                        Industries.AQUACULTURE, // aquaculture
                        Industries.MINING, // mining industry
                        Industries.LIGHTINDUSTRY // light industry
                )),
                0.18f, false, true
        );

        // Planet III Space Station: Skyline-III-Station (core transportation hub)
        SectorEntityToken skylineIIIStation = system.addCustomEntity("KE_TA_Skyline_III_Station", "Skyline-III-Station", "station_hightech3", Factions.INDEPENDENT);
        skylineIIIStation.setCircularOrbitPointingDown(skylineIII, 120, 550, 40);
        skylineIIIStation.setCustomDescriptionId("KE_TA_Skyline_III_Station_desc");
        addMarketplace(Factions.INDEPENDENT, skylineIIIStation, null, "Skyline-III-Station", 5,
                new ArrayList<>(Arrays.asList(Conditions.POPULATION_5)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK,"KE_TAMarket")),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.WAYSTATION, // Interstellar Station
                        Industries.ORBITALWORKS, // Rail industry
                        Industries.COMMERCE, // trade center
                        Industries.FUELPROD, // fuel production
                        Industries.STARFORTRESS_HIGH // Star Fortress-High Tech
                )),
                0.2f, false, false
        );

        // [Proper Arrangement of the Asteroid Belt 2]: Located outside Skyline-III, it serves as the outer barrier of the galaxy.
        system.addAsteroidBelt(star, 210, 7800, 500, 90, 250, Terrain.ASTEROID_BELT, "Skyline Outer Belt");

        // Jump point: Located at the outermost edge of the galaxy, associated with Skyline-III
        JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("KE_TA_Skyline_jump", "Skyline Jump-point");
        jumpPoint.setCircularOrbit(star, 270, 9500, 400);
        jumpPoint.setRelatedPlanet(skylineIII);
        jumpPoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint);

        system.autogenerateHyperspaceJumpPoints(true, true);
        MagicCampaign.hyperspaceCleanup(system);
    }
}