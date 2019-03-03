package com.mobilewebcam2.mobilewebcam2.app_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mobilewebcam2.mobilewebcam2.exceptions.CameraNotReadyException;
import com.mobilewebcam2.mobilewebcam2.managers.CameraManager;
import com.mobilewebcam2.mobilewebcam2.managers.TriggersManager;

import java.util.Timer;
import java.util.TimerTask;

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

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        getHolder().setFixedSize(320, 240); // Fallback value, in case the camera will not provide better values.
        // holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        camFailedPaint = new Paint(Color.RED);
        camFailedPaint.setTextSize(40);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Needed for onDraw to be called.
        setWillNotDraw(false);  // TODO Does this produce performance issues?
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        if(!CameraManager.getInstance().isCameraOpen()){
            return;
        }
        try {
            Camera.Size bestPreviewSize = CameraManager.getInstance().getBestPreviewSize();
            holder.setFixedSize(bestPreviewSize.width, bestPreviewSize.height);
            Log.d(LOG_TAG, "Size of the preview: w" + bestPreviewSize.width + " h" +  bestPreviewSize.height );
            CameraManager.getInstance().startPreview(holder);

        } catch(CameraNotReadyException e){
            Log.e(LOG_TAG, "Cannot start the preview: camera not ready! Exception is:", e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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

        if(CameraManager.getInstance().isCameraOpen()) {
            super.onDraw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
            canvas.drawText("NO IMAGE", 70, 120, camFailedPaint);
        }
    }
}
