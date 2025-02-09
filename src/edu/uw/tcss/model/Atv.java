package edu.uw.tcss.model;

import java.util.Map;

/**
 * A class that manages the behavior of an ATV vehicle in the
 * road rage application.
 *
 * @author Georgia Karwhite
 * @version 2025 February 08
 */
public class Atv extends AbstractVehicle {
    /**
     * The time ATVs stay dead after a collision
     */
    private static final int DEATH_TIME = 25;

    /**
     * Creates the ATV with its starting state.
     *
     * @param theX the starting x-coordinate of the ATV
     * @param theY the starting y-coordinate of the ATV
     * @param theDirection the starting direction the ATV is facing
     */
    public Atv(final int theX, final int theY, final Direction theDirection) {
        super(theX, theY, theDirection, DEATH_TIME);
    }

    /**
     * Returns the direction this object would like to move, based on the given
     * map of the neighboring terrain.
     * ATVs can travel on any terrain except walls. They ignore all
     * traffic and crosswalk lights, and will never reverse in a map.
     *
     * @param theNeighbors The map of neighboring terrain.
     * @return the direction the ATV would like to move.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction toGive = Direction.random();
        if (!isAcceptable(theNeighbors.get(toGive), toGive)) {
            //the random direction wasn't valid, so keep looking    ATVs start right
            if (isAcceptable(theNeighbors.get(toGive.right()), toGive.right())) {
                toGive = toGive.right();
            } else if (isAcceptable(theNeighbors.get(toGive.left()), toGive.left())) {
                toGive = toGive.left();
            } else if (isAcceptable(theNeighbors.get(toGive.reverse()), toGive.reverse())) {
                toGive = toGive.reverse();
            }
        }
        return toGive;
    }

    /**
     * ATVs can pass through any terrain, regardless of the light color.
     *
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return true if the ATV can pass, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return isValid(theTerrain);
    }



    /*
        Determines if the terrain is valid for the ATV; true if it's not a wall
     */
    private boolean isValid(final Terrain theTerrain) {
        return theTerrain != Terrain.WALL;
    }

    /*
        Makes sure the terrain is valid and the new direction isn't reverse
     */
    private boolean isAcceptable(final Terrain theTerrain, final Direction theDirection) {
        return isValid(theTerrain) && theDirection != getDirection().reverse();
    }
}
