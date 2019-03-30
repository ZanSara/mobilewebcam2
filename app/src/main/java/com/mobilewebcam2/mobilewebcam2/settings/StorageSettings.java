package com.mobilewebcam2.mobilewebcam2.settings;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Base class for Storage related settings. See implementations.
 *
 */
public abstract class StorageSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "StorageSettings";

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

    protected StorageSettings(String storageTypeName){
        this.storageTypeName = storageTypeName;
    }

    public String getStorageTypeName() { return storageTypeName; }


    @Override
    public String toString(){
        String repr = "";
        repr += "\t\tStorage Type = " + this.storageTypeName + "\n";
        return repr;
    }

}
