package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;

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
        @JsonSubTypes.Type(value = LocalStorageManager.class, name = StorageManager.STORAGE_LOCAL)
        //@JsonSubTypes.Type(value = Bar.class, name = "bar")
})
public abstract class StorageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    @JsonIgnore
    private static final String LOG_TAG = "StorageManager";

    private final SerializableSetting<String> storageTypeName;
    private final SerializableSetting<Boolean> addTimestamp;
    private final SerializableSetting<Boolean> timestampAtTheBeginning;
    private final SerializableSetting<String> timestampFormatString;


    public static final String STORAGE_LOCAL = "Local Storage (Save on disk)";
    public static final String STORAGE_FTP = "FTP Storage (Upload)";
    public static final String STORAGE_SOCIALMEDIA = "Social Media Storage (Sharing)";


    protected StorageManager(String storageType) {
        if( !allowedStorageTypeValues().contains(storageType) ){
            throw new IllegalArgumentException(LOG_TAG+": StorageManager does not have any subclass named "+storageType);
        }

        storageTypeName = new SerializableSetting<>(String.class, "Storage Type",
                STORAGE_LOCAL, STORAGE_LOCAL, null, "Where to store the files",
                null, null, allowedStorageTypeValues());

        addTimestamp = new SerializableSetting<>(Boolean.class, "Add the timestamp to the picture name",
                Boolean.TRUE, Boolean.TRUE, null, "If true, adds the timestamp to the picture name.",
                null, null, Arrays.asList(Boolean.TRUE, Boolean.FALSE));

        timestampAtTheBeginning = new SerializableSetting<>(Boolean.class,
                "Put the timestamp at the beginning of the picture name",
                Boolean.TRUE, Boolean.TRUE, null,
                "If true, adds the timestamp to the beginning of the picture name. " +
                        "If false, it puts it at the end of it. IGNORED IF THE TIMESTAMP IS DISABLED",
                null, null, Arrays.asList(Boolean.TRUE, Boolean.FALSE));

        // FIXME This field will have no JavaScript validation!!!
        timestampFormatString = new SerializableSetting<>(String.class,
                "Timestamp Format", "", "", null,
                "Format string for the timestamp (i.e. YYYY/MM/DD hh:mm:ss). If empty, a " +
                        "regular UNIX timestamp is used.",
                null, null, null);
    }


    public abstract void storePicture(Bitmap bitmap);


    public static List<String> allowedStorageTypeValues(){
        List<String> list = new ArrayList<>();
        list.add(STORAGE_LOCAL);
        list.add(STORAGE_FTP);
        list.add(STORAGE_SOCIALMEDIA);
        return list;
    }


    @Override
    public String toString() {
        String repr = "";
        repr += "\t\tStorage Type: " + storageTypeName.getValue() + "\n";
        repr += "\t\tAdd timestamp to picture name? " + addTimestamp.getValue() + "\n";
        repr += "\t\tAdd timestamp at the beginning of the picture name (if false, at the end)? "
                + timestampAtTheBeginning.getValue() + "\n";
        repr += "\t\tTimestamp format string: " + timestampFormatString.getValue() + "\n";

        return repr;
    }
}