package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "StorageManager";

    /**
     * Utility constants for the storageName value
     */
    @Expose(serialize = false, deserialize = false)
    public static final String STORAGE_LOCAL = "Local Storage (Save on disk)";
    @Expose(serialize = false, deserialize = false)
    public static final String STORAGE_FTP = "FTP Storage (Upload)";
    @Expose(serialize = false, deserialize = false)
    public static final String STORAGE_SOCIALMEDIA = "Social Media Storage (Sharing)";

    @SerializedName("Storage Type")
    private final String storageTypeName;
    @SerializedName("Add the timestamp to the picture name?")
    private final boolean addTimestamp;
    @SerializedName("Add the timestap to the beginning or the end of the name?")
    private boolean timestampAtTheBeginning; // True = Beginning, False = End
    @SerializedName("Format string for the timestamp: ")
    private String timestampFormatString;


    protected StorageManager(String storageTypeName){
        this.storageTypeName = storageTypeName;
        addTimestamp = true;
        timestampAtTheBeginning = false;
        timestampFormatString = "";
    }

    public String getStorageTypeName() { return storageTypeName; }

    protected boolean getAddTimestamp() { return addTimestamp; }

    protected boolean getTimmestampAttheBeginning() { return timestampAtTheBeginning; }

    protected String getTimestampFormatString() { return timestampFormatString; }


    public abstract void storePicture(Bitmap bitmap);


    @Override
    public String toString(){
        String repr = "";
        repr += "\t\tStorage Type: " + this.storageTypeName + "\n";
        repr += "\t\tAdd timestamp to picture name? " + this.addTimestamp + "\n";
        repr += "\t\tAdd timestamp at the beginning of the picture name (if false, at the end)? " + this.timestampAtTheBeginning + "\n";
        repr += "\t\tTimestamp format string: " + this.timestampFormatString + "\n";

        return repr;
    }
}