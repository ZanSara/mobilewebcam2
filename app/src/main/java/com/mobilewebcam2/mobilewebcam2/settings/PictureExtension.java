package com.mobilewebcam2.mobilewebcam2.settings;

public enum PictureExtension {

    JPG(".jpg"),
    PNG(".png"),
    GIF(".gif");

    private String extensionString;

    PictureExtension(String ext){
        extensionString = ext;
    }

    public String getExtension(){
        return extensionString;
    }

    @Override
    public String toString(){
        return extensionString;
    }

}
