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


    private StorageManager() {
        // TODO
    }

    // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class SingletonHelper{
        private static final StorageManager INSTANCE = new StorageManager();
    }

    /**
     * Returns the singleton instance of the manager. It is lazily created.
     * @return the CameraManager instance.
     */
    public static StorageManager getInstance(){
        return StorageManager.SingletonHelper.INSTANCE;
    }

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


    /**
     * FIXME brutally copied from Preview. Check if contains anything worth.
     */
    public static void sharePicture() { /*(Context c, PhotoSettings s, byte[] data) {
        if(c == null)
        {
            MobileWebCam2.LogE("Unable to share picture: Context is null!");
            return;
        }

        // sharePictureNow = false;

        FileOutputStream out = null;
        try {
            out = c.openFileOutput("current.jpg", Context.MODE_WORLD_READABLE);
            out.write(data);
            out.flush();
            out.close();

            if(s.mStoreGPS)
                ExifWrapper.addCoordinates("file:///data/data/com.mobilewebcam2.mobilewebcam2/files/current.jpg", WorkImage.gLatitude, WorkImage.gLongitude, WorkImage.gAltitude);

            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///data/data/com.mobilewebcam2.mobilewebcam2/files/current.jpg"));
            c.startActivity(Intent.createChooser(shareIntent, "Share picture ...").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(OutOfMemoryError e)
        {
            e.printStackTrace();
        }

        MobileWebCam2.gInSettings = false;
        */
    }

}
