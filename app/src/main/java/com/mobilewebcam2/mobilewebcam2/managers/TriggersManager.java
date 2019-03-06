package com.mobilewebcam2.mobilewebcam2.managers;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages timing related functions and manages the triggers logic (day-night, winter,
 * low battery, motion, etc...). It is also the entry point to start the background processes.
 */
public class TriggersManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "TriggersManager";

    private TriggersManager() {
        // TODO
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
     * Sets up all the recurring alarms. Entry point to go background.
     */
    public void goBackground(){
        Log.d(LOG_TAG, "************* Going in background *************");
        setupShootingAlarm();
    }

    /**
     * Sets up the Alarms that will take pictures regularly.
     */
    private void setupShootingAlarm(){
        Log.d(LOG_TAG, "Setting up the Alarms to shoot pictures");

    }








    private Handler handler;
    private Timer shootingTimer;
    /**
     * OLD CODE Sets up the main timer that shoots pictures. Called by CameraPreviewSurface at creation.
     */
    public void startShooting(){
        // Setup the timed events:
        shootingTimer = new Timer();
        shootingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //CameraManager.getInstance().shootPicture();
                        new ShootPicture().execute();
                    }
                });
            }
        },1000,5000);
    }


    private class ShootPicture extends AsyncTask<Void, Void, Void> {
        private final String LOG_TAG = "ShootPicture [AsyncT]";
        @Override
        protected Void doInBackground(Void... params) {
            Log.v(LOG_TAG,"Shooting");
            CameraManager.getInstance().shootPicture();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        }
    }


}
