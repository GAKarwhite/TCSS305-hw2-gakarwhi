package edu.uw.tcss.model;

import java.util.Map;

/**
 * A class that manages the behavior of bicycles in the
 * road rage application.
 *
 * @author Georgia Karwhite
 * @version 2025 February 08
 */
public class Bicycle extends AbstractVehicle {

    /**
     * The time the Bicycle stays dead after a collision.
     */
    private static final int DEATH_TIME = 35;

    /**
     * Creates the Bicycle with its starting state.
     *
     * @param theX the starting x-coordinate of the Bicycle
     * @param theY the starting y-coordinate of the Bicycle
     * @param theDirection the starting direction the Bicycle is facing
     */
    public Bicycle(final int theX, final int theY, final Direction theDirection) {
        super(theX, theY, theDirection, DEATH_TIME);
    }


    /**
     * Returns the direction this object would like to move, based on the given
     * map of the neighboring terrain.
     * Bicycles can travel through streets, lights, crosswalks, and trails.
     * Trails are preferred.
     *
     * @param theNeighbors The map of neighboring terrain.
     * @return the direction the bicycle would like to move.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction toGive = findTrail(theNeighbors);
        if (toGive == null) {
            if (isValid(theNeighbors.get(getDirection()))) {
                toGive = getDirection();
            } else if (isValid(theNeighbors.get(getDirection().left()))) {
                toGive = getDirection().left();
            } else if (isValid(theNeighbors.get(getDirection().right()))) {
                toGive = getDirection().right();
            } else {
                toGive = getDirection().reverse();
            }
        }
        return toGive;
    }


    /**
     * Bicycles can travel on trails and streets regardless of lights.
     * They stop for yellow and red lights.
     *
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return true if the Bicycle can pass, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean toGive = isValid(theTerrain);
        if (toGive) {
            if ((theTerrain == Terrain.CROSSWALK || theTerrain == Terrain.LIGHT)
                    && theLight != Light.GREEN) {
                toGive = false;
            }
        }
        return toGive;
    }



    /*
        Returns null if there is no trail available
     */
    private Direction findTrail(final Map<Direction, Terrain> theNeighbors) {
        Direction toGive = null;
        for (final Direction direction : theNeighbors.keySet()) {
            if (theNeighbors.get(direction) == Terrain.TRAIL //if there's a trail
                    && direction != getDirection().reverse()) { //and it's not behind the bike
                toGive = direction;
            }
        }
        return toGive;
    }

    private boolean isValid(final Terrain theTerrain) {
        return theTerrain == Terrain.TRAIL
                || theTerrain == Terrain.STREET
                || theTerrain == Terrain.LIGHT
                || theTerrain == Terrain.CROSSWALK;
    }
}
