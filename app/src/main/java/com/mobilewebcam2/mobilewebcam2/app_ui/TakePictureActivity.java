package com.mobilewebcam2.mobilewebcam2.app_ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.mobilewebcam2.mobilewebcam2.R;
import com.mobilewebcam2.mobilewebcam2.managers.CameraManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Displays the preview.
 * This Activity also wakes up the phone if it is sleeping.
 *
 * @see CameraPreviewSurface
 */
public class TakePictureActivity extends Activity {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "TakePictureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takepic_layout);

        this.getSystemService(ALARM_SERVICE);

        // Flags required for the activity to wake up the phone at startup
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    /*
     * FIXME this method might have contained something useful, but surely it needs some refactoring.
     * It was showing the little popups when it shoot a photo in the background.
     * Check MobileWebCam2.onNewIntent() in the legacy code to make sure.
     * /
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
    } */


}