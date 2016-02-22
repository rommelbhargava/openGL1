package com.example.rommel.opengl1;

/**
 * Created by rommel on 2/21/16.
 */
public class Bug extends VisibleActor{

    Triangle body;

    Bug()
    {
        //nothing special

        body = new Triangle();
    }

    public float[] getMatrix()
    {
        return body.getModelMatrix();
    }

    @Override
    void spawn(float x, float y, float z, float xyAngle, float scale) {

        float position[] = new float[3];


        position[0] = x;
        position[1] = y;
        position[2] = z;

        this.setPosition(position);
        body.setPosition(position[0], position[1], position[2], xyAngle, scale);
    }

    @Override
    void draw(float[] ProjectionMatrix) {
        float[] color = this.getColor();
        body.setColor(color[0], color[1], color[2], color[3]);
        body.draw(ProjectionMatrix);
    }
}
