package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@DynamoDBTable(tableName = "Players")
public class Player {
    public static final String YEAR_FIELD = "y";
    public static final String NAME_FIELD = "n";
    public static final String YEAR_INDEX = "y-index";


    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = NAME_FIELD)
    @JsonProperty(value = NAME_FIELD)
    private String name;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = YEAR_INDEX)
    @DynamoDBAttribute(attributeName = YEAR_FIELD)
    @JsonProperty(value = YEAR_FIELD)
    private int year;

    public Player(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return getYear() == player.getYear() &&
                Objects.equals(getName(), player.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getYear());
    }
}
