package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;
import android.util.Pair;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mobilewebcam2.mobilewebcam2.SerializableSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles all the kinds of storage of the pictures: in the memory/SD, over FTP, or
 * by sharing them, according to user settings.
 *
 * Other parts of the application should be unaware of how the pictures are stored.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = LocalStorageManager.class, name = StorageManager.STORAGE_LOCAL),
        @JsonSubTypes.Type(value = FtpStorageManager.class, name = StorageManager.STORAGE_FTP),
        @JsonSubTypes.Type(value = SocialMediaStorageManager.class, name = StorageManager.STORAGE_SOCIALMEDIA)
})
public abstract class StorageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    @JsonIgnore
    private static final String LOG_TAG = "StorageManager";

    @JsonIgnore
    private final SerializableSetting<String> storageType;

    private final SerializableSetting<Boolean> addTimestamp;
    private final SerializableSetting<Boolean> timestampAtTheBeginning;
    private final SerializableSetting<String> timestampFormatString;


    public static final String STORAGE_LOCAL = "Save in the gallery";
    public static final String STORAGE_FTP = "Upload to Server";
    public static final String STORAGE_SOCIALMEDIA = "Share on Social Media";


    protected StorageManager(String storageType) {
        if( !allowedStorageTypeValues().contains(storageType) ){
            throw new IllegalArgumentException(LOG_TAG+": StorageManager does not have any subclass named "+storageType);
        }

        this.storageType = new SerializableSetting<>(String.class, 10, "Storage Type",
                STORAGE_LOCAL, STORAGE_LOCAL, null, "Where to store the files",
                null, null, allowedStorageTypeValues(),
                SerializableSetting.SettingCategory.REGULAR);

        this.addTimestamp = new SerializableSetting<>(Boolean.class, 20, "Add timestamp",
                Boolean.TRUE, Boolean.TRUE, null, "If true, adds the timestamp to the file name.",
                null, null, Arrays.asList(Boolean.TRUE, Boolean.FALSE),
                SerializableSetting.SettingCategory.REGULAR);

        this.timestampAtTheBeginning = new SerializableSetting<>(Boolean.class, 22,
                "Timestamp at the beginning",
                Boolean.TRUE, Boolean.TRUE, null,
                "If true, adds the timestamp to the beginning of the file name. " +
                        "If false, it puts it at the end of it. IGNORED IF THE TIMESTAMP IS DISABLED",
                null, null, Arrays.asList(Boolean.TRUE, Boolean.FALSE),
                SerializableSetting.SettingCategory.REGULAR);

        // FIXME add regex validation in the allowedValues field
        this.timestampFormatString = new SerializableSetting<>(String.class, 24,
                "Timestamp Format", "", "", null,
                "Format string for the timestamp (i.e. YYYY/MM/DD hh:mm:ss). If empty, a " +
                        "regular UNIX timestamp is used.",
                null, null, null,
                SerializableSetting.SettingCategory.REGULAR);
    }


    public abstract void storePicture(Bitmap bitmap);


    public static List<String> allowedStorageTypeValues(){
        List<String> list = new ArrayList<>();
        list.add( STORAGE_LOCAL );
        list.add( STORAGE_FTP );
        list.add( STORAGE_SOCIALMEDIA );
        return list;
    }

    public static List<StorageManager> existingSubclasses(){
        List<StorageManager> list = new ArrayList<>();
        list.add( new LocalStorageManager() );
        list.add( new FtpStorageManager() );
        list.add( new SocialMediaStorageManager() );
        return list;
    }

    @Override
    public String toString() {
        String repr = "";
        repr += storageType;
        repr += addTimestamp;
        repr +=timestampAtTheBeginning;
        repr += timestampFormatString;

        return repr;
    }
}