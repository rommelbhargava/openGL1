package com.example.rommel.opengl1;

/**
 * Created by rommel on 2/21/16.
 */
public class NPCActor {

    //Position in 3D space of an actor
    private float[] position;
    //Direction where actor is moving
    private float[] direction;
    //Orientation of actor : Not Used Yet
    private float[] orientation;
    //Speed of actor
    private float[] speed;

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getDirection() {
        return direction;
    }

    public void setDirection(float[] direction) {
        this.direction = direction;
    }

    public float[] getOrientation() {
        return orientation;
    }

    public void setOrientation(float[] orientation) {
        this.orientation = orientation;
    }

    public float[] getSpeed() {
        return speed;
    }

    public void setSpeed(float[] speed) {
        this.speed = speed;
    }
}
