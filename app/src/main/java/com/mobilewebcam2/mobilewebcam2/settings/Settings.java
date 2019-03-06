package com.mobilewebcam2.mobilewebcam2.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Root class that holds reference to the subclasses that gather more specific sets of settings.
 *
 * REMEMBER: these setting classes are READ ONLY and IMMUTABLE. Only getters allowed, and they all
 * return copies. If you want the app to be able to modify those values, change them into the
 * settings.json and then re-load it
 */
public class Settings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "Settings";

    @SerializedName("Settings File Path")
    private final String settingsFilePath;

    @SerializedName("Image Settings")
    private final ImageSettings imgSettings;

    @SerializedName("Camera Settings")
    private final CameraSettings camSettings;

    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     */
    protected Settings(){
        settingsFilePath = "/";
        imgSettings = new ImageSettings();
        camSettings = new CameraSettings();
    }

    public String getSettingsFilePath() {
        return new String(settingsFilePath);
    }

    public ImageSettings getImageSettings(){
        return imgSettings;
    }

    public CameraSettings getCameraSettings(){
        return camSettings;
    }

    @Override
    public String toString(){
        String repr =  "\n******\nSettings Class:\n";
        repr += "\tSettings File Path: " + this.settingsFilePath + "\n";
        repr += imgSettings.toString();
        repr += camSettings.toString();

        repr += "******\n";
        return repr;
    }
}
