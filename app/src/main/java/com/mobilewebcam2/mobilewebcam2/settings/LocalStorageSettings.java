package com.mobilewebcam2.mobilewebcam2.settings;


import com.google.gson.annotations.Expose;

/**
 * Implementation of StorageSettings that stores the pictures on the device's disk
 */
public class LocalStorageSettings extends StorageSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "StorageSettings";

    protected LocalStorageSettings(){
        super(StorageSettings.STORAGE_LOCAL);
    }

}
