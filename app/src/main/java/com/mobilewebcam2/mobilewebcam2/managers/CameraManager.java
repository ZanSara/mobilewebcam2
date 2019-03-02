package com.mobilewebcam2.mobilewebcam2.managers;

import android.content.res.Resources;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.mobilewebcam2.mobilewebcam2.exceptions.CameraNotReadyException;
import com.mobilewebcam2.mobilewebcam2.settings.SettingsManager;

import java.io.IOException;
import java.util.List;

/**
 * Applies camera configurations, according to user settings. It's a singleton.
 *
 * NOTE: this application is MEANT TO MISBEHAVE with the camera locks: in particular, it will
 * acquire or retain the camera lock even when pushed in the background.
 */
// TODO remember to perform tests without giving camera permissions to the app.
public class CameraManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "CameraManager";

    /**
     * Open camera reference. It can **always** be null!!
     */
    private Camera camera;


    private CameraManager() {
        // TODO instantiate the CameraManager according to default settings
        try {
            openCamera();
        } catch (CameraNotReadyException e){
            Log.e(LOG_TAG, "CameraManager failed to open the camera at creation. Camera is null. Exception is: ", e);
        }
    }

    // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class SingletonHelper{
        private static final CameraManager INSTANCE = new CameraManager();
    }

    /**
     * Returns the singleton instance of the manager. It is lazily created.
     * @return the CameraManager instance.
     */
    public static CameraManager getInstance(){
        return CameraManager.SingletonHelper.INSTANCE;
    }

    /**
     * If the camera is open, its instance should be not null.
     * This method is used to make sure everything is alright before calling camera methods (but
     * remember, they can also fail later)
     * @return true if the camera instance is not null.
     */
    public boolean isCameraOpen(){
        return (camera != null);
    }

    /**
     * Setup the camera for the preview.
     * @throws IOException if camera.setPreviewDisplay() does.
     * @see com.mobilewebcam2.mobilewebcam2.app_ui.CameraPreviewSurface
     */
    public void startPreview(SurfaceHolder previewHolder) throws CameraNotReadyException {
        if(camera == null){
            openCamera();
        }
        try {
            camera.setPreviewDisplay(previewHolder);

        } catch (IOException e) {
            Log.e(LOG_TAG, "An error occurred setting the CameraPreviewSurface as Preview Display for the camera. Exception is: ", e);
            throw new CameraNotReadyException(this, "An error occurred setting the CameraPreviewSurface as Preview Display for the camera.");
        }
        Camera.Parameters parameters = camera.getParameters();
        parameters.getSupportedPreviewSizes();
        camera.setParameters(parameters);
        camera.startPreview();
    }

    /**
     * This method inspects the camera parameters and returns the best preview size.
     * NOTE: it will open the camera if needed, but won't close it.
     *
     * @return Camera.Size object containing height and width values
     * @throws CameraNotReadyException if the camera cannot be opened or its data accessed
     * @see "https://developer.android.com/reference/android/hardware/Camera.Size.html"
     */
    public Camera.Size getBestPreviewSize() throws CameraNotReadyException {

        if(camera == null){
            Log.w(LOG_TAG, "CameraManager.getBestPreviewSize() was called without any camera being open. Calling openCamera() before proceeding.");
            openCamera();
        }
        List<Camera.Size> supportedSizes = camera.getParameters().getSupportedPreviewSizes();
        if(supportedSizes.size() <= 0){
            Log.e(LOG_TAG, "openCamera threw no exception, but camera.getParameters().getSupportedPreviewSizes() returned an empty list. ***** PLEASE DEBUG *****");
            supportedSizes.add(camera.new Size(320, 240));
        }
        Log.v(LOG_TAG, "Supported Preview Sizes: ");
        for(int p=0; p<supportedSizes.size(); p++){
            Log.v(LOG_TAG, "w" + supportedSizes.get(p).width + " h" + supportedSizes.get(p).height);
        }

        // FIXME The image from the camera will be always oriented in landscape. Is it ok?
        // It finds the smalled dimension (width or height), because it must fit the picture's width.
        // Keeps the custom margins into consideration.
        int minDimension = Math.min(    Resources.getSystem().getDisplayMetrics().widthPixels,
                Resources.getSystem().getDisplayMetrics().heightPixels );
        minDimension = (int)(minDimension * (1 - SettingsManager.getInstance().getPrS().getMinimumPreviewMargin()));

        Camera.Size bestSize = supportedSizes.get(0); // Provides a fail-safe value in case of problems.
        for(int i=0; i < supportedSizes.size(); i++){
            if(supportedSizes.get(i).width < minDimension && supportedSizes.get(i).width > bestSize.width){
                bestSize = supportedSizes.get(i);
            }
        }
        return bestSize;
    }

    /**
     * Shoots a picture.
     *
     * WARNING: it can fail due to the camera being offline. This is not considered an exception,
     * but a misconfiguration, and even if it does not make the app fail, it should be notified with
     * high priority.
     */
    public void shootPicture() {
        // FIXME is shooting fails due to camera offline, it should be notified with high priority
        // After taking a picture, preview display will have stopped. To take more photos, call startPreview() again first.
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
    public void setZoom() {
        // TODO
    }

    /**
     * Sets the camera exposure
     * Some code can be found at MobileWebCam2 in the legacy (in the switch case MENU_SET_EXPOSURE)
     */
    public void setExposure() {
        // TODO
    }

    /**
     * Sets the camera white balance
     * Some code can be found at MobileWebCam2 in the legacy (in the switch case MENU_SET_WHITEBALANCE)
     */
    public void setWhiteBalance() {
        // TODO
    }

    /**
     * Sets the camera's flash mode (never, always, at need, etc)
     */
    public void setFlash() {
        // TODO
    }


    /**
     * Opens the camera with all the due checks.
     * If this.camera is not null, it returns without doing anything.
     *
     * @throws RuntimeException if opening the camera fails (for example, if the camera is in use
     * by another process or device policy manager has disabled the camera).
     */
    private void openCamera() throws CameraNotReadyException {
        if(camera != null){
            Log.w(LOG_TAG, "Someone called .openCamera(), but `camera` was not null (that means, it was already opened). Doing nothing.");
            return;
        }
        try {
            camera = Camera.open();

        } catch(RuntimeException e){
            // TODO Retry after the given retry interval has passed.
            throw new CameraNotReadyException(this, "[originated by " + LOG_TAG + "] A RuntimeException occurred trying to open the camera. Make sure the app has permission to access the camera. Exception is: ", e);
        }
        Log.v(LOG_TAG, "Camera opened successfully");

        camera.setErrorCallback(new Camera.ErrorCallback() {
                @Override
                public void onError(int error, Camera failed_camera) {
                    Log.e(LOG_TAG, "Camera threw an error (code " + error + "). Trying to release and close camera.");
                    closeCamera(failed_camera);
                }
        });

    }

    /**
     * Releases and closes the camera. Removes its reference.
     * @see CameraManager#closeCamera(Camera)
     */
    public void closeCamera() {
        this.closeCamera(this.camera);
        this.camera = null;
        Log.v(LOG_TAG, "Camera closed successfully");
    }

    /**
     * Releases and closes the given camera.
     * @param camera the camera to close.
     */
    private void closeCamera(Camera camera){
        if(camera == null) {
            Log.w(LOG_TAG, "Someone triggered the private CameraManager.closeCamera(camera) on a null camera. This shouldn't happen. Doing nothing.");
            return;
        }
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        Log.v(LOG_TAG, "Camera" + camera.toString() + "was released successfully");
    }

    /** FIXME brutally copied from Preview. Check if contains anything worth
     */
    private void tryOpenCam() { /*
        Log.i("MobileWebCam2", "Preview.tryOpenCam()");

        if(mCamera == null)
        {
            try
            {
                            // This part was covered
                                        if(mCamera == null)
                                        {
                                            Log.i("MobileWebCam2", "Camera.open()");
                                            mCamera = Camera.open();
                                        }
                                        if(mCamera != null)
                                        {
                                            Log.i("MobileWebCam2", "mCamera.setErrorCallback()");
                                            mCamera.setErrorCallback(new Camera.ErrorCallback()
                                            {
                                                @Override
                                                public void onError(int error, Camera camera)
                                                {
                                                    if(error != 0) // Samsung Galaxy S returns 0? https://groups.google.com/forum/?fromgroups=#!topic/android-developers/ZePJqveaExk
                                                    {
                                                        MobileWebCam2.LogE("Camera error: " + error);
                                                        if(mCamera != null)
                                                        {
                                                            mCamera = null;
                                                            camera.setPreviewCallback(null);
                                                            camera.stopPreview();
                                                            camera.release();

                                                            System.gc();
                                                        }

                                                        mPhotoLock.set(false);
                                                        Log.v("MobileWebCam2", "PhotoLock released!");
                                                        JobFinished();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    catch(RuntimeException e)
                                    {
                                        e.printStackTrace();
                                        if(e.getMessage() != null)
                                        {
                                            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            MobileWebCam2.LogE(e.getMessage());
                                        }
                                        else
                                        {
                                            Toast.makeText(mActivity, "No access to camera!", Toast.LENGTH_SHORT).show();
                                            MobileWebCam2.LogE("No access to camera!");
                                        }
                                        mCamera = null;
                                        mPhotoLock.set(false);
                                        Log.v("MobileWebCam2", "PhotoLock released!");
                                        JobFinished();
                                    }

                                    if(mCamera != null)
                                    {
                                        Log.i("MobileWebCam2", "mCamera.setPreviewDisplay()");
                                        try {
                                            mCamera.setPreviewDisplay(mHolder);
                                        } catch (IOException exception) {
                                            mCamera.release();
                                            mCamera = null;
                                            if(exception.getMessage() != null)
                                            {
                                                Toast.makeText(mActivity, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                                MobileWebCam2.LogE(exception.getMessage());
                                            }
                                            else
                                            {
                                                Toast.makeText(mActivity, "No camera preview!", Toast.LENGTH_SHORT).show();
                                                MobileWebCam2.LogE("No camera preview!");
                                            }
                                            mPhotoLock.set(false);
                                            Log.v("MobileWebCam2", "PhotoLock released!");
                                            JobFinished();
                                        }

                // Now that the size is known, set up the camera parameters and begin
                // the preview.
                if(mCamera != null)
                {
                    // wait for zoom (required at least on Galaxy Camera 2)
                    if(mSettings.mZoom > 0)
                    {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    Camera.Parameters parameters = mCamera.getParameters();
                    if(parameters != null)
                    {
                        parameters.set("orientation", "landscape");
                        mCamera.setParameters(parameters);
                        mSettings.SetCameraParameters(parameters, false);
                        try
                        {
                            mCamera.setParameters(parameters);
                        }
                        catch(RuntimeException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    //***			setCameraDisplayOrientation(MobileWebCam2.this, 0, mCamera);

                    if(mSettings.mMode != Mode.HIDDEN)
                    {
                        // show preview window
                        try
                        {
                            mCamera.startPreview();
                        }
                        catch(RuntimeException e)
                        {
                            if(e.getMessage() != null)
                                MobileWebCam2.LogE(e.getMessage());
                            else
                                e.printStackTrace();
                        }
                    }

                    if(mSettings.mMotionDetect)
                    {
                        Log.i("MobileWebCam2", "mCamera.setPreviewCallback()");
                        mCamera.setPreviewCallback(new PreviewCallback() {
                            @Override
                            public void onPreviewFrame(byte[] data, Camera camera)
                            {
                                if(mPreviewChecker != null)
                                {
                                    int d = (mPreviewChecker.mDataLockIdx + 1) % mPreviewChecker.DATACOUNT;
                                    if(mPreviewChecker.mData[d] == null)
                                        mPreviewChecker.mData[d] = new byte[data.length];
                                    System.arraycopy(data, 0, mPreviewChecker.mData[d], 0, data.length);
                                    if(MobileWebCam2.DEBUG_MOTIONDETECT && mActivity.mDrawOnTop != null)
                                        mActivity.mDrawOnTop.invalidate();
                                }
                            }
                        });

                        mPreviewChecker = new Preview.PreviewImageChecker();
                        mPreviewChecker.start();
                    }
                }
            }
        }
        */
    }
}
