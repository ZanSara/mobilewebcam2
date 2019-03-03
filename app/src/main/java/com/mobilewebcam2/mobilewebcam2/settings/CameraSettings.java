package com.mobilewebcam2.mobilewebcam2.settings;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CameraSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "CameraSettings";

    @SerializedName("Orientation (degrees)")
    private final int orientation;
    /**
     * Utility constants for the Orientation value
     */
    @Expose(serialize = false, deserialize = false)
    public static final int IMAGE_ORIENTATION_UPSIDE_DOWN_PORTRAIT = 0;
    @Expose(serialize = false, deserialize = false)
    public static final int IMAGE_ORIENTATION_LANDSCAPE = 90;
    @Expose(serialize = false, deserialize = false)
    public static final int IMAGE_ORIENTATION_PORTRAIT = 180;
    @Expose(serialize = false, deserialize = false)
    public static final int IMAGE_ORIENTATION_UPSIDE_DOWN_LANDSCAPE = 270;

    @SerializedName("If Camera fails to open, retry after (in seconds. 0 to never retry): ")
    private final long retryTime;

    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     *
     * The class is protected: should be not instantiated by something else than SettingsManager.
     */
    protected CameraSettings(){
        this.orientation = IMAGE_ORIENTATION_PORTRAIT;
        retryTime = 0;
    }

    public int getOrientation(){
        return orientation;
    }

    public long getRetryTime(){
        return retryTime;
    }

    @Override
    public String toString(){
        String repr = "\n\tCamera Settings:\n";
        repr += "\t\tOrientation (degrees) = " + this.orientation + "\n";
        repr += "\t\tCamera Opening Retry Time (sec): " + retryTime + "\n";
        return repr;
    }

}
