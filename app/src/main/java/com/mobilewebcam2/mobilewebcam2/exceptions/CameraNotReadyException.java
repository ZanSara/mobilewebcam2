package com.mobilewebcam2.mobilewebcam2.exceptions;

import com.mobilewebcam2.mobilewebcam2.managers.CameraManager;

/**
 * Thrown if CameraManager#openCamera() fails, or if CameraManager#camera is null.
 * NOTE: it will try to close the camera every time it is thrown, and set cameraFailed to True.
 */
public class CameraNotReadyException extends Exception {

    public CameraNotReadyException(CameraManager cm, String s){
        super(s);
        cm.cameraFailed();
        cm.closeCamera();
    }

    public CameraNotReadyException(CameraManager cm, String s, Throwable t){
        super(s, t);
        cm.cameraFailed();
        cm.closeCamera();
    }

}
