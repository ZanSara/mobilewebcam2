package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Implementation of StorageManagerInterface that shares the image on social media
 */
public class SocialMediaStorageManager extends StorageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    @JsonIgnore
    private static final String LOG_TAG = "SocialM.StorageManager";


    protected SocialMediaStorageManager(){
        super(StorageManager.STORAGE_SOCIALMEDIA);
    }

    @Override
    public void storePicture(Bitmap bitmap) {
        Log.e(LOG_TAG, "****** storePicture NOT IMPLEMENTED YET *****");
    }


    @Override
    public String toString(){
        String repr = super.toString();
        return repr;
    }

}
