package com.example.rommel.opengl1;

import android.content.Context;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by rommel on 2/20/16.
 */
class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;


    private ScaleGestureDetector scaleGestureDetector;

    public MyGLSurfaceView(MainActivity context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        mRenderer.setZoom(-3.0f);
        mRenderer.setScreenX(0.0f);
        mRenderer.setScreenY(0.0f);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y < getHeight()) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x > getWidth()) {
                    dy = dy * -1 ;
                }
                mRenderer.setScreenX(mRenderer.getScreenX() + dx / 1.0f);
                mRenderer.setScreenY(mRenderer.getScreenY() + dy / 1.0f);
                requestRender();
                break;
            case MotionEvent.ACTION_DOWN:
                mRenderer.SetXY(x, y);

                Log.d("Pointer:", " [" + x + ", " + y + "]");
                break;

        }


        mPreviousX = x;
        mPreviousY = y;

        scaleGestureDetector.onTouchEvent(e);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float zoom = mRenderer.getZoom();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            Log.d("MAIN", "ScaleFactor: "+ scaleFactor);
            zoom -= (1 - scaleFactor);
            mRenderer.setZoom(zoom);
            return true;
        }
    }
}
