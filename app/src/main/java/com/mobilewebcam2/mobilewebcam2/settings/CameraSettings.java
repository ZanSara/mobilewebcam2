package com.mobilewebcam2.mobilewebcam2.settings;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CameraSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "CameraSettings";

    @SerializedName("If Camera fails to open, retry after (in seconds. 0 to never retry): ")
    private final long retryTime;

    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     *
     * The class is protected: should be not instantiated by something else than SettingsManager.
     */
    protected CameraSettings(){
        retryTime = 0;
    }

    public long getRetryTime(){
        return retryTime;
    }

    @Override
    public String toString(){
        String repr = "\n\tCamera Settings:\n";
        repr += "\t\tCamera Opening Retry Time (sec): " + retryTime + "\n";
        return repr;
    }

}
