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

    @SerializedName("Image Settings")
    private final ImageSettings imgSettings;

    @SerializedName("Camera Settings")
    private final CameraSettings camSettings;

    @SerializedName("Picture Storage Settings")
    private final StorageSettings picStoSettings;

    @SerializedName("Logs Storage Settings")
    private final StorageSettings logStoSettings;

    @SerializedName("Picture Triggers Settings")
    private final TriggersSettings picTriSettings;

    @SerializedName("Settings File Path")
    private final String settingsFilePath;


    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     */
    protected Settings(){
        imgSettings = new ImageSettings();
        camSettings = new CameraSettings();
        picStoSettings = new LocalStorageSettings();
        logStoSettings = new LocalStorageSettings();
        picTriSettings = new TriggersSettings();
        settingsFilePath = "/";
    }

    public ImageSettings getImageSettings(){
        return imgSettings;
    }

    public CameraSettings getCameraSettings(){
        return camSettings;
    }

    public StorageSettings getPicturesStorageSettings(){
        return picStoSettings;
    }

    public String getSettingsFilePath() {
        return new String(settingsFilePath);
    }


    @Override
    public String toString(){
        String repr =  "\n******\nSettings Class:\n";
        repr += "\n\tImage Settings:\n" + imgSettings.toString();
        repr += "\n\tCamera Settings:\n" + camSettings.toString();
        repr += "\n\tPictures Storage Settings:\n" + picStoSettings.toString();
        repr += "\n\tLogs Storage Settings:\n" + logStoSettings.toString();
        repr += "\n\tPicture Triggers Settings:\n" + picTriSettings.toString();

        repr += "\tSettings File Path: " + this.settingsFilePath + "\n";
        repr += "******\n";
        return repr;
    }
}
