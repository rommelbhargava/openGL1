package com.example.rommel.opengl1;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.Vector;

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
    private final float[] mVPMatrix = new float[16];
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
        mbug2.spawn(0.5f, 0.0f, 0.0f, 450f, 0.5f);

        mbug3.setColor(0.2f, 0.3f, 1.0f, 1.0f);
        mbug3.spawn(1.0f, 0.5f, 0.0f, 90.0f, 1.0f);
    }

    public void onDrawFrame(GL10 unused) {


        //Matrix.frustumM(mProjectionMatrix, 0, width, height, -1, 1, 1, 1000);
        Matrix.orthoM(mProjectionMatrix, 0, -mRatio, mRatio, -1, 1, 0, 1000.0f);

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3.0f, 0, 0, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw shape
        //mSquare.draw();



        mbug1.draw(mVPMatrix);
        mbug2.draw(mVPMatrix);
        mbug3.draw(mVPMatrix);
    }

    private float mWidth;
    private float mHeight;
    private float mRatio;
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        //getViewPortMatrix();
        this.mWidth = width;
        this.mHeight = height;
        this.mRatio = this.mWidth / this.mHeight;
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

        float[] PositionVec = new float[4];

        float[] iVPMatrix = new float[16];

        float [] nPositionVec = new float[4];

        float[] tempPosition;

        PositionVec[0] = mRatio*2*((x/mWidth) - 0.5f);
        PositionVec[1] = -(2*((y/mHeight) - 0.5f));
        PositionVec[2] = 0.0f;
        PositionVec[3] = 0.0f;

        Matrix.setIdentityM(iVPMatrix, 0);
        Matrix.invertM(iVPMatrix, 0, mVPMatrix, 0);

        Matrix.multiplyMV(nPositionVec, 0, iVPMatrix, 0, PositionVec, 0);

        Log.d("Matrix:", "X:" + x + "Y:" + y);

        for(int i = 0; i < nPositionVec.length/4; i++)
        {
            Log.d("Matrix:", "InvPos        : ["+nPositionVec[i*4 + 0]+" ,"+nPositionVec[i*4 + 1]+" ,"+nPositionVec[i*4 + 2]+" ,"+nPositionVec[i*4 + 3]+"]" );
        }
        for(int i = 0; i < PositionVec.length/4; i++)
        {
            Log.d("Matrix:", "Pos        : ["+PositionVec[i*4 + 0]+" ,"+PositionVec[i*4 + 1]+" ,"+PositionVec[i*4 + 2]+" ,"+PositionVec[i*4 + 3]+"]" );
        }

        tempPosition = mbug1.getPosition();

        double dist = Math.sqrt((tempPosition[0]-PositionVec[0])*(tempPosition[0]-PositionVec[0]) +
                                (tempPosition[1]-PositionVec[1])*(tempPosition[1]-PositionVec[1]));

        if(dist < 0.1f)
        {
            mbug1.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else
        {
            mbug1.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        }
        Log.d("Dist:", "Bug1 :"+dist);


        tempPosition = mbug2.getPosition();

        dist = Math.sqrt((tempPosition[0]-PositionVec[0])*(tempPosition[0]-PositionVec[0]) +
                         (tempPosition[1]-PositionVec[1])*(tempPosition[1]-PositionVec[1]));

        if(dist < 0.1f)
        {
            mbug2.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else
        {
            mbug2.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        }

        Log.d("Dist:", "Bug1 :"+dist);

        tempPosition = mbug3.getPosition();

        dist = Math.sqrt((tempPosition[0] - PositionVec[0]) * (tempPosition[0] - PositionVec[0]) +
                (tempPosition[1] - PositionVec[1])*(tempPosition[1]-PositionVec[1]));

        if(dist < 0.5f)
        {
            mbug3.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else
        {
            mbug3.setColor(0.0f, 0.0f, 1.0f, 1.0f);
        }
        Log.d("Dist:", "Bug1 :"+dist);

    }


}
