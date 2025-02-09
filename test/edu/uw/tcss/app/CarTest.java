/*
 * TCSS 305 - Road Rage
 */

package edu.uw.tcss.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.uw.tcss.model.Car;
import edu.uw.tcss.model.Direction;
import edu.uw.tcss.model.Light;
import edu.uw.tcss.model.Terrain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class Car.
 *
 * @author Georgia Karwhite
 * @version 2025 February 09
 */
public class CarTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    private static final int FIXTURE_X = 10;

    private static final int FIXTURE_Y = 10;

    private static final int FIXTURE_DEATH_TIME = 15;

    private static final List<TerrainTest> MY_TERRAIN_TESTS = initializeTerrain();


    /** Test method for Car constructor. */
    @Test
    public void testCarConstructor() {
        final Car h = new Car(FIXTURE_X, FIXTURE_Y, Direction.NORTH);

        assertEquals(FIXTURE_X, h.getX(),
                "Car x coordinate not initialized correctly!");
        assertEquals(FIXTURE_Y, h.getY(),
                "Car y coordinate not initialized correctly!");
        assertEquals(Direction.NORTH, h.getDirection(),
                "Car direction not initialized correctly!");
        assertEquals(FIXTURE_DEATH_TIME, h.getDeathTime(),
                "Car death time not initialized correctly!");
        assertTrue(h.isAlive(),
                "Car isAlive() fails initially!");
    }

    /** Test method for Car setters. */
    @Test
    public void testCarSetters() {
        final Car h = new Car(FIXTURE_X, FIXTURE_Y, Direction.NORTH);

        final int changedX = 12;
        final int changedY = 13;

        h.setX(changedX);
        assertEquals(changedX, h.getX(),
                "Car setX failed!");

        h.setY(changedY);
        assertEquals(changedY, h.getY(),
                "Car setY failed!");

        h.setDirection(Direction.SOUTH);
        assertEquals(Direction.SOUTH, h.getDirection(),
                "Car setDirection failed!");
    }

    /**
     * Test method for {@link Car#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {
        final Car car = new Car(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Direction dir : Direction.values()) {
            car.setDirection(dir);
            loopTerrainsAndLights(car);
        }
    }

    /**
     * Test for chooseDirection.
     */
    @Test
    public void testChooseDirection() {
        final Car car = new Car(0, 0, Direction.NORTH);
        for (final TerrainTest test : MY_TERRAIN_TESTS) {
            assertEquals(test.myAnswer, car.chooseDirection(test.myNeighbors),
                    "Car chooseDirection selected incorrectly");
        }
    }



    /*  Loops through the terrains and lights  */
    private void loopTerrainsAndLights(final Car theCar) {
        for (final Terrain destinationTerrain : Terrain.values()) {
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                checkValidity(destinationTerrain, currentLightCondition, theCar);
            }
        }
    }


    private void checkValidity(final Terrain theDestinationTerrain,
                               final Light theCurrentLightCondition,
                               final Car theCar) {
        //Check invalid terrain first
        if (theDestinationTerrain == Terrain.WALL) {
            // Cars cannot pass WALL
            assertFalse(theCar.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Car should not be able to pass WALL");
        }
        if (theDestinationTerrain == Terrain.GRASS) {
            // Cars cannot pass WALL
            assertFalse(theCar.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Car should not be able to pass GRASS");
        }
        if (theDestinationTerrain == Terrain.TRAIL) {
            assertFalse(theCar.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Car should not be able to pass TRAIL");
        }
        //Check valid terrain, unaffected by lights
        if (theDestinationTerrain == Terrain.STREET) {
            assertTrue(theCar.canPass(theDestinationTerrain, theCurrentLightCondition),
                    "Car should be able to pass STREET");
        }


        //check light-specific scenarios
        if (theDestinationTerrain == Terrain.LIGHT) {
            if (theCurrentLightCondition != Light.RED) {
                assertTrue(theCar.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Car should be able to pass LIGHT on GREEN/YELLOW");
            } else {
                assertFalse(theCar.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Car should not be able to pass LIGHT on RED");
            }
        }
        if (theDestinationTerrain == Terrain.CROSSWALK) {
            if (theCurrentLightCondition == Light.GREEN) {
                assertTrue(theCar.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Car should be able to pass LIGHT on GREEN/YELLOW");
            } else {
                assertFalse(theCar.canPass(theDestinationTerrain,
                                theCurrentLightCondition),
                        "Car should not be able to pass LIGHT on RED");
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
