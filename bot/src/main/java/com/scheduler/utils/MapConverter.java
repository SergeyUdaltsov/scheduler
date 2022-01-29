package com.scheduler.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/13/2021
 */
public class MapConverter implements DynamoDBTypeConverter<Map<String, AttributeValue>, Map<String, Object>> {
    public static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<Map<String, Object>>() {};
    @Override
    public Map<String, AttributeValue> convert(Map<String, Object> object) {
        return DynamoDbJsonUtils.jsonObjectToMap(JsonUtils.parseObjectToJsonNode(object));
    }

    @Override
    public Map<String, Object> unconvert(Map<String, AttributeValue> object) {
        return JsonUtils.parseJsonNode(DynamoDbJsonUtils.mapToJsonObject(object), MAP_TYPE);
    }
}
