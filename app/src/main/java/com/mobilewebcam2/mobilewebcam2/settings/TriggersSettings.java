package com.mobilewebcam2.mobilewebcam2.settings;

import com.google.gson.annotations.Expose;

public class TriggersSettings {

    /**
     * Tag for the logger. Every class should have one.
     */
    @Expose(serialize = false, deserialize = false)
    private static final String LOG_TAG = "TriggersSettings";

    protected TriggersSettings() {
        // TODO
    }

    @Override
    public String toString(){
        String repr = "";
        repr += "\t\t  " + "\n";
        return repr;
    }

}
