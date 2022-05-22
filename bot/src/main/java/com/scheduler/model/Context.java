package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.scheduler.utils.CommandTypeConverter;
import com.scheduler.utils.MapObjectConverter;
import com.scheduler.utils.MapStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@DynamoDBTable(tableName = "Context")
public class Context {
    public static final String HASH_KEY = "id";
    public static final String PARAMS_FIELD = "p";
    public static final String LOCALE_FIELD = "l";
    public static final String COMMANDS_FIELD = "c";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    private long userId;

    @DynamoDBAttribute(attributeName = PARAMS_FIELD)
    @DynamoDBTypeConverted(converter = MapObjectConverter.class)
    private Map<String, Object> params;

    @DynamoDBAttribute(attributeName = LOCALE_FIELD)
    @DynamoDBTypeConvertedEnum
    private Language language;

    @DynamoDBAttribute(attributeName = COMMANDS_FIELD)
    @DynamoDBTypeConverted(converter = MapStringConverter.class)
    private Map<String, String> commands;

    public Context() {
    }

    public Context(int userId, Map<String, Object> params, Language language) {
        this.userId = userId;
        this.params = params;
        this.language = language;
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Map<String, String> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, String> commands) {
        this.commands = commands;
    }
}
