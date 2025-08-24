package com.elearning.elearning_support.utils.object;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectMapperUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, Boolean.FALSE);
    }

    public static <T> T mapping(String json, Class<?> type) {
        try {
            Object o = mapper.readValue(json, type);
            return (T) o;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static <T> T objectMapper(String json, Class<?> type) {
        try {
            Object o = mapper.readValue(json, type);
            return (T) o;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static <T> List<T> listMapper(String json, Class<T> type) {
        try {
            if (Objects.isNull(json)) {
                return Collections.emptyList();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
            List<T> res = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
            return res;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    public static <T> String toJsonString(T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "";
        }
    }

    public static <T> String toJsonStringDoubleQuote(T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
            return mapper.writeValueAsString(object);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "";
        }
    }

}
