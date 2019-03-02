package com.mobilewebcam2.mobilewebcam2.managers;

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
     * @return the CameraManager instance.
     */
    public static ImageManager getInstance(){
        return ImageManager.SingletonHelper.INSTANCE;
    }


    /**
     * FIXME Copypasted from MobileWebCam2. No idea if it is useful or what does it do really.
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
