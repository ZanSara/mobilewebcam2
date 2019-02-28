package com.mobilewebcam2.mobilewebcam2.settings;

import com.google.gson.annotations.SerializedName;

/**
 * Root class that holds reference to the subclasses that gather more specific sets of settings.
 *
 * REMEMBER: these setting classes are READ ONLY and IMMUTABLE. Only getters allowed, and they all
 * return copies. If you want the app to be able to modify those values, change them into the
 * settings.json and then re-load it
 */
final public class Settings {

    @SerializedName("Settings File Path")
    private final String settingsFilePath;

    @SerializedName("Picture Settings")
    private final PictureSettings picSettings;

    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     */
    public Settings(){
        settingsFilePath = "/";
        picSettings = new PictureSettings();
    }

    public String getSettingsFilePath() {
        return new String(settingsFilePath);
    }

    public PictureSettings getPicSettings(){
        return picSettings;
    }

    @Override
    public String toString(){
        String repr =  "\n******\nSettings Class:\n";
        repr += "\tSettings File Path: " + this.settingsFilePath;
        repr += picSettings.toString();

        repr += "******\n";
        return repr;
    }
}
