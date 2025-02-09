package edu.uw.tcss.model;

/**
 * This is an abstract class that manages shared behavior for
 * vehicles.
 *
 * @author Georgia Karwhite
 * @version 2025 February 09
 */
public abstract class AbstractVehicle implements Vehicle {

    /**
     * This keeps track of if the vehicle is alive or not.
     */
    private int myLife;

    /**
     * How long the vehicle should remain dead, in update cycles.
     */
    private final int myDeathTime;

    /**
     * The direction the vehicle is travelling.
     */
    private Direction myDirection;

    /**
     * The x coordinate of the vehicle.
     */
    private int myX;

    /**
     * The y coordinate of the vehicle.
     */
    private int myY;


    private final Starting myStartingState;


    /**
     * Creates the vehicle with its starting state.
     *
     * @param theX the starting x-coordinate of the vehicle
     * @param theY the starting y-coordinate of the vehicle
     * @param theDirection the starting direction the vehicle is facing
     * @param theDeathTime how long the vehicle remains dead after a collision
     */
    public AbstractVehicle(final int theX, final int theY, final Direction theDirection,
                           final int theDeathTime) {
        super();
        myX = theX;
        myY = theY;
        myDeathTime = theDeathTime;
        myLife = 0;
        myDirection = theDirection;
        myStartingState = new Starting(theX, theY, theDirection);
    }


    @Override
    public void collide(final Vehicle theOther) {
        if (myLife == 0 && myDeathTime > theOther.getDeathTime()) {
            myLife = myDeathTime;
        } //myLife < 0 when it's alive;
        // they only die (in the current implementation) when their death times are
        // less than the other vehicle
    }

    @Override
    public String getImageFileName() {
        final String toGive;
        if (myLife == 0) {
            toGive = this.getClass().getSimpleName().toLowerCase() + ".gif";
        } else {
            toGive = this.getClass().getSimpleName().toLowerCase() + "_dead.gif";
        }
        return toGive;
    }

    @Override
    public int getDeathTime() {
        return myDeathTime;
    }

    @Override
    public Direction getDirection() {
        return myDirection;
    }

    @Override
    public int getX() {
        return myX;
    }

    @Override
    public int getY() {
        return myY;
    }

    @Override
    public boolean isAlive() {
        return myLife == 0;
    }

    @Override
    public void poke() {
        if (myLife > 0) {
            myLife--;
        }
        if (myLife == 0) {
            myDirection = Direction.random();
        }
    }

    @Override
    public void reset() {
        myX = myStartingState.myX;
        myY = myStartingState.myY;
        myDirection = myStartingState.myDirection;
        myLife = 0; //turns it back to alive, regardless of what it was previously
    }

    @Override
    public void setDirection(final Direction theDirection) {
        myDirection = theDirection;
    }

    @Override
    public void setX(final int theX) {
        myX = theX;
    }

    @Override
    public void setY(final int theY) {
        myY = theY;
    }



    private record Starting(int myX, int myY, Direction myDirection) { }
}
