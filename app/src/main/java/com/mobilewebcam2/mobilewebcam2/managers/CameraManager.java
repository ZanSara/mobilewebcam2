package com.mobilewebcam2.mobilewebcam2.managers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobilewebcam2.mobilewebcam2.SerializableSetting;
import com.mobilewebcam2.mobilewebcam2.exceptions.CameraNotReadyException;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Applies camera configurations, according to user settings. It's a singleton.
 *
 * NOTE: this application is MEANT TO MISBEHAVE with the camera locks: in particular, it will
 * acquire or retain the camera lock even when pushed in the background.
 */
// TODO remember to perform tests without giving camera permissions to the app.
@JsonIgnoreProperties(ignoreUnknown = true)
public class CameraManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    @JsonIgnore
    private static final String LOG_TAG = "CameraManager";

    private final SerializableSetting<Integer> cameraId;
    private final SerializableSetting<Double> previewMargin;
    private final SerializableSetting<Long> retryTime;
    private final SerializableSetting<Integer> afterShootingWaitingTime;


    // TODO those 'transient' are required because of Jackson. Maybe there is a better solution.
    /**
     * The last opened camera reference. It can **always** be null!!
     */
    @JsonIgnore
    private transient Camera camera;
    @JsonIgnore
    private boolean cameraHasFailed = false;
    @JsonIgnore
    private transient Camera.PictureCallback pictureCallback;
    @JsonIgnore
    private int cameraOrientation = -1; // Default value for 'unchecked yet'


    /**
     * Opens the camera to try assessing its orientation, and sets up the picture callback.
     */
    protected CameraManager() {
        this.cameraId = new SerializableSetting<>(Integer.class, 1, "Camera ID", 0, 0, null,
                "ID of the camera to use. In most phones, 0 means back camera, and 1 means front "+
                "camera. Check for your phone model if this does not hold, of if your phone has "+
                "more than 2 cameras.", Integer.MAX_VALUE, 0, null, SerializableSetting.SettingType.REGULAR);

        this.previewMargin = new SerializableSetting<>(Double.class, 100, "Preview Margin", 10.0, 10.0,
                "%", "Margin between the phone screen size and the preview. Does not affect the "+
                "picture take in any way: useful only for the user to see the preview better.",
                100.0, 0.0, null, SerializableSetting.SettingType.ADVANCED);

        this.retryTime = new SerializableSetting<>(Long.class, 101,"After Failure Retry Time", 0l, 0l,
                "milliseconds", "After the camera has failed to open or to shoot "+
                "a picture, time to wait before retrying to open the camera. Set 0 to never retry.",
                Long.MAX_VALUE, 0l, null, SerializableSetting.SettingType.ADVANCED);

        this.afterShootingWaitingTime = new SerializableSetting<>(Integer.class, 102,
                "After Shooting Waiting Time", 2000, 2000, "milliseconds",
                "Time to wait after the camera has shot the picture. Highly device depended. "+
                "It should never be higher than the interval between shooting pictures. The default "+
                "value is usually good, change this at your own risk.", Integer.MAX_VALUE, 0, null,
                SerializableSetting.SettingType.ADVANCED);

        // Set what to do once the picture is taken
        pictureCallback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.v(LOG_TAG, "Picture taken successfully. Going to post-processing");
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                RootManager.getInstance().getImageManager().postProcessImage(bitmap);
            }
        };
    }

    /**
     * Opens the camera with all the due checks.
     * If this.camera is not null, it returns without doing anything.
     *
     * @throws RuntimeException if opening the camera fails (for example, if the camera is in use
     *                          by another process or device policy manager has disabled the camera).
     */
    private void openCamera() throws CameraNotReadyException {
        if (camera != null) {
            Log.w(LOG_TAG, "Someone called .openCamera(), but `camera` was not null. "+
                    "Closing and reopening it.");
            closeCamera();
        }

        Log.v(LOG_TAG, "Opening camera ID: " + cameraId);
        try {
            if (Build.VERSION.SDK_INT < 9) {
                if (cameraId.getValue() != 0) {
                    Log.i(LOG_TAG, "CameraManager.openCamera() was called with a nonzero " +
                            "camera_id, but the phone runs SDK version " + Build.VERSION.SDK_INT +
                            ". Support for multiple cameras was introduced in SDK 9");
                }
                camera = Camera.open();
            } else {
                camera = Camera.open(cameraId.getValue());
            }

            if (camera == null) {
                // https://developer.android.com/reference/android/hardware/Camera.html#open()
                // --> returns null if the phone have no camera
                Log.e(LOG_TAG, "Camera failed to open, but threw no exception. "+
                        "The phone may have NO CAMERA. **** PLEASE DEBUG *****");
                throw new CameraNotReadyException(this, "Camera failed to open, but threw no "+
                        "exception. **** PLEASE DEBUG *****");
            }

            // TODO check if this code is useful to anything
            camera.setErrorCallback(new Camera.ErrorCallback() {
                @Override
                public void onError(int error, Camera failed_camera) {
                    Log.e(LOG_TAG, "Camera threw an error (code " + error +
                            "). Trying to release and close camera.");
                    closeCamera(failed_camera);
                }
            });

            Log.v(LOG_TAG, "Camera opened successfully");

        } catch (RuntimeException e) {
            // TODO Retry after the given retry interval has passed.
            throw new CameraNotReadyException(this, "[originated by " + LOG_TAG +
                    "] A RuntimeException occurred trying to open the camera. Make sure the app"+
                    " has permission to access the camera. Exception is: ", e);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Unexpected exception in openCamera()! ", e);
            throw new CameraNotReadyException(this, "Unexpected exception in openCamera()!"+
                    " Exception is: ", e);
        }
    }

    /**
     * Releases and closes the camera. Removes its reference.
     *
     * @see CameraManager#closeCamera(Camera)
     */
    public void closeCamera() {
        this.closeCamera(this.camera);
        this.camera = null;
        Log.v(LOG_TAG, "Camera closed successfully");
    }

    /**
     * Releases and closes the given camera.
     *
     * @param camera the camera to close.
     */
    private void closeCamera(Camera camera) {
        if (camera == null) {
            Log.w(LOG_TAG, "Someone triggered the private CameraManager.closeCamera(camera) on a null camera. This shouldn't happen. Doing nothing.");
            return;
        }
        camera.stopPreview();
        camera.setPreviewCallback(null);
        camera.release();
        Log.v(LOG_TAG, "Camera" + camera.toString() + "was released successfully");
    }

    /**
     * If the camera is open, its instance should be not null.
     * This method is used to make sure everything is alright before calling camera methods (but
     * remember, they can also fail later)
     *
     * @return true if the camera instance is not null.
     */
    public boolean isCameraOpen() {
        return (camera != null);
    }

    /**
     * Returns True if last attempt to open the camera has failed.
     *
     * @return true if the camera failed to open at the last attempt.
     */
    public boolean isCameraFailing() {
        if (cameraHasFailed) {
            Log.d(LOG_TAG, "Camera is flagged as failing!");
        }
        return cameraHasFailed;
    }

    /**
     * Switch the cameraFailing flag to True. Usually used by CameraNotReadyException
     */
    public void cameraFailed() {
        cameraHasFailed = true;
    }

    /**
     * This method inspects the camera parameters and returns the best preview size.
     * NOTE: it will open the camera if needed, but won't close it.
     *
     * @return Camera.Size object containing height and width values
     * @throws CameraNotReadyException if the camera cannot be opened or its data accessed
     * @see "https://developer.android.com/reference/android/hardware/Camera.Size.html"
     */
    public Camera.Size getPreviewSize() throws CameraNotReadyException {

        if (camera == null) {
            Log.w(LOG_TAG, "CameraManager.getBestPreviewSize() was called without any " +
                    "camera being open. Calling openCamera() before proceeding.");
            openCamera();
        }

        // Get native camera size (the biggest among the available ones)
        List<Camera.Size> availableSizes = camera.getParameters().getSupportedPictureSizes();
        Camera.Size nativeSize = availableSizes.get(0);
        for (Camera.Size size : availableSizes) {
            if (size.height * size.width > nativeSize.height * nativeSize.width) {
                nativeSize = size;
            }
        }
        Log.d(LOG_TAG, "Preview Sizing - Native camera Size: h" + nativeSize.height +
                " w" + nativeSize.width);

        // Calculating margins - FIXME remove only 50px of supposed navigation bar height
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = screenWidth - (int) (screenWidth * (previewMargin.getValue()/100));
        screenHeight = screenHeight - (int) (screenHeight * (previewMargin.getValue()/100));

        Camera.Size screenSize = camera.new Size(screenWidth, screenHeight);
        Log.d(LOG_TAG, "Preview Sizing - Screen Size: : h" + screenSize.height +
                " w" + screenSize.width + " (margin is " + previewMargin.getValue() + "%)");

        // Calculating scale
        float xScale = ((float) screenSize.width) / nativeSize.width;
        float yScale = ((float) screenSize.height) / nativeSize.height;
        float scale = Math.min(xScale, yScale);

        return camera.new Size((int) (nativeSize.width * scale), (int) (nativeSize.height * scale));

    }

    /**
     * Setup the camera for the preview and starts it.
     *
     * @throws CameraNotReadyException if camera.setPreviewDisplay() throws IOException.
     * @see com.mobilewebcam2.mobilewebcam2.app_ui.CameraPreviewSurface
     */
    public void startPreview(SurfaceHolder previewHolder) throws CameraNotReadyException {
        if (previewHolder == null) {
            throw new IllegalArgumentException("previewHolder cannot be null (I think)");
        }
        if (camera == null) {
            Log.v(LOG_TAG, "CameraManager.startPreview() was called without any open camera. Opening camera.");
            openCamera();
        }

        // Sets the preview display
        try {
            // camera.reconnect();
            camera.setPreviewDisplay(previewHolder);

        } catch (IOException e) {
            Log.e(LOG_TAG, "An error occurred setting the CameraPreviewSurface as Preview Display for the camera. Exception is: ", e);
            throw new CameraNotReadyException(this, "An error occurred setting the CameraPreviewSurface as Preview Display for the camera.");
        }

        if (Build.VERSION.SDK_INT < 9) {
            setCameraDisplayOrientation(); // private method
        }
        camera.startPreview();
    }


    /**
     * Makes the preview and the camera orientation match. Useful especially for front cameras.
     * Slightly modified from https://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation%28int%29
     */
    private void setCameraDisplayOrientation() {

        if (Build.VERSION.SDK_INT < 9) {
            // TODO can I workaround this limitation somehow? Rotating the surfaceview and the image maybe?
            Log.w(LOG_TAG, "Camera Orientation settings are not available for Android SDK < 9");
            return;
        }

        if(cameraOrientation == -1){ // Then we did not check for its value yet
            checkCameraOrientation();
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId.getValue(), info);

        final int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            //result = (360 - (info.orientation + degrees) % 360) % 360;  // original (wrong?) line
            result = (360 - (cameraOrientation) % 360) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (cameraOrientation + 360) % 360;
        }
        camera.setDisplayOrientation(result);

        Log.d(LOG_TAG, "Camera Orientation set to " + result + "º\nNote that, in this case, info.orientation says " + info.orientation + "º");

    }

    /**
     * Utility that verifies whether the camera has any orientation we should be aware of
     */
    private void checkCameraOrientation (){
        // Verify the camera orientation using its native aspect ratio.
        //      Not perfect: pictures can be upside down, it's not verified.
        try {
            openCamera();
            Camera.Size pictureSize = camera.getParameters().getSupportedPictureSizes().get(0);
            cameraOrientation = ((pictureSize.width > pictureSize.height) ? 0 : 90);
        } catch (CameraNotReadyException e) {
            Log.w(LOG_TAG, "CameraManager could not assess the orientation of the camera. "+
                    "Exception is: ", e);
            cameraOrientation = 0;
        }
    }

    /**
     * Shoots a picture.
     * The method is synchronized, because it is called from an AsyncTask.
     * <p>
     * WARNING: it can fail due to the camera being offline. This is not considered an exception,
     * but a misconfiguration, and even if it does not make the app fail, it should be notified with
     * high priority.
     *
     * @see CameraManager#openCamera()
     */
    public synchronized void shootPicture() {
        // FIXME is shooting fails due to camera offline, it should be notified with high priority

        try {
            if (camera == null) {
                // Shouldn't happen, but can be fixed
                Log.i(LOG_TAG, "CameraManager.shootPicture was called before the camera was ready. Quickly setting it up.");
                openCamera();
                setCameraDisplayOrientation();
            }
            //camera.stopPreview();
            camera.startPreview();
            camera.takePicture(null, null, pictureCallback);
            Log.i(LOG_TAG, "Picture taken (at " + DateFormat.getDateTimeInstance().format(new Date()) + ")");

            try {
                Thread.sleep(afterShootingWaitingTime.getValue());
            } catch (InterruptedException e) {
                Log.w(LOG_TAG, "Interrupted while sleeping after shooting a picture");
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "CameraManager.shootPicture failed! Exception is: ", e);
            // Passing NULL tells the ImageManager to load the NOCAM image.
            RootManager.getInstance().getImageManager().postProcessImage(null);
        }
    }


    /**
     * Switches on and off the camera, thus preventing any more picture to be taken EVEN IF the
     * triggers activate.
     */
    public void toggleCameraOffline() {
        // TODO
    }

    /**
     * Setup the camera according to a new set of parameters. Useful to switch, for example, between
     * night and day mode (or any other user-defined set of parameters).
     */
    public void loadCameraMode() {
        // TODO
    }


    public void selectCameraToUse() {
        // TODO select the camera to use (effective if the phone has more than one)
    }

    /**
     * Sets the camera zoom
     * Some code can be found at MobileWebCam2 in the legacy (in the switch case MENU_SET_ZOOM)
     */
    @JsonIgnore
    public void setZoom() {
        // TODO
    }

    /**
     * Sets the camera exposure
     * Some code can be found at MobileWebCam2 in the legacy (in the switch case MENU_SET_EXPOSURE)
     */
    @JsonIgnore
    public void setExposure() {
        // TODO
    }

    /**
     * Sets the camera white balance
     * Some code can be found at MobileWebCam2 in the legacy (in the switch case MENU_SET_WHITEBALANCE)
     */
    @JsonIgnore
    public void setWhiteBalance() {
        // TODO
    }

    /**
     * Sets the camera's flash mode (never, always, at need, etc)
     */
    @JsonIgnore
    public void setFlash() {
        // TODO
    }



    @Override
    public String toString(){
        String repr = "";
        repr += cameraId;
        repr += previewMargin;
        repr += retryTime;
        repr += afterShootingWaitingTime;
        return repr;
    }


}

