package com.mobilewebcam2.mobilewebcam2.managers;

/**
 * Handles all the kinds of storage of the pictures: in the memory/SD, over FTP, or
 * by sharing them, according to user settings.
 *
 * Other parts of the application should be unaware of how the pictures are stored.
 */
public class StorageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "StorageManager";

    public void storePicture(){
        // TODO implement me
    }


    private void socialMediaStorage() {
        // TODO implement me: share picture using system API
    }

    private void ftpStorage(){
        // TODO implement me: send picture over FTP
    }

    private void localStorage(){
        // TODO implement me: store picture in the local filesystem
    }

}
