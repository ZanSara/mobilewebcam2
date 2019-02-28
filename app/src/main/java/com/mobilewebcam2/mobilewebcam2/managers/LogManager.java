package com.mobilewebcam2.mobilewebcam2.managers;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Compiles reports and logs at need.
 *
 * TODO: make sure this class is really needed, or eventually rename to ReportsManager
 */
public class LogManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "LogManager";

    /* TODO
     * Understand what does this method do.
     * Seems to be an utility to format the log in a more readable way.
     * Moved from Mainactivity
     */
    public static String GetLog(Context c, SharedPreferences prefs){//, PhotoSettings settings) {
        /*
        String info_device = android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE + " " + android.os.Build.DISPLAY;

        String log = "MainActivity " + MobileWebCamHttpServer.getVersionNumber(c) + " (" + settings.mImprintText + ") " + info_device + "\r\n\r\n";

        log += "Date and time: " + new Date().toString() + "\r\n";

        log += WorkImage.getBatteryInfo(c, "Battery %d%% %.1f\248C") + "\r\n";

        if(settings.mStoreGPS || settings.mImprintGPS)
        {
            String lat = String.format(Locale.US, "%f", WorkImage.gLatitude);
            String lon = String.format(Locale.US, "%f", WorkImage.gLongitude);
            String alt = String.format(Locale.US, "%f", WorkImage.gAltitude);
            log += "Latitude: " + lat + "\r\n";
            log += "Longitude: " + lon + "\r\n";
            log += "Altitude: " + alt + "\r\n";
            log += "http://maps.google.com/maps?q=" + lat + "," + lon + "+(MainActivity+Location)&z=18&ll=" + lat + "," + lon + "\r\n";
        }

        if(prefs.getBoolean("server_enabled", false))
        {
            String myIP = RemoteControlSettings.getIpAddress(c, false);
            if(myIP != null)
                log += "Browser access URL: http://" + myIP + ":" + MobileWebCamHttpService.getPort(prefs) + "\r\n";
        }

        log += "\r\n";

        if(settings.mMode == Mode.MANUAL)
            log += "Pictures: " + MainActivity.gPictureCounter + "    Uploading: " + MainActivity.gUploadingCount + "   Manual Mode active" + "\r\n";
        else
            log += "Pictures: " + MainActivity.gPictureCounter + "    Uploading: " + MainActivity.gUploadingCount + "\r\n";
        log += "Orientation: " + Preview.gOrientation + "\r\n";;
        if(settings.mMode == Mode.MANUAL)
            log += "Mode: " + c.getResources().getStringArray(R.array.entries_list_camera_mode)[0];
        else if(settings.mMode == Mode.NORMAL)
            log += "Mode: " + c.getResources().getStringArray(R.array.entries_list_camera_mode)[1];
        else if(settings.mMode == Mode.BACKGROUND)
            log += "Mode: " + c.getResources().getStringArray(R.array.entries_list_camera_mode)[2];
        else if(settings.mMode == Mode.HIDDEN)
            log += "Mode: " + c.getResources().getStringArray(R.array.entries_list_camera_mode)[3];
        if(settings.mMotionDetect)
            log += " detect motion";
        if(settings.mNightAutoConfig && settings.mNightAutoConfigEnabled)
            log += " night detected";
        if(settings.mNightAutoBrightness && settings.IsNight())
            log += " autobright";
        if(settings.mNightIRLight)
            log += " IR";
        if(settings.mBroadcastReceiver.length() > 0)
            log += "\r\nCustom Reiver: " + settings.mBroadcastReceiver + " " + MainActivity.gCustomReceiverActive;
        if(!settings.mNightAutoBrightness || !settings.IsNight())
            log += String.format("\r\nWhite Balance: %s, Color Effect: %s, Scene Mode: %s, Exposure Compensation: %d", settings.mWhiteBalance, settings.mColorEffect, settings.mSceneMode, settings.mExposureCompensation);
        else
            log += String.format("\r\nWhite Balance: %s, Color Effect: %s, Scene Mode: %s, Exposure Compensation: %d", settings.mNightAutoBrightnessWhitebalance, settings.mColorEffect, settings.mNightAutoBrightnessScenemode, settings.mNightAutoBrightnessExposure);
        float usedMegs = (float) Debug.getNativeHeapAllocatedSize() / (float)1048576L;
        log += String.format("\r\nMemory used: %.2f MB\r\n", usedMegs);
        log += String.format("Used upload image size: %d x %d (from %d x %d)\r\n", MobileWebCamHttpService.gImageWidth, MobileWebCamHttpService.gImageHeight, MobileWebCamHttpService.gOriginalImageWidth, MobileWebCamHttpService.gOriginalImageHeight);

        log += "\r\nActivity:\r\n";

        int cnt = 0;
        int i = MainActivity.gCurLogInfos;
        while(cnt < MainActivity.gLogInfos.length)
        {
            if(MainActivity.gLogInfos[i] != null)
                log += MainActivity.gLogInfos[i] + "\r\n";
            i++;
            if(i >= MainActivity.gLogInfos.length)
                i = 0;
            cnt++;
        }

        log += "\r\nErrors:\r\n";

        cnt = 0;
        i = MainActivity.gCurLogMessage;
        while(cnt < MainActivity.gLogMessages.length)
        {
            if(MainActivity.gLogMessages[i] != null)
                log += MainActivity.gLogMessages[i] + "\r\n";
            i++;
            if(i >= MainActivity.gLogMessages.length)
                i = 0;
            cnt++;
        }

        return log;
        */
        return "";
    }

}
