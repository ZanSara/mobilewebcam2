/* Copyright 2012 Michael Haar

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.mobilewebcam2.mobilewebcam2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


public class MainActivity extends Activity {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "MainActivity";

    public static final String SHARED_PREFS_NAME="mobile_webcam_2";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Makes the app fullscreen TODO is this needed??
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout);

        /* TODO Preview related code: what does this do?
        PhotoSettings mSettings = new PhotoSettings(CamActivity.this);
        Preview preview = (Preview)findViewById(R.id.preview);
        preview.SetSettings(mSettings);
        mSettings.EnableMobileWebCam(mSettings.mCameraStartupEnabled);
        */
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // FIXME trivially adapted from the original legacy code. May be completely unnecessary
        // systemManager.setLastMotionKeepAliveTime(System.currentTimeMillis());
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