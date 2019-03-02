package com.mobilewebcam2.mobilewebcam2.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "ImageSettings";

    @SerializedName("Height")
    private final int height;

    @SerializedName("Width")
    private final int width;

    @SerializedName("File Type")
    private final ImageExtension fileType;


    /**
     * If SettingsManager fails to read the settings file,
     * the constructor provides some default values.
     *
     * The class is protected: should be not instantiated by something else than SettingsManager.
     */
    protected ImageSettings(){
        this.height = 480;
        this.width = 640;
        this.fileType = ImageExtension.JPG;
    }


    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public ImageExtension getFileType(){
        return fileType;
    }

    @Override
    public String toString(){
        String repr =  "\n\tImage Settings:\n";
        repr += "\t\theight = " + this.height + "\n";
        repr += "\t\twidth = " + this.width + "\n";
        repr += "\t\tfiletype = " + this.fileType + "\n";
        return repr;
    }

}
