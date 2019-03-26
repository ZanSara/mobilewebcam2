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
    }

    public int getCameraId() { return cameraId; }

    public double getPreviewMargin() {return previewMargin; }

    public long getRetryTime(){ return retryTime; }

    @Override
    public String toString(){
        String repr = "\n\tCamera Settings:\n";
        repr += "\t\tCamera ID = " + this.cameraId + "\n";
        repr += "\t\tPreview Margin = " + this.previewMargin*100 + "%\n";
        repr += "\t\tCamera Opening Retry Time = " + retryTime + "s\n";
        return repr;
    }

}
