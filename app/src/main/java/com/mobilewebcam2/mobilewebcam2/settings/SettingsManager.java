package com.mobilewebcam2.mobilewebcam2.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mobilewebcam2.mobilewebcam2.R;

import java.io.File;

/**
 * This class is the main configuration manager.
 *
 * It takes care of managing the configurations file:
 *  - Pulls from server according to its own settings
 *  - Marshals and unmarshals with GSON
 *  - Gives access to inner config classes at request (keep references and provides getters)
 */
public final class SettingsManager {

    private Settings settings;

    private SettingsManager() {
        this.settings = new Settings();

        // FIXME decide how to make the path to settings.json configurable
        // this.settings = this.readSettingsFile();
    }

    public Settings getSettings() {
        return this.settings;
    }

    /**
     * Reads settings file into a string and instantiates the hierarchy of Settings objects.
     *
     * If the read fails for any reason, a new Settings class is instantiated with its hard coded,
     * default values.
     *
     * @return reference to the root Settings object
     */
    private Settings readSettingsFile() {
        try {
            File file = new File(settings.getSettingsFilePath()); // FIXME decide how to make the path to settings.json configurable
            CharSource source = Files.asCharSource(file, Charsets.UTF_8);
            String jsonString = source.read();

            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(jsonString);
            Gson gson = new Gson();
            return gson.fromJson(jsonElement, Settings.class);

        } catch (Exception e) {
            // FIXME Log this issue and notify the user
            e.printStackTrace();
            return new Settings();
        }
    }

    /*
     * FIXME Test code - REMOVE

    public static String writeConfigFile() {

        Settings config = new Settings();
        PictureSettings picConfig = config.getPicSettings();
        picConfig.height = 1;
        picConfig.width = 2;
        picConfig.fileType = PictureExtension.GIF;

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

    public static String writeConfigFile() {

        Settings config = new Settings();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        return gson.toJson(config);

    }

    public static void main(String[] args){
        String jf = writeConfigFile();
        SettingsManager s = new SettingsManager();

        System.out.println(s.getSettings());
    }
}
