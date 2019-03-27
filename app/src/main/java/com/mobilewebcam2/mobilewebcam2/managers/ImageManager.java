package com.mobilewebcam2.mobilewebcam2.managers;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Applies image-specific settings, like scaling & cropping, post-processing, color alteration,
 * imprints. NOT RESPONSIBLE FOR STORING THE PICTURE IN THE FILESYSTEM.
 */
public class ImageManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "ImageManager";

    private ImageManager() {
        // TODO
    }

    // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class SingletonHelper{
        private static final ImageManager INSTANCE = new ImageManager();
    }

    /**
     * Returns the singleton instance of the manager. It is lazily created.
     * @return the ImageManager instance.
     */
    public static ImageManager getInstance(){
        return ImageManager.SingletonHelper.INSTANCE;
    }

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

        StorageManager.getInstance().storePicture();
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
}
