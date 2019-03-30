package com.mobilewebcam2.mobilewebcam2.app_ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.mobilewebcam2.mobilewebcam2.R;

import java.text.DateFormat;
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

        // Flags required for the activity to wake up the phone at startup
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }
}