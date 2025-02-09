package edu.uw.tcss.model;

import java.util.Map;

/**
 * A class that manages the behavior of Taxis in the
 * road rage application.
 *
 * @author Georgia Karwhite
 * @version 2025 February 08
 */
public class Taxi extends AbstractVehicle {

    /**
     * The time the Taxi stays dead after a collision
     */
    private static final int DEATH_TIME = 15;

    /**
     * The time the Taxi waits at a red crosswalk
     */
    private static final int WAIT_TIME = 3;

    /**
     * How long the taxi will wait at a crosswalk
     */
    private int myWaitCycle;


    /**
     * Creates the Taxi with its starting state.
     *
     * @param theX the starting x-coordinate of the Taxi
     * @param theY the starting y-coordinate of the Taxi
     * @param theDirection the starting direction the Taxi is facing
     */
    public Taxi(final int theX, final int theY, final Direction theDirection) {
        super(theX, theY, theDirection, DEATH_TIME);
    }


    /**
     * Returns the direction this object would like to move, based on the given
     * map of the neighboring terrain.
     * Taxis prefer to go straight, then go left, then go right, and only
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
     * Taxis stop for red lights at intersections.
     * They stop temporarily at red lights on crosswalks.
     *
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return true if the Taxi can pass, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean toGive = false;
        //crosswalk+red light will be handled uniquely.
        if (theTerrain == Terrain.CROSSWALK && theLight == Light.RED) {
            if (myWaitCycle > 0) {
                myWaitCycle--;

                if (myWaitCycle == 0) {
                    toGive = true;
                }
            } else {
                myWaitCycle = WAIT_TIME;
            }
        } else {
            toGive = theTerrain == Terrain.STREET
                    || (theTerrain == Terrain.CROSSWALK || theTerrain == Terrain.LIGHT)
                    && (theLight == Light.YELLOW || theLight == Light.GREEN);
            myWaitCycle = 0;
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
