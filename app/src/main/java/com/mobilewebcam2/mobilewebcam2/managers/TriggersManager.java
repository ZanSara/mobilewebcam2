package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages timing related functions and manages the triggers logic (day-night, winter,
 * low battery, motion, etc...).
 */
public class TriggersManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "TriggersManager";

    private Handler handler;
    private Timer shootingTimer;

    private TriggersManager() {
        handler = new Handler();
    }

    // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class SingletonHelper{
        private static final TriggersManager INSTANCE = new TriggersManager();
    }

    /**
     * Returns the singleton instance of the manager. It is lazily created.
     * @return the TriggersManager instance.
     */
    public static TriggersManager getInstance(){
        return TriggersManager.SingletonHelper.INSTANCE;
    }

    /**
     * Sets up the main timer that shoots pictures. Called by CameraPreviewSurface at creation.
     */
    public void startShooting(){
        // Setup the timed events:
        shootingTimer = new Timer();
        shootingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        CameraManager.getInstance().shootPicture();
                    }
                });
            }
        },1000,1000);
    }
}
