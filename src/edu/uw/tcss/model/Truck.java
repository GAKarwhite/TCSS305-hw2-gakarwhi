package edu.uw.tcss.model;

import java.util.Map;

/**
 * A class that manages the behavior of Trucks in the
 * road rage application.
 *
 * @author Georgia Karwhite
 * @version 2025 February 08
 */
public class Truck extends AbstractVehicle {
    /**
     * The truck does not die, so the time it stays dead is 0
     */
    private static final int DEATH_TIME = 0;


    /**
     * Creates the Truck with its starting state.
     *
     * @param theX the starting x-coordinate of the Truck
     * @param theY the starting y-coordinate of the Truck
     * @param theDirection the starting direction the Truck is facing
     */
    public Truck(final int theX, final int theY, final Direction theDirection) {
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
        Direction toGive = onlyOneValidDirection(theNeighbors);
        if (toGive == null) {
            toGive = Direction.random();
            if (!isAcceptable(theNeighbors.get(toGive), toGive)) {
                //the direction is not a good choice for random, so keep checking
                //trucks will start by checking to the left
                if (isAcceptable(theNeighbors.get(toGive.left()), toGive.left())) {
                    toGive = toGive.left();
                } else if (isAcceptable(theNeighbors.get(toGive.right()), toGive.right())) {
                    toGive = toGive.right();
                } else {
                    toGive = toGive.reverse();
                }
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
        boolean toGive = isValid(theTerrain);
        if (theTerrain == Terrain.CROSSWALK && theLight == Light.RED) {
            toGive = false;
        }
        return toGive;
    }

    /*
        This returns the valid direction if there is only one, otherwise null.
        This is the only way humans should reverse.
     */
    private Direction onlyOneValidDirection(final Map<Direction, Terrain> theNeighbors) {
        int validDirections = 0;
        Direction validDirection = null;
        for (final Direction direction : theNeighbors.keySet()) {
            if (isValid(theNeighbors.get(direction))) {
                validDirections++;
                validDirection = direction;
            }
        }
        if (validDirections != 1) {
            validDirection = null;
        }
        return validDirection;
    }

    /*
        Checks if the terrain is valid
     */
    private boolean isValid(final Terrain theTerrain) {
        return theTerrain == Terrain.STREET
                || theTerrain == Terrain.LIGHT
                || theTerrain == Terrain.CROSSWALK;
    }

    /*
        Makes sure the terrain is valid and the new direction isn't reverse
     */
    private boolean isAcceptable(final Terrain theTerrain, final Direction theDirection) {
        return isValid(theTerrain) && theDirection != getDirection().reverse();
    }
}
