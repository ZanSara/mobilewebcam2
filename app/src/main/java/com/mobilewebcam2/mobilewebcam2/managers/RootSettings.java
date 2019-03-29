package com.mobilewebcam2.mobilewebcam2.managers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Root class for the hierarchy of managers.
 * Mainly useful for serialization purposes.
 */
public class RootSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "RootSettings";

    @SerializedName("Image Settings")
    private final ImageManager imageManager;

    @SerializedName("Camera Settings")
    private final CameraManager cameraManager;

    @SerializedName("Picture Storage Settings")
    private final StorageManager pictureStorageManager;

    @SerializedName("Log Storage Settings")
    private final StorageManager logStorageManager;

    @SerializedName("Camera Upload Triggers Settings")
    private final TriggersManager pictureTriggersManager;

    @SerializedName("General Settings")
    private final SettingsManager setManager;

    // FIXME it may merge with the above
    @SerializedName("Settings File Path")
    private final String settingsFilePath;


    protected RootSettings() {
        settingsFilePath = "/";
        imageManager = new ImageManager();
        cameraManager = new CameraManager();
        pictureStorageManager = new LocalStorageManager();
        logStorageManager = new LocalStorageManager();
        pictureTriggersManager = new TriggersManager();
        setManager = new SettingsManager();
    }


    public String getSettingsFilePath() {
        return new String(settingsFilePath);
    }

    public ImageManager getImageManager(){
        return imageManager;
    }

    public CameraManager getCameraManager(){
        return cameraManager;
    }

    public StorageManager getPictureStorageManager(){
        return pictureStorageManager;
    }

    public StorageManager getLogStorageManager(){
        return logStorageManager;
    }

    public TriggersManager getPictureTriggersManager(){
        return pictureTriggersManager;
    }

    public SettingsManager getSettingsManager() { return setManager; }

    @Override
    public String toString(){
        String repr =  "\n******\nSettings Overview:\n";
        repr += "\n\tImage Settings:\n" + imageManager.toString();
        repr += "\n\tCamera Settings:\n" +cameraManager.toString();
        repr += "\n\tPictures Storage Settings:\n" +pictureStorageManager.toString();
        repr += "\n\tLogs Storage Settings:\n" +logStorageManager.toString();
        repr += "\n\tPicture Triggers Settings:\n" +pictureTriggersManager.toString();
        repr += "\n\tGeneral Settings:\n" +setManager.toString();
        repr += "\n\tSettings File Path: " + this.settingsFilePath + "\n";
        repr += "******\n";
        return repr;
    }
}
