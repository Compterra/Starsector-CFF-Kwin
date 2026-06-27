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

public class KE_EW_Starsystem {
    public void generate(SectorAPI sector) {
        // Basic galaxy setting: Element galaxy (red giant core, matching document)
        StarSystemAPI system = sector.createStarSystem("Element");
        system.getLocation().set(40000, 15000);
        system.setBackgroundTextureFilename("graphics/backgrounds/background_galatia.jpg");
        system.setLightColor(new Color(255, 50, 0)); // Red giant star light hue

        // Star: red giant, the ID is strictly set to KE_EW_Element_star as required
        PlanetAPI star = system.initStar("KE_EW_Element_star", "star_red_giant", 1100f, 650f, 12f, 0.5f, 2.7f);
        star.setCustomDescriptionId("KE_EW_Element_star_desc");
        star.setName("Element Crimson");

        // 1. Planet 1: Element-I (Headquarters, desert planet)
        PlanetAPI elementI = system.addPlanet("KE_EW_Element_I", star, "Element-I", "desert", 30, 200, 3200, 140);
        elementI.setCustomDescriptionId("KE_EW_Element_I_desc");
        elementI.setFaction(Factions.INDEPENDENT);
        Misc.initConditionMarket(elementI);
        MarketAPI elementIMarket = addMarketplace(Factions.INDEPENDENT, elementI, null, "Element-I", 6,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_6,
                        Conditions.DESERT,
                        Conditions.HOT, // hot
                        Conditions.RUINS_WIDESPREAD, // common ruins
                        Conditions.ORE_SPARSE, // sporadic mineral deposits
                        Conditions.RARE_ORE_SPARSE // Sporadic rare elements
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK,"KE_EWMarket")),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.FUELPROD, // fuel production
                        Industries.REFINING, // smelter
                        Industries.LIGHTINDUSTRY, // light industry
                        Industries.HEAVYBATTERIES, // heavy defense array
                        Industries.PATROLHQ // patrol headquarters
                )),
                0.18f, false, true
        );

        // Planet 1 Space Station: Element-I-Station (Transportation Hub + Military Base)
        SectorEntityToken elementIStation = system.addCustomEntity("KE_EW_Element_I_Station", "Element-I-Station", "station_lowtech3", Factions.INDEPENDENT);
        elementIStation.setCircularOrbitPointingDown(elementI, 90, 450, 38);
        elementIStation.setCustomDescriptionId("KE_EW_Element_I_Station_desc");
        addMarketplace(Factions.INDEPENDENT, elementIStation, null, "Element-I-Station", 5,
                new ArrayList<>(Arrays.asList(Conditions.POPULATION_5)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK,"KE_EWMarket")),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.COMMERCE, // trade center
                        Industries.STARFORTRESS, // Star Fortress-Luke
                        Industries.MILITARYBASE, // military base
                        Industries.WAYSTATION, // Interstellar Station
                        Industries.ORBITALWORKS // Rail industry
                )),
                0.2f, false, false
        );

        // [Rational arrangement of the asteroid belt 1]: Located between Element-I and Element-II, as a resource transition zone
        system.addAsteroidBelt(star, 70, 4500, 420, 75, 210, Terrain.ASTEROID_BELT, "Element Resource Belt");

        // 2. Planet II: Element-II (controlled by the Luddic Church, ice volcano planet)
        PlanetAPI elementII = system.addPlanet("KE_EW_Element_II", star, "Element-II", "cryovolcanic", 90, 180, 6000, 220);
        elementII.setCustomDescriptionId("KE_EW_Element_II_desc");
        elementII.setFaction(Factions.LUDDIC_CHURCH);
        Misc.initConditionMarket(elementII);
        MarketAPI elementIIMarket = addMarketplace(Factions.LUDDIC_CHURCH, elementII, null, "Element-II", 5,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.COLD, // cold
                        Conditions.VOLATILES_ABUNDANT, // Rich in volatiles
                        Conditions.ORE_ABUNDANT, // massive mineral deposits
                        Conditions.RARE_ORE_MODERATE // small amounts of rare elements
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MILITARYBASE, // military base
                        Industries.BATTLESTATION_MID, // Combat Base-Zhongke
                        Industries.HEAVYBATTERIES, // heavy defense array
                        Industries.MINING, // mining industry
                        Industries.WAYSTATION // Interstellar Station
                )),
                0.25f, false, true
        );

        // [Reasonable Arrangement of the Asteroid Belt 2]: Surround Element-II, matching the "asteroid dense" setting in the document
        system.addAsteroidBelt(star, 130, 7500, 500, 100, 280, Terrain.ASTEROID_BELT, "Element Asteroid Congestion Belt");

        // 3. Planet Three: Element-III (economic core, arid planet)
        PlanetAPI elementIII = system.addPlanet("KE_EW_Element_III", star, "Element-III", "arid", 150, 200, 9000, 300);
        elementIII.setCustomDescriptionId("KE_EW_Element_III_desc");
        elementIII.setFaction(Factions.INDEPENDENT);
        Misc.initConditionMarket(elementIII);
        MarketAPI elementIIIMarket = addMarketplace(Factions.INDEPENDENT, elementIII, null, "Element-III", 5,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.ARID,
                        Conditions.COLD, // cold
                        Conditions.POLLUTION, // pollute
                        Conditions.ORE_ABUNDANT, // rich mineral deposits
                        Conditions.RARE_ORE_ULTRARICH, // Abundant rare elements
                        Conditions.ORGANICS_PLENTIFUL, // rich organic matter
                        Conditions.VOLATILES_PLENTIFUL // Rich volatiles
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MINING, // mining industry
                        Industries.REFINING, // smelter
                        Industries.ORBITALWORKS, // Rail industry
                        Industries.BATTLESTATION, // Combat Base-Luke
                        Industries.PATROLHQ, // patrol headquarters
                        Industries.HEAVYBATTERIES // heavy defense array
                )),
                0.22f, false, true
        );

        // 4. Planet IV: Element-IV (Hegemony Garrison, Frozen Planet)
        PlanetAPI elementIV = system.addPlanet("KE_EW_Element_IV", star, "Element-IV", "frozen", 210, 160, 11500, 380);
        elementIV.setCustomDescriptionId("KE_EW_Element_IV_desc");
        elementIV.setFaction(Factions.HEGEMONY);
        Misc.initConditionMarket(elementIV);
        MarketAPI elementIVMarket = addMarketplace(Factions.HEGEMONY, elementIV, null, "Element-IV", 4,
                new ArrayList<>(Arrays.asList(
                        Conditions.POPULATION_4,
                        Conditions.DARK, // dark
                        Conditions.COLD, // cold
                        Conditions.RUINS_EXTENSIVE, // lots of ruins
                        Conditions.ORE_MODERATE, // small deposits
                        Conditions.VOLATILES_ABUNDANT // Rich in volatiles
                )),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_OPEN, Submarkets.GENERIC_MILITARY)),
                new ArrayList<>(Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MILITARYBASE, // military base
                        Industries.HEAVYBATTERIES, // heavy defense array
                        Industries.BATTLESTATION, // Combat Base-Luke
                        Industries.MINING, // mining industry
                        Industries.HEAVYINDUSTRY // Heavy industry
                )),
                0.28f, false, true
        );

        // Jump point: located outside Element-IV, giving the planet unusual strategic value.
        JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("KE_EW_Element_jump", "Element Jump-point");
        jumpPoint.setCircularOrbit(star, 270, 13500, 450);
        jumpPoint.setRelatedPlanet(elementIV);
        jumpPoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint);

        system.autogenerateHyperspaceJumpPoints(true, true);
        MagicCampaign.hyperspaceCleanup(system);
    }
}
