package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Applies image-specific settings, like scaling & cropping, post-processing, color alteration,
 * imprints. NOT RESPONSIBLE FOR STORING THE PICTURE IN THE FILESYSTEM.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageManager  extends MWCSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    //@Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "ImageManager";

    /**
     * Internal Settings class, to be serialized.
     */
    @JsonProperty("Settings")
    private InternalSettings internalSettings = new InternalSettings();

    // Jackson has trouble with non static inner classes
    // http://cowtowncoder.com/blog/archives/2010/08/entry_411.html
    @JsonPropertyOrder(alphabetic=true)
    static private class InternalSettings {

        @JsonProperty("Height")
        private final int height;
        @JsonProperty("Width")
        private final int width;
        @JsonProperty("File Type")
        private final ImageExtension fileType;

        InternalSettings() {
            this.height = 480;
            this.width = 640;
            this.fileType = ImageExtension.JPG;
        }

    }

    protected ImageManager() { }

    /**
     * Called on every picture taken. Applies all required editing before saving the image on disk.
     * If a NULL is passed, assumes camera had failed somewhere and loads a NOCAM picture
     */
    public void postProcessImage(Bitmap bitmap) {
        if(bitmap == null) {
            Log.v(LOG_TAG, "postProcessImage() received a null bitmap, therefore camera "+
                    "must have failed. Uploading the NOCAM default image.");
            // Loads a NOCAM image
        }
        Log.d(LOG_TAG, "post-processing the picture");

        // TODO actually post-process it, if needed.

        RootManager.getInstance().getPictureStorageManager().storePicture(bitmap);
    }



    /**
     * FIXME Copypasted from the legacy MobileWebCam. No idea if it is useful or what does it do really.
     */
    static public void decodeYUV420SPGrayscale(int[] rgb, byte[] yuv420sp, int width, int height)
    {
        final int frameSize = width * height;
        for (int pix = 0; pix < frameSize; pix++) {
            int pixVal = (0xff & ((int) yuv420sp[pix])) - 16;
            if (pixVal < 0) pixVal = 0;
            if (pixVal > 255) pixVal = 255;
            rgb[pix] = 0xff000000 | (pixVal << 16) | (pixVal << 8) | pixVal;
        }
    }



    @Override
    public String toString(){
        String repr =  "";
        repr += "\t\tHeight: " + internalSettings.height + "\n";
        repr += "\t\tWidth: " + internalSettings.width + "\n";
        repr += "\t\tFile Type: " + internalSettings.fileType + "\n";
        return repr;
    }

    /**
     * List of all the extensione an image can have.
     */
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
}
