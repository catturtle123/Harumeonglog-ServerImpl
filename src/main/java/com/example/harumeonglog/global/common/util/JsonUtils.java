package com.example.harumeonglog.global.common.util;

import com.example.harumeonglog.global.error.code.JsonDeserializationErrorCode;
import com.example.harumeonglog.global.error.exception.JsonDeserializationException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new JsonDeserializationException(JsonDeserializationErrorCode._JSON_DESERIALIZATION_FAILED);
        }
    }
}
