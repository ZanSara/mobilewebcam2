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
    private String fullName;
    private String description;
    private T value;
    private T defaultValue;
    private String unit;
    private T upperBound;
    private T lowerBound;
    private List<T> allowedValues;

    public SerializableSetting(Class<T> className,
                               String fullName,
                               T value,
                               T defaultValue,
                               String unit,
                               String description,
                               T upperBound,
                               T lowerBound,
                               List<T> allowedValues){

        if(upperBound != null && lowerBound != null &&
                upperBound instanceof Comparable && lowerBound instanceof Comparable &&
                ((Comparable)upperBound).compareTo( ((Comparable)lowerBound) ) < 0 ){
                throw new IllegalArgumentException(LOG_TAG+": upperBound is higher than lowerBound");
            }
        this.className = className;
        this.fullName = fullName;
        this.value = value;
        this.defaultValue = defaultValue;
        this.unit = unit;
        this.description = description;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.allowedValues = allowedValues;
    }

    public SerializableSetting(Class<T> className, String fullName, T value, String description){
        this(className, fullName, value, null, null, description, null, null, null);
    }

    public T getValue() {
        return value;
    }


    @JsonCreator
    public static <T> SerializableSetting<T> jsonCreator(
            @JsonProperty("className") Class<T> className,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("value") T value,
            @JsonProperty("defaultValue")T defaultValue,
            @JsonProperty("unit") String unit,
            @JsonProperty("description") String description,
            @JsonProperty("upperBound") T upperBound,
            @JsonProperty("lowerBound")T lowerBound,
            @JsonProperty("allowedValues")JsonNode allowedValues ) {

        ObjectMapper om = new ObjectMapper();
        ObjectReader or = om.reader();
        JavaType valuesType = or.getTypeFactory().constructParametricType(List.class, className);

        try {
            return new SerializableSetting<>(className,
                    fullName,
                    value,
                    defaultValue,
                    unit,
                    description,
                    upperBound,
                    lowerBound,
                    or.forType(valuesType).readValue(allowedValues));
        } catch (JsonProcessingException e){
            Log.e(LOG_TAG, "JsonProcessingException while deserializing config.json", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException while deserializing config.json", e);
            return null;
        }
    }
}
