package com.mobilewebcam2.mobilewebcam2.managers;

/**
 * Applies the setting relative to Android itself, like: prevent sleeping, keep screen on/off, etc
 */
public class SystemManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "SystemManager";

    private SystemManager(){
        // TODO instantiate the SystemManager according to default settings
    }

    // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class SingletonHelper{
        private static final SystemManager INSTANCE = new SystemManager();
    }

    /**
     * Returns the singleton instance of the manager. It is lazily created.
     * @return the SystemManager instance.
     */
    public static SystemManager getInstance(){
        return SystemManager.SingletonHelper.INSTANCE;
    }

    /**
     * FIXME
     * Temporary container for the code relative to the FullWakeLock functionality from the
     * legacy app.
     */
    public void toggleFullWakeLock(){
     /*
        if(mSettings.mFullWakeLock)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

       */
    }
}
