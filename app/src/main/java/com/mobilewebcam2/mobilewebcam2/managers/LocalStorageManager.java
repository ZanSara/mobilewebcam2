package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Implementation of StorageManagerInterface that saves the image on disk
 */
public class LocalStorageManager extends StorageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    //@Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "LocalStorageManager";

    /**
     * Internal Settings class, to be serialized.
     */
    @JsonProperty("Settings")
    private InternalSettings internalSettings = new InternalSettings();

    @JsonPropertyOrder(alphabetic=true)
    private static class InternalSettings {
        @JsonProperty("Save on external storage? (Yes = on SD, No = on the internal memory)")
        private final boolean externalStorage;
        @JsonProperty("Folder to save the picture in: ")
        private final String path;

        protected InternalSettings(){
            externalStorage = true;
            path = ""; // FIXME point this to the phone gallery or something similar
        }
    }

    protected LocalStorageManager(){
        super(StorageManager.STORAGE_LOCAL);
    }

    @Override
    public void storePicture(Bitmap bitmap){

        String folderPath = Environment.getExternalStorageDirectory().toString();
        File picturePath = new File(folderPath, "examplePicture"+new Date().getTime() +".png");
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
        repr += "\t\tSave on external storage? "+ internalSettings.externalStorage+"\n";
        repr += "\t\tPath to save pictures in: "+ internalSettings.path+"\n";
        return repr;
    }

}
