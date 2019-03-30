package com.mobilewebcam2.mobilewebcam2.app_ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mobilewebcam2.mobilewebcam2.R;
import com.mobilewebcam2.mobilewebcam2.managers.TriggersManager;
import com.mobilewebcam2.mobilewebcam2.settings.LocalStorageSettings;
import com.mobilewebcam2.mobilewebcam2.settings.Settings;
import com.mobilewebcam2.mobilewebcam2.settings.SettingsManager;

import java.text.DateFormat;
import java.util.Date;


/**
 * Displays the preview.
 * Useful for debugging and eventually setting up the application at the beginning.
 */
public class MainActivity extends Activity {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage_layout);


        // Button Listener Setup
        final Button button = findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TriggersManager.getInstance().setupShootingAlarm(MainActivity.this);
                finish();
            }
        });


        SettingsManager s = SettingsManager.getInstance();
        String sf = s.writeConfigFile();
        Log.d(LOG_TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        Log.d(LOG_TAG, "sf: "+sf);
        Log.d(LOG_TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        Log.d(LOG_TAG, "Generated Tree: " + s.readSettingsJSON(sf));
        Log.d(LOG_TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

    }


    /**
     * This inner class receives the alarm and triggers the TakePictureActivity intent.
     *
     * NOTE: for some reason, it works only if it's here. Moving it into TakePictureActivity
     * will fail to trigger the onReceive method. FIXME investigate
     */
    public static class AlarmReceiver extends BroadcastReceiver {

        private final static String LOG_TAG = "AlarmReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "AlarmReceiver has been triggered (at "+
                    DateFormat.getDateTimeInstance().format(new Date()) +")");

            Intent takePicture = new Intent(context, TakePictureActivity.class);
            takePicture.addCategory(Intent.CATEGORY_HOME);
            takePicture.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(takePicture);
        }
    }

}