package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
import com.scheduler.utils.CommandTypeConverter;
import com.scheduler.utils.MapObjectConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@DynamoDBTable(tableName = "Context")
public class Context {
    public static final String HASH_KEY = "id";
    public static final String PARAMS_FIELD = "p";
    public static final String LOCATION_FIELD = "l";
    public static final String COLLECT_FIELD = "col";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    private long userId;

    @DynamoDBAttribute(attributeName = PARAMS_FIELD)
    @DynamoDBTypeConverted(converter = MapObjectConverter.class)
    private Map<String, Object> params;

    @DynamoDBTypeConverted(converter = CommandTypeConverter.class)
    @DynamoDBAttribute(attributeName = LOCATION_FIELD)
    private List<CommandType> location = new ArrayList<>();

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = COLLECT_FIELD)
    private List<Map<String, Object>> collect = new ArrayList<>();

    public Context() {
    }

    public Context(int userId, Map<String, Object> params, List<CommandType> location) {
        this.userId = userId;
        this.params = params;
        this.location = location;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<CommandType> getLocation() {
        return location;
    }

    public void setLocation(List<CommandType> location) {
        this.location = location;
    }

    public List<Map<String, Object>> getCollect() {
        return collect;
    }

    public void setCollect(List<Map<String, Object>> collect) {
        this.collect = collect;
    }
}
