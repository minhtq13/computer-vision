package com.elearning.elearning_support.utils.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.elearning.elearning_support.utils.converter.GsonIntegerTypeAdapter;
import com.elearning.elearning_support.utils.converter.GsonLongTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GsonMapperUtils {

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(Integer.class, new GsonIntegerTypeAdapter())
        .registerTypeAdapter(Long.class, new GsonLongTypeAdapter())
        .create();

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            // Nếu kiểu là Map hoặc List thì dùng JsonElement để parse đúng kiểu số
            if (clazz == Object.class || Map.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)) {
                JsonElement jsonElement = JsonParser.parseString(json);
                Object result = parseJsonElement(jsonElement);
                return clazz.cast(result);
            }
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            log.error("GsonMapperUtil.fromJson: {}", e.getMessage(), e);
            return null;
        }
    }

    public static <T> T fromJson(String json, Type type) {
        if (json == null) {
            return null;
        }
        try {
            if (type.equals(new TypeToken<Map<String, Object>>() {
            }.getType()) ||
                type.equals(new TypeToken<List<Object>>() {
                }.getType()) ||
                type.equals(Object.class)) {
                JsonElement jsonElement = JsonParser.parseString(json);
                Object result = parseJsonElement(jsonElement);
                return (T) result;
            }
            return gson.fromJson(json, type);
        } catch (Exception e) {
            log.error("GsonMapperUtil.fromJson: {}", e.getMessage(), e);
            return null;
        }
    }

    public static String toJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            log.error("GsonMapperUtil.toJson: {}", e.getMessage(), e);
            return null;
        }
    }

    private static Object parseJsonElement(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }

        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive prim = jsonElement.getAsJsonPrimitive();
            if (prim.isBoolean()) {
                return prim.getAsBoolean();
            }
            if (prim.isString()) {
                return prim.getAsString();
            }
            if (prim.isNumber()) {
                double num = prim.getAsDouble();
                if (num == (int) num) {
                    return (int) num;
                } else if (num == (long) num) {
                    return (long) num;
                } else {
                    return num;
                }
            }
        } else if (jsonElement.isJsonArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonElement elem : jsonElement.getAsJsonArray()) {
                list.add(parseJsonElement(elem));
            }
            return list;
        } else if (jsonElement.isJsonObject()) {
            Map<String, Object> map = new LinkedTreeMap<>();
            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                map.put(entry.getKey(), parseJsonElement(entry.getValue()));
            }
            return map;
        }

        return jsonElement.toString();
    }

}
