package com.mobilewebcam2.mobilewebcam2.managers;

import android.view.WindowManager;

/**
 * Applies the setting relative to Android itself, like: prevent sleeping, keep screen on/off, etc
 */
public class SystemManager {

    /**
     * FIXME
     * Temporary container for the code relative to the FullWakeLock functionality from the
     * legacy app.
     */
    public void toggleFullWakeLock(){
     /*
        if(mSettings.mFullWakeLock)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

       */
    }

    /**
     * FIXME May be completely unnecessary
     */
    public void setLastMotionKeepAliveTime(long time){
        // FIXME trivially adapted from the original legacy code in MobileWebCam2, onResume()
    }

}
