package com.mobilewebcam2.mobilewebcam2.settings;

public enum ImageExtension {

    JPG(".jpg"),
    PNG(".png"),
    GIF(".gif");

    private String extensionString;

    ImageExtension(String ext){
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
