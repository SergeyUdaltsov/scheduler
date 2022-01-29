/*
 * Copyright 2020 Softline Group Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scheduler.utils;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DynamoDbJsonUtils {

    private static final JsonNodeFactory NODE_FACTORY = objectMapper().getNodeFactory();
    private static final int MAX_DEPTH = 50;

    private DynamoDbJsonUtils() { }

    public static JsonNode itemListToJsonArray(List<Map<String, AttributeValue>> items) {
        if (items == null) {
            throw new RuntimeException("Items cannnot be null");
        }
        ArrayNode array = NODE_FACTORY.arrayNode();
        for (Map<String, AttributeValue> item : items) {
            array.add(mapToJsonObject(item, 0));
        }
        return array;
    }

    public static List<AttributeValue> jsonArrayToList(JsonNode node) {
        return jsonArrayToList(node, 0);
    }

    private static List<AttributeValue> jsonArrayToList(JsonNode node, int depth) {
        assertDepth(depth);
        if (node != null && node.isArray()) {
            List<AttributeValue> result = new ArrayList<>();
            Iterator<JsonNode> children = node.elements();
            while (children.hasNext()) {
                JsonNode child = children.next();
                result.add(getAttributeValue(child, depth));
            }
            return result;
        }
        throw new RuntimeException("Expected JSON array, but received " + node);
    }

    public static Map<String, AttributeValue> jsonObjectToMap(JsonNode node) {
        return jsonObjectToMap(node, 0);
    }

    private static Map<String, AttributeValue> jsonObjectToMap(JsonNode node, int depth) {
        assertDepth(depth);
        if (node != null && node.isObject()) {
            Map<String, AttributeValue> result = new HashMap<>();
            Iterator<String> keys = node.fieldNames();
            while (keys.hasNext()) {
                String key = keys.next();
                result.put(key, getAttributeValue(node.get(key), depth + 1));
            }
            return result;
        }
        throw new RuntimeException("Expected JSON Object, but received " + node);
    }

    public static JsonNode listToJsonArray(List<AttributeValue> item) {
        return listToJsonArray(item, 0);
    }

    private static JsonNode listToJsonArray(List<AttributeValue> item, int depth) {
        assertDepth(depth);
        if (item == null) {
            throw new RuntimeException("Item cannot be null");
        }
        ArrayNode node = NODE_FACTORY.arrayNode();
        for (AttributeValue value : item) {
            node.add(getJsonNode(value, depth + 1));
        }
        return node;
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                .disable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(new JodaModule());
    }

    public static JsonNode mapToJsonObject(Map<String, AttributeValue> item) {
        return mapToJsonObject(item, 0);
    }

    private static JsonNode mapToJsonObject(Map<String, AttributeValue> item, int depth) {
        assertDepth(depth);
        if (item == null) {
            throw new RuntimeException("Item cannot be null");
        }
        ObjectNode node = NODE_FACTORY.objectNode();
        for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
            node.replace(entry.getKey(), getJsonNode(entry.getValue(), depth + 1));
        }
        return node;
    }

    private static void assertDepth(int depth) {
        if (depth > MAX_DEPTH) {
            throw new RuntimeException("Max depth reached. The object/array has too much depth.");
        }
    }

    private static AttributeValue getAttributeValue(JsonNode node, int depth) {
        assertDepth(depth);
        switch (node.asToken()) {
            case VALUE_STRING:
                return new AttributeValue().withS(node.textValue());
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                return new AttributeValue().withN(node.numberValue().toString());
            case VALUE_TRUE:
            case VALUE_FALSE:
                return new AttributeValue().withBOOL(node.booleanValue());
            case VALUE_NULL:
                return new AttributeValue().withNULL(true);
            case START_OBJECT:
                return new AttributeValue().withM(jsonObjectToMap(node, depth));
            case START_ARRAY:
                return new AttributeValue().withL(jsonArrayToList(node, depth));
            default:
                throw new RuntimeException("Unknown node type: " + node);
        }
    }

    private static JsonNode getJsonNode(AttributeValue av, int depth) {
        assertDepth(depth);
        if (av.getS() != null) {
            return NODE_FACTORY.textNode(av.getS());
        } else if (av.getN() != null) {
            try {
                return NODE_FACTORY.numberNode(Integer.parseInt(av.getN()));
            } catch (NumberFormatException e) {
                try {
                    return NODE_FACTORY.numberNode(Float.parseFloat(av.getN()));
                } catch (NumberFormatException e2) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else if (av.getBOOL() != null) {
            return NODE_FACTORY.booleanNode(av.getBOOL());
        } else if (av.getNULL() != null) {
            return NODE_FACTORY.nullNode();
        } else if (av.getL() != null) {
            return listToJsonArray(av.getL(), depth);
        } else if (av.getM() != null) {
            return mapToJsonObject(av.getM(), depth);
        } else {
            throw new RuntimeException("Unknown type value " + av);
        }
    }
}
