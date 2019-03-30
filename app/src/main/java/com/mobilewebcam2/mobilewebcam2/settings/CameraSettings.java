package com.mobilewebcam2.mobilewebcam2.settings;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CameraSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "CameraSettings";

    @SerializedName("Camera ID")
    private final int cameraId;

    @SerializedName("Preview Margin (0% - 100%)")
    private final double previewMargin;

    @SerializedName("If Camera fails to open, retry after (in seconds. 0 to never retry): ")
    private final long retryTime;

    @SerializedName("Time to wait after the picture is shot (ms)")
    private final int afterShootingWaitingTime;

    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     *
     * The class is protected: should be not instantiated by something else than SettingsManager.
     */
    protected CameraSettings(){
        this.cameraId = 0; // FIXME Good in most phones, but note that in some, only ID=1 exists!
        this.previewMargin = 0.1;
        this.retryTime = 0;
        this.afterShootingWaitingTime = 2000;
    }

    public int getCameraId() { return cameraId; }

    public double getPreviewMargin() {return previewMargin; }

    public long getRetryTime(){ return retryTime; }

    public int getAfterShootingWaitingTime(){ return afterShootingWaitingTime; }

    @Override
    public String toString(){
        String repr = "";
        repr += "\t\tCamera ID = " + this.cameraId + "\n";
        repr += "\t\tPreview Margin = " + this.previewMargin*100 + "%\n";
        repr += "\t\tCamera Opening Retry Time = " + retryTime + "s\n";
        repr += "\t\tAfter Shooting Waiting Time = " + afterShootingWaitingTime + "ms\n";
        return repr;
    }

}
