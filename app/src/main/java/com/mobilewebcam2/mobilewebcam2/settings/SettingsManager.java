package com.mobilewebcam2.mobilewebcam2.settings;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;

/**
 * Main configuration manager. It takes care of managing the configurations file:
 *  - Pulls from server according to its own settings
 *  - Marshals and unmarshals with GSON
 *  - Gives access to inner config classes at request (keep references and provides getters)
 *  It is a singleton.
 */
public final class SettingsManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "SettingsManager";

    private Settings settings;

    private SettingsManager() {
        this.settings = new Settings();

        // FIXME decide how to make the path to settings.json configurable
        // this.settings = this.readSettingsFile();
    }

    // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class SingletonHelper{
        private static final SettingsManager INSTANCE = new SettingsManager();
    }

    /**
     * Returns the singleton instance of SettingsManager. It is lazily created.
     * @return the SettingsManager instance.
     */
    public static SettingsManager getInstance(){
        return SingletonHelper.INSTANCE;
    }


    /**
     * Returns the private Settings objects, with the values loaded from settings.json.
     * REMEMBER: Do not save this instance. It may be replaced any time by a new one.
     * @return the latest Settings object.
     */
    public Settings getSettings() {
        return this.settings;
    }

    /**
     * Shortcut to access Camera's settings
     */
    public CameraSettings getCaS(){
        return this.settings.getCameraSettings();
    }

    /**
     * Shortcut to access Images's settings
     */
    public ImageSettings geImS(){
        return this.settings.getImageSettings();
    }

    /**
     * Shortcut to access Picture Storage's settings
     */
    public StorageSettings getPicStoS(){
        return this.settings.getPicturesStorageSettings();
    }


    private Settings readSettingsFile(){
        try {
            File file = new File(settings.getSettingsFilePath()); // FIXME decide how to make the path to settings.json configurable
            CharSource source = Files.asCharSource(file, Charsets.UTF_8);
            return readSettingsJSON(source.read());

        } catch (Exception e) {
            // FIXME Log this issue and notify the user
            e.printStackTrace();
            return new Settings();
        }
    }
    /**
     * Reads settings file into a string and instantiates the hierarchy of Settings objects.
     *
     * If the read fails for any reason, a new Settings class is instantiated with its hard coded,
     * default values.
     *
     * @return reference to the root Settings object
     */
    public Settings readSettingsJSON(String settings) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(settings);
        Gson gson = new Gson();
        return gson.fromJson(jsonElement, Settings.class);
    }

    /*
     * FIXME Test code - REMOVE

    public static String writeConfigFile() {

        Settings config = new Settings();
        ImageSettings picConfig = config.getPicSettings();
        picConfig.height = 1;
        picConfig.width = 2;
        picConfig.fileType = ImageExtension.GIF;

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        return gson.toJson(config);

    }

    public static Settings readConfigFile(String jsonfile) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonfile);
        //String eventClassName = jsonElement.getAsJsonObject().get("eventClassName").getAsString();
        Gson gson = new Gson();
        return gson.fromJson(jsonElement, Settings.class);
    }

    public static void main(String[] args){
        String jf = writeConfigFile();
        Settings decoded = readConfigFile(jf);
        System.out.println(decoded);
    }

     */

    public String writeConfigFile() {

        Settings config = new Settings();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        return gson.toJson(config);

    }

    public static void main(String[] args){
        SettingsManager s = new SettingsManager();
        String sf = s.writeConfigFile();
        System.out.println(s.readSettingsJSON(sf));
    }
}
