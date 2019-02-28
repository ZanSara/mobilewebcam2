package com.mobilewebcam2.mobilewebcam2.managers;

/**
 * Applies camera configurations, according to user settings.
 */
public class CameraManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "CameraManager";

    /**
     * Shoots a picture.
     *
     * WARNING: it can fail due to the camera being offline. This is not considered an exception,
     * but a misconfiguration, and even if it does not make the app fail, it should be notified with
     * high priority.
     */
    public void shootPicture() {
        // FIXME is shooting fails due to camera offline, it should be notified with high priority
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
}
