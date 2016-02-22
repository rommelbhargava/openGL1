package com.example.rommel.opengl1;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by rommel on 2/20/16.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Bug mbug1;
    private Bug mbug2;
    private Bug mbug3;

    private Square   mSquare;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public float getScreenX() {
        return ScreenX;
    }

    public void setScreenX(float screenX) {
        ScreenX = screenX;
    }

    public volatile float ScreenX;

    public float getScreenY() {
        return ScreenY;
    }

    public void setScreenY(float screenY) {
        ScreenY = screenY;
    }

    public volatile float ScreenY;

    private volatile float zoom;



    public float getZoom() {
        return zoom;
    }


    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mbug1 = new Bug();
        mbug2 = new Bug();
        mbug3 = new Bug();

        mbug1.setColor(1.0f, 0.2f, 0.2f, 1.0f);
        mbug1.spawn(0.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        mbug2.setColor(0.2f, 1.0f, 0.2f, 1.0f);
        mbug2.spawn(500.0f, -0.0f, 0.0f, 10.0f, 0.5f);

        mbug3.setColor(0.2f, 0.3f, 1.0f, 1.0f);
        mbug3.spawn(-0.0f, 500.0f, 0.0f, 90.0f, 1.0f);
    }

    public void onDrawFrame(GL10 unused) {

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, ScreenX, ScreenY, -zoom, ScreenX, ScreenY, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw shape
        //mSquare.draw();
        mbug1.draw(mMVPMatrix);
        mbug2.draw(mMVPMatrix);
        mbug3.draw(mMVPMatrix);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        //Matrix.frustumM(mProjectionMatrix, 0, width, height, -1, 1, 1, 1000);
        Matrix.orthoM(mProjectionMatrix, 0, -(width>>1), width>>1, -(height>>1), height>>1, 0, 1000.0f);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private volatile float mTouchX;
    private volatile float mTouchY;
    public void SetXY(float x, float y)
    {
        this.mTouchX = x;
        this.mTouchY = y;
        float[] position;
        float[] bug1Matrix = mbug1.getMatrix();

        position = mbug1.getPosition();

        if((x<position[0]+500 && x > position[0]-500) &&
           (y<position[1]+500 && y > position[1]-500))
        {
            mbug1.setColor(1.0f, 0.0f, 1.0f, 1.0f);
        }
        else
        {
            mbug1.setColor(1.0f, 0.2f, 0.2f, 1.0f);
        }

    }


}
