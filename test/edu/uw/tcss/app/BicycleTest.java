/*
 * TCSS 305 - Road Rage
 */

package edu.uw.tcss.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.uw.tcss.model.Bicycle;
import edu.uw.tcss.model.Direction;
import edu.uw.tcss.model.Light;
import edu.uw.tcss.model.Terrain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class Bicycle.
 *
 * @author Georgia Karwhite
 * @version 2025 February 09
 */
public class BicycleTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    private static final int FIXTURE_X = 10;

    private static final int FIXTURE_Y = 10;

    private static final int FIXTURE_DEATH_TIME = 35;

    private static final List<TerrainTest> MY_TERRAIN_TESTS = initializeTerrain();

    /** Test method for Bicycle constructor. */
    @Test
    public void testBicycleConstructor() {
        final Bicycle h = new Bicycle(FIXTURE_X, FIXTURE_Y, Direction.NORTH);

        assertEquals(FIXTURE_X, h.getX(),
                "Bicycle x coordinate not initialized correctly!");
        assertEquals(FIXTURE_Y, h.getY(),
                "Bicycle y coordinate not initialized correctly!");
        assertEquals(Direction.NORTH, h.getDirection(),
                "Bicycle direction not initialized correctly!");
        assertEquals(FIXTURE_DEATH_TIME, h.getDeathTime(),
                "Bicycle death time not initialized correctly!");
        assertTrue(h.isAlive(),
                "Bicycle isAlive() fails initially!");
    }

    /** Test method for Bicycle setters. */
    @Test
    public void testBicycleSetters() {
        final Bicycle h = new Bicycle(FIXTURE_X, FIXTURE_Y, Direction.NORTH);

        final int changedX = 12;
        final int changedY = 13;

        h.setX(changedX);
        assertEquals(changedX, h.getX(),
                "Bicycle setX failed!");

        h.setY(changedY);
        assertEquals(changedY, h.getY(),
                "Bicycle setY failed!");

        h.setDirection(Direction.SOUTH);
        assertEquals(Direction.SOUTH, h.getDirection(),
                "Bicycle setDirection failed!");
    }

    /**
     * Test method for {@link Bicycle#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {
        final Bicycle bike = new Bicycle(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Direction dir : Direction.values()) {
            bike.setDirection(dir);
            loopTerrainsAndLights(bike);
        }
    }

    /**
     * Test for chooseDirection.
     */
    @Test
    public void testChooseDirection() {
        final Bicycle bike = new Bicycle(0, 0, Direction.NORTH);
        for (final TerrainTest test : MY_TERRAIN_TESTS) {
            assertEquals(test.myAnswer, bike.chooseDirection(test.myNeighbors),
                    "Bicycle chooseDirection selected incorrectly");
        }
    }




    /*  Loops through the terrains and lights  */
    private void loopTerrainsAndLights(final Bicycle theBicycle) {
        for (final Terrain destinationTerrain : Terrain.values()) {
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                checkValidity(destinationTerrain, currentLightCondition, theBicycle);
            }
        }
    }

    
    
    private void checkValidity(final Terrain theDestinationTerrain,
                               final Light theCurrentLightCondition,
                               final Bicycle theBicycle) {
        //Check invalid terrain first
        if (theDestinationTerrain == Terrain.WALL) {
            // Bicycles cannot pass WALL
            assertFalse(theBicycle.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Bicycle should not be able to pass WALL");
        }
        if (theDestinationTerrain == Terrain.GRASS) {
            // Bicycles cannot pass WALL
            assertFalse(theBicycle.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Bicycle should not be able to pass GRASS");
        }

        //Check valid terrain, unaffected by lights
        if (theDestinationTerrain == Terrain.STREET) {
            assertTrue(theBicycle.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Bicycle should be able to pass STREET");
        }
        if (theDestinationTerrain == Terrain.TRAIL) {
            assertTrue(theBicycle.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Bicycle should be able to pass TRAIL");
        }

        //check light-specific scenarios
        if (theDestinationTerrain == Terrain.LIGHT) {
            if (theCurrentLightCondition == Light.GREEN) {
                assertTrue(theBicycle.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Bicycle should be able to pass LIGHT on GREEN");
            } else {
                assertFalse(theBicycle.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Bicycle should not be able to pass LIGHT on RED/YELLOW");
            }
        }
        if (theDestinationTerrain == Terrain.CROSSWALK) {
            if (theCurrentLightCondition == Light.GREEN) {
                assertTrue(theBicycle.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Bicycle should be able to pass LIGHT on GREEN");
            } else {
                assertFalse(theBicycle.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Bicycle should not be able to pass LIGHT on RED/YELLOW");
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
                Direction.EAST));
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
                Direction.NORTH));
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
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.TRAIL,
                Direction.EAST, Terrain.LIGHT,
                Direction.SOUTH, Terrain.CROSSWALK,
                Direction.WEST, Terrain.WALL),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.LIGHT,
                Direction.EAST, Terrain.TRAIL,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.CROSSWALK),
                Direction.EAST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.STREET,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.CROSSWALK),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.TRAIL),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.TRAIL),
                Direction.WEST));
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
