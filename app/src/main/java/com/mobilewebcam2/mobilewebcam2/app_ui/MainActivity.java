package com.mobilewebcam2.mobilewebcam2.app_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobilewebcam2.mobilewebcam2.R;
import com.mobilewebcam2.mobilewebcam2.managers.TriggersManager;


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
        setContentView(R.layout.layout);

        // Button Listener Setup
        final Button button = findViewById(R.id.goBackground);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TriggersManager.getInstance().goBackground();
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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