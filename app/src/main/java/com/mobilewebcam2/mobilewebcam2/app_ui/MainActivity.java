package com.mobilewebcam2.mobilewebcam2.app_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mobilewebcam2.mobilewebcam2.R;
import com.mobilewebcam2.mobilewebcam2.managers.TriggersManager;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Entry point of the application.
 * Keeps a few references and passes them along at need.
 */
public class MainActivity extends Activity {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        TriggersManager.getInstance().startShooting();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    /**
     * FIXME this method might have contained something useful, but surely it needs some refactoring.
     * It was showing the little popups when it shoot a photo in the background.
     * Check MobileWebCam2.onNewIntent() in the legacy code to make sure.
     */
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
    }

}