/*
 * TCSS 305 - Road Rage
 */

package edu.uw.tcss.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.uw.tcss.model.Atv;
import edu.uw.tcss.model.Direction;
import edu.uw.tcss.model.Light;
import edu.uw.tcss.model.Terrain;
import edu.uw.tcss.model.Vehicle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class Atv.
 *
 * @author Georgia Karwhite
 * @version 2025 February 09
 */
public class AtvTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    private static final int FIXTURE_X = 10;

    private static final int FIXTURE_Y = 10;

    private static final int FIXTURE_DEATH_TIME = 25;

    private static final List<TerrainTest> MY_TERRAIN_TESTS = initializeTerrain();

    /** Test method for Atv constructor. */
    @Test
    public void testAtvConstructor() {
        final Atv h = new Atv(FIXTURE_X, FIXTURE_Y, Direction.NORTH);

        assertEquals(FIXTURE_X, h.getX(),
                "ATV x coordinate not initialized correctly!");
        assertEquals(FIXTURE_Y, h.getY(),
                "Atv y coordinate not initialized correctly!");
        assertEquals(Direction.NORTH, h.getDirection(),
                "Atv direction not initialized correctly!");
        assertEquals(FIXTURE_DEATH_TIME, h.getDeathTime(),
                "Atv death time not initialized correctly!");
        assertTrue(h.isAlive(),
                "Atv isAlive() fails initially!");
    }

    /** Test method for Atv setters. */
    @Test
    public void testAtvSetters() {
        final Atv h = new Atv(FIXTURE_X, FIXTURE_Y, Direction.NORTH);

        final int changedX = 12;
        final int changedY = 13;

        h.setX(changedX);
        assertEquals(changedX, h.getX(),
                "Atv setX failed!");

        h.setY(changedY);
        assertEquals(changedY, h.getY(),
                "Atv setY failed!");

        h.setDirection(Direction.SOUTH);
        assertEquals(Direction.SOUTH, h.getDirection(),
                "Atv setDirection failed!");
    }

    /**
     * Test method for {@link Atv#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {

        //Atvs should be able to move on any terrain that is not Walls

        final Atv atv = new Atv(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Direction dir : Direction.values()) {
            atv.setDirection(dir);
            loopTerrainsAndLights(atv);
        }
    }


    /** Test the chooseDirection method, for choosing the "correct" direction. */
    @Test
    public void testChooseDirectionCorrectness() {
        final Atv atv = new Atv(0, 0, Direction.NORTH);
        for (int i = 0; i < TRIES_FOR_RANDOMNESS; i++) {
            for (final TerrainTest test : MY_TERRAIN_TESTS) {
                assertNotEquals(atv.chooseDirection(test.myNeighbors), test.myAnswer,
                        "ATVs should not choose to go into a wall");
                assertNotEquals(atv.chooseDirection(test.myNeighbors),
                        atv.getDirection().reverse(), "ATVs should not reverse");
            }
        } //this tries each map

    }

    /**  Test the chooseDirection method, for choosing a random direction.  */
    @Test
    public void testChooseDirectionRandom() {
        final Atv atv = new Atv(0, 0, Direction.NORTH);
        for (final TerrainTest test : MY_TERRAIN_TESTS) {
            assertTrue(testRandomDirections(test, atv),
                    "ATV is not choosing all possible directions");
        }
    }



    /* checks if the vehicle has gone to all possible directions */
    private boolean testRandomDirections(final TerrainTest theTest, final Vehicle theVehicle) {
        final Set<Direction> directions = new HashSet<>();
        for (int i = 0; i < TRIES_FOR_RANDOMNESS; i++) {
            directions.add(theVehicle.chooseDirection(theTest.myNeighbors()));
        }
        return directions.size()
                == theTest.myNeighbors.size() - countWalls(theTest.myNeighbors) - 1;
                //the size should be one less than the possible number (total - walls)
                //because it should not reverse
    }


    /* this counts the number of walls in the test */
    private int countWalls(final Map<Direction, Terrain> theNeighbors) {
        int walls = 0;
        for (final Direction dir : theNeighbors.keySet()) {
            if (Terrain.WALL == theNeighbors.get(dir)) {
                walls++;
            }
        }
        return walls;
    }

    /*  Loops through the terrains and lights  */
    private void loopTerrainsAndLights(final Atv theAtv) {
        for (final Terrain destinationTerrain : Terrain.values()) {
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                if (destinationTerrain == Terrain.WALL) {
                    // ATVs cannot pass WALL
                    assertFalse(theAtv.canPass(destinationTerrain, currentLightCondition),
                            "Atv should be able to pass WALL");
                }
            }
        }
    }


    //a list of testing terrains
    //for Atvs, they should NOT be going the direction listed in the TerrainTest
    //if 'null' is listed, any direction is good.
    private static List<TerrainTest> initializeTerrain() {
        final List<TerrainTest> terrainTests = new ArrayList<>();

        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.TRAIL,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.WALL),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.WALL,
                Direction.EAST, Terrain.WALL,
                Direction.SOUTH, Terrain.CROSSWALK,
                Direction.WEST, Terrain.LIGHT),
                Direction.EAST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.WALL,
                Direction.EAST, Terrain.WALL,
                Direction.SOUTH, Terrain.CROSSWALK,
                Direction.WEST, Terrain.LIGHT),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.WALL,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                Direction.NORTH));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.CROSSWALK,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.CROSSWALK,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.TRAIL,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.GRASS),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.LIGHT,
                Direction.EAST, Terrain.CROSSWALK,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.LIGHT),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.TRAIL,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.LIGHT),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.TRAIL,
                Direction.EAST, Terrain.LIGHT,
                Direction.SOUTH, Terrain.CROSSWALK,
                Direction.WEST, Terrain.WALL),
                Direction.WEST));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.LIGHT,
                Direction.EAST, Terrain.TRAIL,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.CROSSWALK),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.STREET,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.CROSSWALK),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.TRAIL),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.STREET,
                Direction.WEST, Terrain.TRAIL),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.CROSSWALK,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.LIGHT),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.GRASS,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.LIGHT),
                null));
        terrainTests.add(new TerrainTest(Map.of(Direction.NORTH, Terrain.GRASS,
                Direction.EAST, Terrain.STREET,
                Direction.SOUTH, Terrain.GRASS,
                Direction.WEST, Terrain.LIGHT),
                null));

        return terrainTests;
    }

    private record TerrainTest(Map<Direction, Terrain> myNeighbors, Direction myAnswer) { }
}
