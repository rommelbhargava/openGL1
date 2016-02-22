package com.example.rommel.opengl1;

/**
 * Created by rommel on 2/21/16.
 */
public abstract class VisibleActor extends NPCActor {
    private float[] color;

    VisibleActor()
    {
        color = new float[4];
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float R, float G, float B, float A) {
        this.color[0] = R;
        this.color[1] = G;
        this.color[2] = B;
        this.color[3] = A;
    }

    abstract void spawn(float x, float y, float z, float xyAngle, float scale);
    abstract void draw(float[] ProjectionMatrix);
}
