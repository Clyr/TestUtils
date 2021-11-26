package com.clyr.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by heiguang on 2017/11/21.
 */

public class GsonUtil {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
    }

    public static String toJson(Object jsonObject) {
        return GSON.toJson(jsonObject);
    }

    public static <T> T fromJson(String jsonData, Class<T> type) {
        T result = GSON.fromJson(jsonData, type);
        return result;
    }

    public static <T> T fromJson(Object json, Class<T> type) {
        String jsonData = GSON.toJson(json);
        return GSON.fromJson(jsonData, type);
    }

    public static <T> T fromJson(String jsonData, Type type) {
        return GSON.fromJson(jsonData, type);
    }

    public static <T> T fromJson(Object json, Type type) {
        String jsonData = GSON.toJson(json);
        return GSON.fromJson(jsonData, type);
    }
}
