package com.mobilewebcam2.mobilewebcam2.settings;


/**
 * Implementation of StorageSettings that stores the pictures on the device's disk
 */
public class LocalStorageSettings extends StorageSettings {

    protected LocalStorageSettings(){
        super(StorageSettings.STORAGE_LOCAL);
    }

}
