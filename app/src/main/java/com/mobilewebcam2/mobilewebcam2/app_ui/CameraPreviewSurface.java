package com.mobilewebcam2.mobilewebcam2.app_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.mobilewebcam2.mobilewebcam2.exceptions.CameraNotReadyException;
import com.mobilewebcam2.mobilewebcam2.managers.CameraManager;

/**
 * Provides the camera preview in the main screen.
 */
public class CameraPreviewSurface extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "CameraPreviewSurface";

    /**
     * Paint object. Used by onDraw if the camera is down.
     */
    private final Paint camFailedPaint;

    public CameraPreviewSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.v(LOG_TAG, "CameraPreviewSurface Constructor");

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Useful for "backgrounding"context +"\n"+  attrs

        camFailedPaint = new Paint(Color.RED);
        camFailedPaint.setTextSize(40);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(LOG_TAG, "Surface created");

        // Needed for onDraw to be called.
        setWillNotDraw(false);  // TODO Does this produce performance issues?
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        Log.d(LOG_TAG, "Surface changed");

        if(CameraManager.getInstance().isCameraFailing()){
            Log.d(LOG_TAG, "Camera is currently flagged as Failing. Skip the .startPreview() call");
            return;
        }

        try {

            Camera.Size bestPreviewSize = CameraManager.getInstance().getPreviewSize();
            Log.d(LOG_TAG, "Preview Sizing - Final preview size: h"+bestPreviewSize.height+" w"+bestPreviewSize.width);

            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.width = bestPreviewSize.width;
            lp.height = bestPreviewSize.height;
            setLayoutParams(lp);

            CameraManager.getInstance().setPreviewDisplay(getHolder());

            Log.d(LOG_TAG, "Start the preview");
            CameraManager.getInstance().startPreview();

        } catch(CameraNotReadyException e){
            Log.e(LOG_TAG, "Cannot start the preview: camera not ready! Exception is:", e);
        }



        /*
        try {
            Log.d(LOG_TAG, "Camera not yet open: setting it up");
            Camera.Size bestPreviewSize = CameraManager.getInstance().getBestPreviewSize();

            //this.setMinimumHeight(bestPreviewSize.height);
            //this.setMinimumWidth(bestPreviewSize.width);

            getHolder().setFixedSize(bestPreviewSize.width, bestPreviewSize.height);
            Log.d(LOG_TAG, "Size of the preview: w" + this.getHeight() + " h" + this.getWidth() );
            CameraManager.getInstance().setPreviewDisplay(getHolder());


        } catch (CameraNotReadyException e) {
            Log.e(LOG_TAG, "Cannot get the best preview size: camera not ready! Exception is:", e);
        } */
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(LOG_TAG, "Surface destroyed");
        // CameraManager.getInstance().closeCamera();
    }

    /**
     * This method is overridden with the sole purpose of providing a NO PIC preview if the camera is
     * down.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Setup some placeholder to highlight how the camera is down
        // TODO Shall we upload fake "CAMERA OFFLINE" pics in case the camera is down?

        if(!CameraManager.getInstance().isCameraFailing()) {
            super.onDraw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
            canvas.drawText("NO IMAGE", 70, 120, camFailedPaint);
        }
    }
}
