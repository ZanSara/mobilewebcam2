package com.mobilewebcam2.mobilewebcam2.managers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobilewebcam2.mobilewebcam2.app_ui.MainActivity;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Manages timing related functions and manages the triggers logic (day-night, winter,
 * low battery, motion, etc...). It is also the entry point to start the background processes.
 */
public class TriggersManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    @JsonIgnore
    private static final String LOG_TAG = "TriggersManager";


    @JsonProperty("Interval between [event](sec)")
    private final long nextAlarmInterval;


    protected TriggersManager() {
        nextAlarmInterval = 10;
    }


    /**
     * Sets up the Alarms that will tae pictures regularly.
     */
    @JsonIgnore
    public void setupNextAlarm(Activity activity){
        Log.d(LOG_TAG, "Setting up the next Alarm");

        // Get the AlarmManager
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);

        // Create the pending intent
        Intent myIntent = new Intent(activity, MainActivity.AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, myIntent, 0);

        // Sets the next alarm
        Calendar calendar = Calendar.getInstance();
        if(Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() +
                            (nextAlarmInterval*1000), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() +
                            (nextAlarmInterval*1000), pendingIntent);
        }

    }

    @Override
    public String toString(){
        String repr = "";
        repr += "\t\tTime to the next alarm: " + nextAlarmInterval + "sec.\n";

        return repr;
    }

}
