package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@DynamoDBTable(tableName = "User")
@DynamoDBDocument
public class User {

    public static String HASH_KEY_NAME = "id";
    private static final String COLLECT_FIELD_NAME = "coll";
    private static final String NAME_FIELD_NAME = "n";

    @DynamoDBHashKey
    private long id;

    @JsonProperty(NAME_FIELD_NAME)
    @DynamoDBAttribute(attributeName = NAME_FIELD_NAME)
    private String name;
    private List<String> years;

    @DynamoDBTypeConvertedEnum
    private Role role;

    @DynamoDBAttribute(attributeName = COLLECT_FIELD_NAME)
    @JsonProperty(COLLECT_FIELD_NAME)
    private List<Collect> collects = new ArrayList<>();

    public User() {
    }

    public User(long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Collect> getCollects() {
        return collects;
    }

    public void setCollects(List<Collect> collects) {
        this.collects = collects;
    }
}
