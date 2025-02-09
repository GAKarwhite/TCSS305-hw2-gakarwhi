/*
 * TCSS 305 - Road Rage
 */

package edu.uw.tcss.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.uw.tcss.model.Direction;
import edu.uw.tcss.model.Light;
import edu.uw.tcss.model.Taxi;
import edu.uw.tcss.model.Terrain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class Taxi.
 *
 * @author Georgia Karwhite
 * @version 2025 February 09
 */
public class TaxiTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    private static final int FIXTURE_X = 10;

    private static final int FIXTURE_Y = 10;

    private static final int FIXTURE_DEATH_TIME = 15;

    private static final List<TerrainTest> MY_TERRAIN_TESTS = initializeTerrain();

    /**
     * The amount of time a taxi waits at a crosswalk
     */
    private static final int TAXI_WAIT_TIME_CROSSWALK = 3;

    /** Test method for Taxi constructor. */
    @Test
    public void testTaxiConstructor() {
        final Taxi h = new Taxi(FIXTURE_X, FIXTURE_Y, Direction.NORTH);

        assertEquals(FIXTURE_X, h.getX(),
                "Taxi x coordinate not initialized correctly!");
        assertEquals(FIXTURE_Y, h.getY(),
                "Taxi y coordinate not initialized correctly!");
        assertEquals(Direction.NORTH, h.getDirection(),
                "Taxi direction not initialized correctly!");
        assertEquals(FIXTURE_DEATH_TIME, h.getDeathTime(),
                "Taxi death time not initialized correctly!");
        assertTrue(h.isAlive(),
                "Taxi isAlive() fails initially!");
    }

    /** Test method for Taxi setters. */
    @Test
    public void testTaxiSetters() {
        final Taxi h = new Taxi(FIXTURE_X, FIXTURE_Y, Direction.NORTH);

        final int changedX = 12;
        final int changedY = 13;

        h.setX(changedX);
        assertEquals(changedX, h.getX(),
                "Taxi setX failed!");

        h.setY(changedY);
        assertEquals(changedY, h.getY(),
                "Taxi setY failed!");

        h.setDirection(Direction.SOUTH);
        assertEquals(Direction.SOUTH, h.getDirection(),
                "Taxi setDirection failed!");
    }

    /**
     * Test method for {@link Taxi#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {
        final Taxi taxi = new Taxi(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Direction dir : Direction.values()) {
            taxi.setDirection(dir);
            loopTerrainsAndLights(taxi);
        }
    }

    /**
     * Test for chooseDirection.
     */
    @Test
    public void testChooseDirection() {
        final Taxi taxi = new Taxi(0, 0, Direction.NORTH);
        for (final TerrainTest test : MY_TERRAIN_TESTS) {
            assertEquals(test.myAnswer, taxi.chooseDirection(test.myNeighbors),
                    "Taxi chooseDirection selected incorrectly");
        }
    }

    /**
     * Tests the time a taxi waits at a red crosswalk.
     */
    @Test
    public void testWaitAtCrosswalk() {
        final Taxi taxi = new Taxi(0, 0, Direction.NORTH);
        //test early release for taxi
        assertFalse(taxi.canPass(Terrain.CROSSWALK, Light.RED));
            //establish the crosswalk is red
        assertTrue(taxi.canPass(Terrain.CROSSWALK, Light.GREEN));
            //the taxi should move if the light turns green

        //test full wait for taxi
        for (int i = 0; i < TAXI_WAIT_TIME_CROSSWALK; i++) {
            assertFalse(taxi.canPass(Terrain.CROSSWALK, Light.RED));
        }
        assertTrue(taxi.canPass(Terrain.CROSSWALK, Light.RED));
    }



    /*  Loops through the terrains and lights  */
    private void loopTerrainsAndLights(final Taxi theTaxi) {
        for (final Terrain destinationTerrain : Terrain.values()) {
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                checkValidity(destinationTerrain, currentLightCondition, theTaxi);
            }
        }
    }


    private void checkValidity(final Terrain theDestinationTerrain,
                               final Light theCurrentLightCondition,
                               final Taxi theTaxi) {
        //Check invalid terrain first
        if (theDestinationTerrain == Terrain.WALL) {
            // Taxis cannot pass WALL
            assertFalse(theTaxi.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Taxi should not be able to pass WALL");
        }
        if (theDestinationTerrain == Terrain.GRASS) {
            // Taxis cannot pass WALL
            assertFalse(theTaxi.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Taxi should not be able to pass GRASS");
        }
        if (theDestinationTerrain == Terrain.TRAIL) {
            assertFalse(theTaxi.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Taxi should not be able to pass TRAIL");
        }
        //Check valid terrain, unaffected by lights
        if (theDestinationTerrain == Terrain.STREET) {
            assertTrue(theTaxi.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Taxi should be able to pass STREET");
        }


        //check light-specific scenarios
        if (theDestinationTerrain == Terrain.LIGHT) {
            if (theCurrentLightCondition != Light.RED) {
                assertTrue(theTaxi.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Taxi should be able to pass LIGHT on GREEN/YELLOW");
            } else {
                assertFalse(theTaxi.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Taxi should not be able to pass LIGHT on RED");
            }
        }
        if (theDestinationTerrain == Terrain.CROSSWALK) {
            if (theCurrentLightCondition != Light.RED) {
                assertTrue(theTaxi.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Taxi should be able to pass LIGHT on GREEN/YELLOW");
            } else {
                assertFalse(theTaxi.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Taxi should not be able to pass LIGHT on RED");
            }
        }
    }


    //a list of testing terrains
    //the vehicle should be moving in the direction listed
    private static List<TerrainTest> initializeTerrain() {
        final List<TerrainTest> terrainTests = new ArrayList<>();

        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.TRAIL,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.WALL),
                Direction.SOUTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.WALL,
                Direction.EAST, Terrain.WALL,
                Direction.SOUTH, Terrain.CROSSWALK,
                Direction.WEST, Terrain.LIGHT),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.WALL,
                Direction.EAST, Terrain.WALL,
                Direction.SOUTH, Terrain.CROSSWALK,
                Direction.WEST, Terrain.LIGHT),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.WALL,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.CROSSWALK,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.CROSSWALK,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.TRAIL,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.LIGHT,
                Direction.WEST, Terrain.GRASS),
                Direction.SOUTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.LIGHT,
                Direction.EAST, Terrain.CROSSWALK,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.LIGHT),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.TRAIL,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.TRAIL,
                Direction.EAST, Terrain.LIGHT,
                Direction.SOUTH, Terrain.CROSSWALK,
                Direction.WEST, Terrain.WALL),
                Direction.EAST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.LIGHT,
                Direction.EAST, Terrain.TRAIL,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.CROSSWALK),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.STREET,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.CROSSWALK),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.TRAIL),
                Direction.EAST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.TRAIL),
                Direction.EAST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.CROSSWALK,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.LIGHT),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.TRAIL,
                Direction.WEST, Terrain.LIGHT),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.LIGHT),
                Direction.WEST));

        return terrainTests;
    }

    private record TerrainTest(Map<Direction, Terrain> myNeighbors, Direction myAnswer) { }
}
