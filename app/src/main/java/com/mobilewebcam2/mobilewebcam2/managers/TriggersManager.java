package com.mobilewebcam2.mobilewebcam2.managers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.mobilewebcam2.mobilewebcam2.app_ui.MainActivity;
import com.mobilewebcam2.mobilewebcam2.app_ui.TakePictureActivity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.ALARM_SERVICE;

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
     * Sets up the Alarms that will tae pictures regularly.
     */
    public void setupShootingAlarm(Activity activity){
        Log.d(LOG_TAG, "Setting up the alarm for the next picture");

        // Get the AlarmManager
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);

        // Create the pending intent
        Intent myIntent = new Intent(activity, MainActivity.AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, myIntent, 0);

        // Sets the next timer
        Calendar calendar = Calendar.getInstance();
        if(Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + (10*1000), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + (10*1000), pendingIntent);
        }

    }

}
