package com.mobilewebcam2.mobilewebcam2.managers;

import android.util.Log;

/**
 * The head of the Manager's hierarchy. Singleton class.
 */
public class RootManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "RootManager";

    private RootSettings rootSettings;

    private RootManager() {
        // FIXME read the actual settings instead!
        Log.w(LOG_TAG, "Failed to read the actual config file! Loading factory "+
                "default settings");
        rootSettings = new RootSettings();
    }

    // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class SingletonHelper{
        private static final RootManager INSTANCE = new RootManager();
    }

    /**
     * Returns the singleton instance of RootManager. It is lazily created.
     * @return the RootManager instance.
     */
    public static RootManager getInstance(){
        return RootManager.SingletonHelper.INSTANCE;
    }

    // FIXME SettingsManager needs this. Isn't any better way to handle this usecase?
    public RootSettings getRootSettings(){
        return rootSettings;
    }

    public ImageManager getImageManager(){
        return rootSettings.getImageManager();
    }

    public CameraManager getCameraManager(){
        return rootSettings.getCameraManager();
    }

    public StorageManager getPictureStorageManager(){
        return rootSettings.getPictureStorageManager();
    }

    public StorageManager getLogStorageManager(){
        return rootSettings.getLogStorageManager();
    }

    public TriggersManager getPictureTriggerManager(){
        return rootSettings.getPictureTriggersManager();
    }

    public SettingsManager getSettingsManager(){
        return rootSettings.getSettingsManager();
    }

    public String getSettingsFilePath() {
        return new String(rootSettings.getSettingsFilePath());
    }

}
