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

public class KE_UE_Starsystem {
    public void generate(SectorAPI sector) {
        // Galaxy basic setting: Delicacy system (orange dwarf core, matching document)
        StarSystemAPI system = sector.createStarSystem("Delicacy");
        system.getLocation().set(10000, 0);
        system.setBackgroundTextureFilename("graphics/backgrounds/background_galatia.jpg");
        system.setLightColor(new Color(255, 200, 160)); // orange dwarf star light hue

        // Star: orange dwarf, the ID is strictly set to KE_UE_Dlicacy_star as required
        PlanetAPI star = system.initStar("KE_UE_Dlicacy_star", "star_orange", 850f, 520f, 10f, 0.7f, 2.8f);
        star.setCustomDescriptionId("KE_UE_Dlicacy_star_desc");
        star.setName("Delicacy Prime");

        // 1. Planet No. 1: Delicacy-I (relict planet, desolate planet)
        PlanetAPI dlicacyI = system.addPlanet("KE_UE_Dlicacy_I", star, "Delicacy-I", "barren", 30, 180, 2200, 100);
        dlicacyI.setCustomDescriptionId("KE_UE_Dlicacy_I_desc");
        dlicacyI.setFaction(Factions.INDEPENDENT);
        Misc.initConditionMarket(dlicacyI);
        MarketAPI dlicacyIMarket = addMarketplace(Factions.INDEPENDENT, dlicacyI, null, "Delicacy-I", 3,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_3,
                        Conditions.HOT,
                        Conditions.RUINS_VAST, // huge ruins
                        Conditions.ORE_MODERATE, // small deposits
                        Conditions.RARE_ORE_SPARSE // sporadic rare elements
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.TECHMINING, // Technology mining
                        Industries.PATROLHQ
                )),
                0.25f, false, true
        );

        // 2. Planet II: Delicacy-II (Headquarters location, Earth-like planet)
        PlanetAPI dlicacyII = system.addPlanet("KE_UE_Dlicacy_II", star, "Delicacy-II", "terran", 60, 240, 3500, 140);
        dlicacyII.setCustomDescriptionId("KE_UE_Dlicacy_II_desc");
        dlicacyII.setFaction(Factions.INDEPENDENT);
        Misc.initConditionMarket(dlicacyII);
        MarketAPI dlicacyIIMarket = addMarketplace(Factions.INDEPENDENT, dlicacyII, null, "Delicacy-II", 6,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_6,
                        Conditions.TERRAN,
                        Conditions.FARMLAND_BOUNTIFUL, // fertile farmland
                        Conditions.SOLAR_ARRAY, // Orbital lighting array
                        Conditions.MILD_CLIMATE, // mild climate
                        Conditions.HABITABLE // livable
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK,"KE_UEMarket")),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.FARMING, // agriculture
                        Industries.COMMERCE, // trade center
                        Industries.MILITARYBASE // military base
                )),
                0.15f, false, true
        );

        // Planet II space station: Delicacy-II Station (giant ecological space station)
        SectorEntityToken dlicacyIIStation = system.addCustomEntity("KE_UE_Dlicacy_II_Station", "Delicacy-II Station", "station_hightech3", Factions.INDEPENDENT);
        dlicacyIIStation.setCircularOrbitPointingDown(dlicacyII, 90, 400, 35);
        dlicacyIIStation.setCustomDescriptionId("KE_UE_Dlicacy_II_Station_desc");
        addMarketplace(Factions.INDEPENDENT, dlicacyIIStation, null, "Delicacy-II Station", 5,
                new ArrayList<>(Arrays.asList(Conditions.POPULATION_5)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK,"KE_UEMarket")),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.ORBITALWORKS, // Rail industry
                        Industries.STARFORTRESS_HIGH, // Star Fortress-High Tech
                        Industries.MILITARYBASE, // military base
                        Industries.HEAVYBATTERIES, // heavy defense array
                        Industries.WAYSTATION // Interstellar Station
                )),
                0.2f, false, false
        );

        // 3. Planet Three: Delicacy-III (Hegemony Garrison, Desert Planet)
        PlanetAPI dlicacyIII = system.addPlanet("KE_UE_Dlicacy_III", star, "Delicacy-III", "desert", 120, 200, 5000, 200);
        dlicacyIII.setCustomDescriptionId("KE_UE_Dlicacy_III_desc");
        dlicacyIII.setFaction(Factions.HEGEMONY);
        Misc.initConditionMarket(dlicacyIII);
        MarketAPI dlicacyIIIMarket = addMarketplace(Factions.HEGEMONY, dlicacyIII, null, "Delicacy-III", 5,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.DESERT,
                        Conditions.EXTREME_WEATHER, // Extreme climate
                        Conditions.ORE_ABUNDANT, // massive mineral deposits
                        Conditions.RARE_ORE_MODERATE // small amounts of rare elements
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.GENERIC_MILITARY)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.HEAVYBATTERIES, // heavy defense array
                        Industries.MILITARYBASE, // military base
                        Industries.STARFORTRESS, // Star Fortress-Luke
                        Industries.WAYSTATION // Interstellar Station
                )),
                0.2f, false, true
        );

        // [Proper arrangement of the asteroid belt]: Located between Delicacy-III and Delicacy-IV to avoid orbital overlap
        system.addAsteroidBelt(star, 150, 6200, 450, 80, 220, Terrain.ASTEROID_BELT, "Delicacy Frontier Belt");

        // 4. Planet IV: Delicacy-IV (pirate-controlled, desolate planet)
        PlanetAPI dlicacyIV = system.addPlanet("KE_UE_Dlicacy_IV", star, "Delicacy-IV", "barren", 180, 180, 7500, 280);
        dlicacyIV.setCustomDescriptionId("KE_UE_Dlicacy_IV_desc");
        dlicacyIV.setFaction(Factions.PIRATES);
        Misc.initConditionMarket(dlicacyIV);
        MarketAPI dlicacyIVMarket = addMarketplace(Factions.PIRATES, dlicacyIV, null, "Delicacy-IV", 3,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_3,
                        Conditions.THIN_ATMOSPHERE, // thin atmosphere
                        Conditions.RUINS_EXTENSIVE, // lots of ruins
                        Conditions.POLLUTION, // pollute
                        Conditions.ORE_MODERATE, // small deposits
                        Conditions.RARE_ORE_SPARSE, // sporadic rare elements
                        Conditions.ORGANICS_ABUNDANT // rich in organic matter
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.PATROLHQ, // patrol headquarters
                        Industries.MINING // mining industry
                )),
                0.3f, true, false
        );

        // Jump point: Located outside the galaxy, associated with Delicacy-III
        JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("KE_UE_Dlicacy_jump", "Delicacy Jump-point");
        jumpPoint.setCircularOrbit(star, 240, 9000, 350);
        jumpPoint.setRelatedPlanet(dlicacyIII);
        jumpPoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint);

        system.autogenerateHyperspaceJumpPoints(true, true);
        MagicCampaign.hyperspaceCleanup(system);
    }
}