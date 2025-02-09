package edu.uw.tcss.model;

import java.util.Iterator;
import java.util.Map;

/**
 * A class that manages the behavior of human-type "vehicle" objects
 * in the road rage application.
 *
 * @author Georgia Karwhite
 * @version 2025 February 09
 */
public class Human extends AbstractVehicle {

    /**
     * The time humans stay dead after a collision
     */
    private static final int DEATH_TIME = 45;

    /**
     * Creates the human with its starting state.
     *
     * @param theX the starting x-coordinate of the human
     * @param theY the starting y-coordinate of the human
     * @param theDirection the starting direction the human is facing
     */
    public Human(final int theX, final int theY, final Direction theDirection) {
        super(theX, theY, theDirection, DEATH_TIME);
    }

    /**
     * Returns the direction this object would like to move, based on the given
     * map of the neighboring terrain.
     * Humans move randomly on grass, and crosswalks
     * when the light is yellow or red. They prefer using the crosswalk when they
     * have the option.
     *
     * @param theNeighbors The map of neighboring terrain.
     * @return the direction this object would like to move.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction toGive = null;
        boolean crosswalk = false;
        final Iterator<Direction> directions = theNeighbors.keySet().iterator();
        Direction current = null;
        while (directions.hasNext() && !crosswalk) {
            current = directions.next();
            if (theNeighbors.get(current) == Terrain.CROSSWALK //if there is a crosswalk
                    && current != getDirection().reverse()) { //the crosswalk isn't behind them
                toGive = current;
                crosswalk = true;
            }
        }
        if (!crosswalk) { //either a crosswalk was found or it was not
            toGive = randomDirection(theNeighbors);
        }

        return toGive;
    }

    /**
     * Humans can pass the terrain if it is grass, or if it is a crosswalk
     * and the light is yellow or red.
     *
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return true if the Human can pass, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return theTerrain == Terrain.GRASS
                || theTerrain == Terrain.CROSSWALK
                && (theLight == Light.RED || theLight == Light.YELLOW);
    }


    /*
        Humans can go on grass and crosswalk
     */
    private static boolean isValid(final Terrain theTerrain) {
        return theTerrain == Terrain.CROSSWALK || theTerrain == Terrain.GRASS;
    }

    /*
        Chooses a random direction
     */
    private Direction randomDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction toGive = onlyOneValidDirection(theNeighbors);
        if (toGive == null) { //there is more than one random direction, continue randomizing
            toGive = Direction.random();
            if (!isAcceptableRandom(theNeighbors, toGive)) {
                //first try didn't work, try another direction. Humans start left.
                if (isAcceptableRandom(theNeighbors, toGive.left())) {
                    toGive = toGive.left();
                } else if (isAcceptableRandom(theNeighbors, toGive.right())) {
                    toGive = toGive.right();
                } else {
                    toGive = toGive.reverse();
                }
            }
        }
        return toGive;
    }

    /*
        This checks if the direction is a valid terrain and is not the reverse of
        the current direction
     */
    private boolean isAcceptableRandom(final Map<Direction, Terrain> theNeighbors,
                                 final Direction theDirection) {
        return isValid(theNeighbors.get(theDirection))
                && theDirection != getDirection().reverse();
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
}
