package edu.uw.tcss.model;

import java.util.Map;

/**
 * A class that manages the behavior of Cars in the
 * road rage application.
 *
 * @author Georgia Karwhite
 * @version 2025 February 08
 */
public class Car extends AbstractVehicle {
    /**
     * The time the Taxi stays dead after a collision
     */
    private static final int DEATH_TIME = 15;

    /**
     * Creates the Car with its starting state.
     *
     * @param theX the starting x-coordinate of the Car
     * @param theY the starting y-coordinate of the Car
     * @param theDirection the starting direction the Car is facing
     */
    public Car(final int theX, final int theY, final Direction theDirection) {
        super(theX, theY, theDirection, DEATH_TIME);
    }

    /**
     * Returns the direction this object would like to move, based on the given
     * map of the neighboring terrain.
     * Cars prefer to go straight, then go left, then go right, and only
     * turns around as a last resort.
     *
     * @param theNeighbors The map of neighboring terrain.
     * @return the direction the taxi would like to move.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction toGive = getDirection();
        if (!isValid(theNeighbors.get(toGive))) {
            if (isValid(theNeighbors.get(toGive.left()))) {
                toGive = getDirection().left();
            } else if (isValid(theNeighbors.get(toGive.right()))) {
                toGive = getDirection().right();
            } else {
                toGive = getDirection().reverse();
            }
        }
        return toGive;
    }

    /**
     * Cars stop for red lights at intersections.
     * They stop for red and yellow lights at crosswalks.
     *
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return true if the Taxi can pass, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean toGive = isValid(theTerrain); //it can pass if it's a street
        if (theTerrain == Terrain.CROSSWALK && theLight != Light.GREEN) {
            toGive = false;
        }
        if (theTerrain == Terrain.LIGHT && theLight == Light.RED) {
            toGive = false;
        }
        return toGive;
    }


    /*
        Checks if the terrain is valid
     */
    private boolean isValid(final Terrain theTerrain) {
        return theTerrain == Terrain.STREET
                || theTerrain == Terrain.LIGHT
                || theTerrain == Terrain.CROSSWALK;
    }
}
