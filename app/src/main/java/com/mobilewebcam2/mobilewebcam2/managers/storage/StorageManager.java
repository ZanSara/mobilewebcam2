package com.mobilewebcam2.mobilewebcam2.managers.storage;

import android.graphics.Bitmap;

import com.mobilewebcam2.mobilewebcam2.settings.SettingsManager;
import com.mobilewebcam2.mobilewebcam2.settings.StorageSettings;

/**
 * Handles all the kinds of storage of the pictures: in the memory/SD, over FTP, or
 * by sharing them, according to user settings.
 *
 * Other parts of the application should be unaware of how the pictures are stored.
 *
 */

public abstract class StorageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "StorageManagerInterface";

    private static StorageManager storageManager;

    protected StorageManager() { }


    public abstract String getStorageTypeName();
    public abstract void storePicture(Bitmap bitmap);


    /**
     * Returns the proper instance of the manager, according to the settings. It is lazily created.
     *
     * Has some overhead due to the subclassing: it checks at every call whether the settings
     * have changed (and therefore the manager has to be changed)
     *
     * @return the proper manager instance.
     */
    public static StorageManager getInstance() {

        String storageType = SettingsManager.getInstance().getStoS().getStorageTypeName();

        if(storageManager != null && storageManager.getStorageTypeName().equals(storageType)){
            return storageManager;
        } else {
            // Instantiate it
            switch (storageType){
                case StorageSettings.STORAGE_LOCAL:
                    storageManager = new LocalStorageManager();
                    break;
                case StorageSettings.STORAGE_FTP:
                    //storageManager = new LocalStorageManager();
                    break;
                case StorageSettings.STORAGE_SOCIALMEDIA:
                    //storageManager = new LocalStorageManager();
                    break;
                default:
                    storageManager = new LocalStorageManager();
                    break;
            }
            return storageManager;
        }

    }

}