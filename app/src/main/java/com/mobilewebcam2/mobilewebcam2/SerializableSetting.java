package com.mobilewebcam2.mobilewebcam2;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.util.List;

/**
 * Holds the metadata relative to the field, in order for the remote interface to be able to
 * validate it and to display additional info (i.e. the unit, an help string, etc...)
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SerializableSetting<T> {

    /**
     * Tag for the logger. Every class should have one.
     */
    @JsonIgnore
    private static final String LOG_TAG = "SerializableSetting";

    private Class<T> className;
    private int position;
    private String fullName;
    private String description;
    private T value;
    private T defaultValue;
    private String unit;
    private T upperBound;
    private T lowerBound;
    private List<T> allowedValues;
    private String faIconName;
    private SettingCategory settingType;

    private SerializableSetting(Class<T> className,
                               int position,
                               String fullName,
                               T value,
                               T defaultValue,
                               String unit,
                               String description,
                               T upperBound,
                               T lowerBound,
                               List<T> allowedValues,
                               String faIconName,
                               SettingCategory settingType){

        if(upperBound != null && lowerBound != null &&
                upperBound instanceof Comparable && lowerBound instanceof Comparable &&
                ((Comparable)upperBound).compareTo( (lowerBound) ) < 0 ) {
            throw new IllegalArgumentException(LOG_TAG + ": upperBound is higher than lowerBound");
        }
        if(className == null){
            throw new IllegalArgumentException(LOG_TAG + ": className must be specified.");
        }
        if(fullName == null || "".equals(fullName)){
            throw new IllegalArgumentException(LOG_TAG + ": fullName must be specified");
        }
        this.className = className;
        this.position = position;
        this.fullName = fullName;
        this.value = value;
        this.defaultValue = defaultValue;
        this.unit = unit;
        this.description = description;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.allowedValues = allowedValues;
        this.faIconName = faIconName;
        if(settingType == null){
            Log.w(LOG_TAG, "'"+fullName+"' did not specify its settingType value. Setting it to Advanced.");
            this.settingType = SettingCategory.ADVANCED;
        } else {
            this.settingType = settingType;
        }
    }

    /**
     * Constructor for regular fields: does not specify any FontAwesome icon.
     */
    public SerializableSetting(Class<T> className,
                               int position,
                               String fullName,
                               T value,
                               T defaultValue,
                               String unit,
                               String description,
                               T upperBound,
                               T lowerBound,
                               List<T> allowedValues,
                               SettingCategory settingType){
            this(className, position, fullName, value, defaultValue, unit, description, upperBound, lowerBound, allowedValues, null, settingType);
    }

    /**
     * Constructor for categories: does not specify any default / validation value, but requires a
     * FontAwesome icon. SettingType in this case defaults to Regular
     */
    public SerializableSetting(Class<T> className, int position, String fullName, T value, String description, String faIconName){
        this(className, position, fullName, value, null, null, description, null, null, null, faIconName, SettingCategory.REGULAR);
    }

    /**
     * Constructor for categories: does not specify any default / validation value, but requires a
     * FontAwesome icon. Can specify the setting type
     */
    public SerializableSetting(Class<T> className, int position, String fullName, T value, String description, String faIconName, SettingCategory settingType){
        this(className, position, fullName, value, null, null, description, null, null, null, faIconName, settingType);
    }

    public T getValue() {
        return value;
    }


    @JsonCreator
    public static <T> SerializableSetting<T> jsonCreator(
            @JsonProperty("className") Class<T> className,
            @JsonProperty("position") int position,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("value") T value,
            @JsonProperty("defaultValue")T defaultValue,
            @JsonProperty("unit") String unit,
            @JsonProperty("description") String description,
            @JsonProperty("upperBound") T upperBound,
            @JsonProperty("lowerBound")T lowerBound,
            @JsonProperty("allowedValues")JsonNode allowedValues,
            @JsonProperty("faIconName") String faIconName,
            @JsonProperty("settingType") SettingCategory settingType
            ) {

        ObjectMapper om = new ObjectMapper();
        ObjectReader or = om.reader();
        JavaType valuesType = or.getTypeFactory().constructParametricType(List.class, className);

        try {
            return new SerializableSetting<>(className,
                    position,
                    fullName,
                    value,
                    defaultValue,
                    unit,
                    description,
                    upperBound,
                    lowerBound,
                    or.forType(valuesType).readValue(allowedValues),
                    faIconName,
                    settingType);
        } catch (JsonProcessingException e){
            Log.e(LOG_TAG, "JsonProcessingException while deserializing config.json", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException while deserializing config.json", e);
            return null;
        }
    }

    @Override
    public String toString(){
        if(unit == null){
            return "\t\t"+position+". "+fullName+ ":   "+ value + "\n";
        }
        return "\t\t"+position+". "+fullName+ ":   "+ value + " "+ unit + "\n";
    }



    public enum SettingCategory {
        REGULAR(""),
        ADVANCED("Advanced");

        private String name;
        private SettingCategory(String name){
            this.name = name;
        }
    }
}
