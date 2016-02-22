package com.example.rommel.opengl1;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by rommel on 2/20/16.
 */
public class Triangle {

    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
               0.0f, 100.0f,  0.0f, // top
            -100.0f,  -0.0f,  0.0f, // bottom left
             100.0f,  -0.0f,  0.0f  // bottom right
    };

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";


    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final int mProgram;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private final float[] mRotationMatrix = new float[16];
    private final float[] mPositionMatrix = new float[16];
    private final float[] mScaleMatrix    = new float[16];
    private final float[] mModelMatrix    = new float[16];


    // Set color with red, green, blue and alpha (opacity) values
    private float color[] = { 0.5f, 0.5f, 0.5f, 1.0f };
    public float[] getColor() {
        return color;
    }

    public void setColor(float R, float G, float B, float A) {
        this.color[0] = R;
        this.color[1] = G;
        this.color[2] = B;
        this.color[3] = A;
    }

    //x, y, z
    private float[] mPosition;
    private float   mXYAngle;
    private float   mScale;
    public void setPosition(float x, float y, float z, float angle, float scale)
    {
        this.mPosition[0]   = x;
        this.mPosition[1]   = y;
        this.mPosition[2]   = z;
        this.mXYAngle       = angle;
        this.mScale         = scale;
    }

    public float[] getModelMatrix()
    {
        return mModelMatrix;
    }

    public Triangle() {

        mPosition = new float[3];
        setPosition(0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
        mScale = 1.0f;

        //:initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);


        //:local shader setup
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] ProjectionMatrix) {
        float[] mvpMatrix = new float[16];
        float[] RSMatrix = new float[16];

        GLES20.glUseProgram(mProgram);

        Matrix.setIdentityM(mPositionMatrix, 0);
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.setIdentityM(mScaleMatrix, 0);

        Matrix.translateM(mPositionMatrix, 0, this.mPosition[0], this.mPosition[1], this.mPosition[2]);
        Matrix.setRotateM(mRotationMatrix, 0, this.mXYAngle, 0, 0, -1.0f);
        Matrix.scaleM(mScaleMatrix, 0, mScale, mScale, mScale);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(RSMatrix, 0);
        Matrix.multiplyMM(RSMatrix, 0, mRotationMatrix, 0, mScaleMatrix, 0);
        Matrix.multiplyMM(mModelMatrix, 0, mPositionMatrix, 0, RSMatrix, 0);

        Matrix.multiplyMM(mvpMatrix, 0, ProjectionMatrix, 0, mModelMatrix, 0);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    vertexStride, vertexBuffer);

            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
            GLES20.glUniform4fv(mColorHandle, 1, color, 0);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
