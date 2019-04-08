package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobilewebcam2.mobilewebcam2.SerializableSetting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Implementation of StorageManagerInterface that saves the image on disk
 */
public class LocalStorageManager extends StorageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    @JsonIgnore
    private static final String LOG_TAG = "LocalStorageManager";

    private final SerializableSetting<Boolean> externalStorage;
    private final SerializableSetting<String> path;


    protected LocalStorageManager(){
        super(StorageManager.STORAGE_LOCAL);

        externalStorage = new SerializableSetting<>(Boolean.class, "Save on external storage?",
                true, true, "",
                "Whether to use the external storage or not. Yes = save on "+
                "the SD, No = save on the internal memory", null, null,
                Arrays.asList(Boolean.TRUE, Boolean.FALSE));

        // FIXME point this to the phone gallery or something similar
        // FIXME add regex validation in the allowedValues field
        path = new SerializableSetting<>(String.class, "Picture Folder",
                "mobilewebcam2_folder", "mobilewebcam2_folder", "",
                "Path to the folder to save the files in.",
                null, null, null);

    }

    @Override
    public void storePicture(Bitmap bitmap){

        String folderPath = Environment.getExternalStorageDirectory().toString();
        File picturePath = new File(folderPath, "examplePicture" +
                DateFormat.getDateTimeInstance().format(new Date()) +".png");
        Log.d(LOG_TAG, "Storing "+picturePath);

        try (FileOutputStream outputStream = new FileOutputStream(picturePath)) {

            // PNG is a lossless format, the compression factor (100) is ignored
            // FIXME make this stuff configurable
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();

        } catch (IOException e) {
            Log.e(LOG_TAG, "I/O Exception! Make sure the app has permission to "+
                    "write on the local storage. Exception is", e);
        }
    }


    @Override
    public String toString(){
        String repr = super.toString();
        repr += "\t\tSave on external storage? "+ externalStorage+"\n";
        repr += "\t\tPath to save pictures in: "+ path+"\n";
        return repr;
    }

}
