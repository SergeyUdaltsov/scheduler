package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.dao.IUserSessionDao;
import com.scheduler.utils.JsonUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public class UserSessionDao implements IUserSessionDao {
    private static final String TABLE_NAME = "UserSession";
    private Table TABLE;

    public UserSessionDao(IDynamoDbFactory dynamoDbFactory) {
        TABLE = dynamoDbFactory.buildDynamo().getTable(TABLE_NAME);
    }

    @Override
    public List<String> getUserActionList(long operatorId) {
        Item settingItem = TABLE.getItem("oId", operatorId);
        if (settingItem == null) {
            return Collections.emptyList();
        }
        Object value = settingItem.get("v");
        JsonNode jsonNode = JsonUtils.parseObjectToJsonNode(value);
        return JsonUtils.parseJsonNode(jsonNode, new TypeReference<List<String>>() {
        });
    }

    @Override
    public void saveUserSession(long operatorId, List<String> actions) {
        AttributeUpdate attributeUpdate = new AttributeUpdate("v").put(actions);
        TABLE.updateItem("oId", operatorId, attributeUpdate);
    }
}
