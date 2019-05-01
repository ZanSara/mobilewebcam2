package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobilewebcam2.mobilewebcam2.SerializableSetting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Implementation of StorageManagerInterface that saves the image on disk
 */
public class FtpStorageManager extends StorageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    @JsonIgnore
    private static final String LOG_TAG = "FtpStorageManager";

    private final SerializableSetting<String> serverAddress;
    private final SerializableSetting<String> serverPort;


    protected FtpStorageManager(){
        super(StorageManager.STORAGE_LOCAL);

        // FIXME point this to the phone gallery or something similar
        // FIXME add regex validation in the allowedValues field
        serverAddress = new SerializableSetting<>(String.class, 110, "Server Address",
                "127.0.0.1", "127.0.0.1", "",
                "IP address of the FTP server where the files will be uploaded",
                null, null, null,
                SerializableSetting.SettingType.REGULAR);

        serverPort = new SerializableSetting<>(String.class, 120, "Server Port",
                "8000", "8000", "",
                "ID of the FTP port of the server where the files will be uploaded",
                null, null, null,
                SerializableSetting.SettingType.REGULAR);

    }

    @Override
    public void storePicture(Bitmap bitmap) {
        Log.e(LOG_TAG, "****** storePicture NOT IMPLEMENTED YET *****");
    }


    @Override
    public String toString(){
        String repr = super.toString();
        repr += serverAddress;
        return repr;
    }

}
