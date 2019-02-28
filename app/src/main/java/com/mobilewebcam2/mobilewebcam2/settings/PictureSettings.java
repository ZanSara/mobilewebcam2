package com.mobilewebcam2.mobilewebcam2.settings;

import com.google.gson.annotations.SerializedName;

class PictureSettings {

    @SerializedName("Height")
    private final int height;

    @SerializedName("Width")
    private final int width;

    @SerializedName("File Type")
    private final PictureExtension fileType;


    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     *
     * The class is protected: should be not instantiated by something else than SettingsManager.
     */
    protected PictureSettings(){
        this.height = 480;
        this.width = 640;
        this.fileType = PictureExtension.JPG;
    }


    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public PictureExtension getFileType(){
        return fileType;
    }

    @Override
    public String toString(){
        String repr =  "\n\tPicture Settings:\n";
        repr += "\t\theight = " + this.height + "\n";
        repr += "\t\twidth = " + this.width + "\n";
        repr += "\t\tfiletype = " + this.fileType + "\n";
        return repr;
    }

}
