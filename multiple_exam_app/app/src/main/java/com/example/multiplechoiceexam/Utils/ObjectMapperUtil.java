package com.example.multiplechoiceexam.Utils;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class ObjectMapperUtil {
    private static final String TAG = ObjectMapperUtil.class.getSimpleName();

    private ObjectMapperUtil() {
        // Private constructor to prevent instantiation
    }

    public static <T> T mapping(String json, Class<?> type) {
        try {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Object o = mapper.readValue(json, type);
            return (T) o;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.toString());
            return null;
        }
    }

    public static <T> T objectMapper(String json, Class<?> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object o = mapper.readValue(json, type);
            return (T) o;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.toString());
            return null;
        }
    }

    public static <T> List<T> listMapper(String json, Class<T> type) {
        try {
            if (json == null || json.isEmpty()) {
                return Collections.emptyList();
            }
            ObjectMapper mapper = new ObjectMapper();
            List<T> res = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
            return res;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    public static <T> String toJsonString(T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return "";
        }
    }

    public static <T> String toJsonStringDoubleQuote(T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            return mapper.writeValueAsString(object);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return "";
        }
    }

}
