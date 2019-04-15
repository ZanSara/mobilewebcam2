package com.mobilewebcam2.mobilewebcam2.managers;

import com.mobilewebcam2.mobilewebcam2.SerializableSetting;

public final class SettingsManager { // extends MWCSettings

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "SettingsManager";


    private final SerializableSetting<String> configFilePath;


    protected SettingsManager() {
        // FIXME add validation with regex for this field
        configFilePath = new SerializableSetting<>(String.class, "Settings File Path",
                "/sdcard/mwc2_config.json", "/mobilewebcam2/config.json", null,
                "Where the app will download and store the latest copy of this configuration file.",
                null, null, null);
    }

    public String getConfigFilePath(){
        return configFilePath.getValue();
    }


    @Override
    public String toString(){
        String repr = "";
        repr += configFilePath;

        return repr;
    }
}
