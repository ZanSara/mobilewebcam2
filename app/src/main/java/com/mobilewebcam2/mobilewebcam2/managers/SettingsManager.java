package com.mobilewebcam2.mobilewebcam2.managers;

import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;

/**
 * Manager of the settings file.\n
 *  - Pulls from server according to its own settings\n
 *  - Marshals and unmarshals with GSON\n
 *  - Returns the unmarshalled hierarchy of newly configured Managers to the caller.\n
 */
public final class SettingsManager {

    /**
     * Tag for the logger. Every class should have one.
     */
    private static final String LOG_TAG = "SettingsManager";

    protected SettingsManager() {
        // TODO
    }


    /**
     * Reads settings file into a string and instantiates the hierarchy of RootSettings objects.
     *
     * If the read fails for any reason, a new RootSettings class is instantiated with its hard coded,
     * default values.
     *
     * @return reference to the root RootSettings object
     */
    public RootSettings readSettingsFile(){
        try {
            File file = new File(RootManager.getInstance().getSettingsFilePath()); // FIXME decide how to make the path to settings.json configurable
            CharSource source = Files.asCharSource(file, Charsets.UTF_8);
            return readSettingsJSON(source.read());

        } catch (Exception e) {
            // FIXME Log this issue and notify the user
            e.printStackTrace();
            return new RootSettings();
        }
    }

    /**
     * Reads JSON string and instantiates the hierarchy of RootSettings objects. INTERNAL.
     * @see SettingsManager#readSettingsFile()
     */
    public RootSettings readSettingsJSON(String settings) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(settings);
        Gson gson = new Gson();
        return gson.fromJson(jsonElement, RootSettings.class);
    }

    /**
     * FIXME This method shouldn't be needed!! Testing purposes only!
     */
    public String writeConfigFile() {
        //GsonBuilder builder = new GsonBuilder();
        //builder.setPrettyPrinting();
        //Gson gson = builder.create();
        //return gson.toJson( RootManager.getInstance().getRootSettings() );

        RootSettings rs = RootManager.getInstance().getRootSettings();
        Log.d(LOG_TAG, ""+rs.toString().length());

        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteStream ,"UTF-8");
            //JsonWriter writer = new JsonWriter(outputStreamWriter);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

            //writer.setIndent("  ");
            return gson.toJson(rs , rs.getClass()); //, writer);
            //writer.close();

            //return byteStream.toString("UTF-8");

        } catch (Exception e ){
            Log.e(LOG_TAG, "writeConfigFile error! ", e);
        }
        return null;
    }

    @Override
    public String toString(){
        String repr = "";
        repr += "\t\t " + "\n";

        return repr;
    }
}
