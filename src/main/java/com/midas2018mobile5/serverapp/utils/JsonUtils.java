package com.midas2018mobile5.serverapp.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Slf4j
public class JsonUtils {
    private final ObjectMapper objectMapper;

    private JsonUtils() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_GETTERS, true);
        objectMapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
    }

    public static JsonUtils getInstance() {
        return new JsonUtils();
    }

    private static ObjectMapper getMapper() {
        return getInstance().objectMapper;
    }

    public static String toJson(Object value) {
        try {
            return getMapper().writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toJsonAsByte(Object value) {
        try {
            return getMapper().writeValueAsBytes(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(byte[] json, Class<T> cls) {
        try {
            return getMapper().readValue(json, cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        try {
            return getMapper().readValue(json, cls);
        } catch (Exception e) {
            log.warn("Json parsing is failed. origin_json={}", json);
            throw new RuntimeException(e);
        }
    }
    public static <T> T fromJson(BufferedReader json, TypeReference<T> typeReference) {
        try {
            return getMapper().readValue(json, typeReference);
        } catch (Exception e) {
            log.warn("Json parsing is failed. origin_json={}", json);
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return getMapper().readValue(json, typeReference);
        } catch (Exception e) {
            log.warn("Json parsing is failed. origin_json={}", json);
            throw new RuntimeException(e);
        }
    }

    public static String toPrettyJson(String value) {
        Object jsonObject = JsonUtils.fromJson(value, Object.class);
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
