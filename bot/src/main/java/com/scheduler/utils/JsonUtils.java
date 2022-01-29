package com.scheduler.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static String convertObjectToString(Object object) {
        try {
            return jsonMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.error("Error during convert object {}", object.getClass());
            throw new IllegalStateException("Error during convert object " + object.getClass(), e);
        }
    }

    public static boolean saveObjectToFileAsJson(File file, Object object) {
        try {
            jsonMapper.writeValue(file, object);
            return true;
        } catch (IOException e) {
            LOG.error("Error during saving object to file {}", object.getClass());
            return false;
        }
    }

    public static <T> T parseJsonNode(JsonNode jsonNode, TypeReference<T> clazz) {
        try {
            return jsonMapper.readerFor(clazz).readValue(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse json node to object", e);
        }
    }

    public static <T> JsonNode parseObjectToJsonNode(T object) {
        try {
            return jsonMapper.valueToTree(object);
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert object to json node", e);
        }
    }

    public static <T> T getObjectFromJsonString(Class<T> type, String json) {
        try {
            return jsonMapper.readValue(json, type);
        } catch (IOException e) {
            LOG.error("Error during deconvert object");
            throw new IllegalStateException("Error during deconvert object", e);
        }
    }

    public static <T> T getObjectFromInputStream(Class<T> type, InputStream json) {
        try {
            return jsonMapper.readValue(json, type);
        } catch (IOException e) {
            LOG.error("Error during deconvert object");
            throw new IllegalStateException("Error during deconvert object", e);
        }
    }

    public static <T> T getObjectFromJsonFile(Class<T> type, File json) {
        try {
            return jsonMapper.readValue(json, type);
        } catch (IOException e) {
            LOG.error("Error during deconvert object");
            throw new IllegalStateException("Error during deconvert object", e);
        }
    }

    public static <T> T getObjectFromJsonString(TypeReference<T> type, String json) {
        try {
            return jsonMapper.readValue(json, type);
        } catch (IOException e) {
            LOG.error("Error during deconvert object");
            throw new IllegalStateException("Error during deconvert object", e);
        }
    }

    public static <T> T getObjectFromInputStream(TypeReference<T> type, InputStream json) {
        try {
            return jsonMapper.readValue(json, type);
        } catch (IOException e) {
            LOG.error("Error during deconvert object");
            throw new IllegalStateException("Error during deconvert object", e);
        }
    }

    public static void writeObjectToOutput(OutputStream outputStream, Object data) {
        try {
            JsonGenerator generator = jsonMapper.getFactory().createGenerator(outputStream);
            generator.writeObject(data);
        } catch (IOException var3) {
            var3.printStackTrace();
            throw new RuntimeException("Error while writing object to response.");
        }
    }

    public static <T> T getObjectFromJsonFile(TypeReference<T> type, File json) {
        try {
            return jsonMapper.readValue(json, type);
        } catch (IOException e) {
            LOG.error("Error during deconvert object");
            throw new IllegalStateException("Error during deconvert object", e);
        }
    }

    public static <T> T parseMap(Map<String, ?> params, TypeReference<T> clazz) {
        try {
            return jsonMapper.convertValue(params, clazz);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot parse json from map", e);
        }
    }

    public static <T> T parseMap(Map<String, ?> params, Class<T> clazz) {
        try {
            return jsonMapper.convertValue(params, clazz);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot parse json from map", e);
        }
    }

}
