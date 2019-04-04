package com.mobilewebcam2.mobilewebcam2.app_ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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
import com.mobilewebcam2.mobilewebcam2.managers.RootManager;

/**
 * The camera preview.
 *
 * Calls CameraManager to shoot a picture as soon as everything is set up.
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

    /**
     * Reference to the parent activity
     */
    private final Activity parentActivity;

    public CameraPreviewSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.v(LOG_TAG, "CameraPreviewSurface Constructor");

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // FIXME Useful for "backgrounding" ?

        parentActivity = getActivity();

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

        // Temporarily saved here to avoid long calls
        CameraManager cameraManager = RootManager.getInstance().getCameraManager();

        if(cameraManager.isCameraFailing()){
            Log.d(LOG_TAG, "Camera is currently flagged as Failing. "+
                    "Skip the .startPreview() call");
            return;
        }

        try {
            // Set SurfaceView Parameters
            ViewGroup.LayoutParams lp = getLayoutParams();
            Camera.Size bestPreviewSize = cameraManager.getPreviewSize();
            lp.width = bestPreviewSize.width;
            lp.height = bestPreviewSize.height;
            setLayoutParams(lp);
            Log.d(LOG_TAG, "SurfaceView Set - Final preview size: h"+
                    getLayoutParams().height+" w"+getLayoutParams().width);

            // Start Preview
            cameraManager.startPreview(getHolder());
            Log.d(LOG_TAG, "Preview Started");

        } catch(CameraNotReadyException e){
            Log.e(LOG_TAG, "Cannot start the preview: camera not ready! Exception is:", e);

        } finally {
            // Goes straight to surfaceDestroyed, which is surely called just once.
            parentActivity.finish();
        }
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(LOG_TAG, "surfaceDestroyed called");

        // Take the picture
        Log.d(LOG_TAG, "Now shooting the picture...");
        RootManager.getInstance().getCameraManager().shootPicture();

        // Setup the next wakeup and quit everything
        RootManager.getInstance().getTakePictureTriggersManager().setupNextAlarm(parentActivity);

        Log.d(LOG_TAG, "Surface destroyed");
    }

    /**
     * This method is overridden with the sole purpose of providing a NO PIC preview if the camera
     * is down.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Setup some placeholder to highlight how the camera is down
        // TODO Upload fake "CAMERA OFFLINE" pics in case the camera is down

        if(!RootManager.getInstance().getCameraManager().isCameraFailing()) {
            super.onDraw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
            canvas.drawText("NO IMAGE", 70, 120, camFailedPaint);
        }
    }


    /**
     * From
     * https://stackoverflow.com/questions/8276634/android-get-hosting-activity-from-a-view
     * @return the parent activity
     */
    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
