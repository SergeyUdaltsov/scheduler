package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.dao.ISettingDao;
import com.scheduler.model.Role;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.JsonUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/5/2021
 */
public class SettingDao implements ISettingDao {

    private static final String TABLE_NAME = "Settings";
    private static final String ROLES = "ROLES";
    private static final String SESSION_DURATION = "SESSION_DURATION";
    private static final TypeReference INTEGER_TYPE = new TypeReference<Integer>() {};
    private static final TypeReference ROLES_MAP_TYPE = new TypeReference<Map<Role, List<String>>>() {};

    private Table TABLE;

    public SettingDao(IDynamoDbFactory dynamoDbFactory) {
        TABLE = dynamoDbFactory.buildDynamo().getTable(TABLE_NAME);
    }

    @Override
    public Map<Role, List<String>> getRoleMapping() {
        Map roleMapping = getOrDefault(ROLES, ROLES_MAP_TYPE, Collections.emptyMap());
        if (CollectionUtils.isEmpty(roleMapping)) {
            throw new IllegalStateException("RoleMapping not found");
        }
        return roleMapping;
    }

    @Override
    public int getSessionDuration() {
        return getOrDefault(SESSION_DURATION, INTEGER_TYPE, 15);
    }

    private <T> T getOrDefault(String keyName, TypeReference<T> typeReference, T defaultValue) {
        Item settingItem = TABLE.getItem("key", keyName);
        if (settingItem == null) {
            return defaultValue;
        }
        Object value = settingItem.get("v");
        JsonNode jsonNode = JsonUtils.parseObjectToJsonNode(value);
        return JsonUtils.parseJsonNode(jsonNode, typeReference);
    }
}
