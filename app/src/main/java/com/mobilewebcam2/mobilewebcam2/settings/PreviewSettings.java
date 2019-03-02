package com.mobilewebcam2.mobilewebcam2.settings;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviewSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "PreviewSettings";

    @SerializedName("Minimum Preview Margin (between 0 and 1)")
    private double minimumPreviewMargin;

    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     *
     * The class is protected: should be not instantiated by something else than SettingsManager.
     */
    protected PreviewSettings(){
        this.minimumPreviewMargin = 0;
    }

    /**
     * Returns the minimum margin (in percentage) that the camera preview should have.
     * @return a value between 0 and 1 (percentage)
     */
    public double getMinimumPreviewMargin(){
        return this.minimumPreviewMargin;
    }

    @Override
    public String toString(){
        String repr =  "\n\tPreview Settings:\n";
        repr += "\t\tMinimum Preview Margin: " + this.minimumPreviewMargin + "\n";
        return repr;
    }

}
