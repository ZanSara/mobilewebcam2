package com.mobilewebcam2.mobilewebcam2.managers.storage;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.mobilewebcam2.mobilewebcam2.settings.StorageSettings;

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
    private static final String LOG_TAG = "LocalStorageManager";

    protected LocalStorageManager(){}

    @Override
    public String getStorageTypeName(){
        return StorageSettings.STORAGE_LOCAL;
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

}
