package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
@DynamoDBTable(tableName = "CollectPayments")
@JsonDeserialize(builder = CollectPayment.Builder.class)
public class CollectPayment {
    public static final String HASH_KEY_FIELD = "n";
    public static final String RANGE_KEY_FIELD = "p";
    public static final String PAYED_FIELD = "payed";
    public static final String TYPE_FIELD = "type";

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = HASH_KEY_FIELD)
    @JsonProperty(HASH_KEY_FIELD)
    private String name;

    @DynamoDBRangeKey
    @DynamoDBAttribute(attributeName = RANGE_KEY_FIELD)
    @JsonProperty(RANGE_KEY_FIELD)
    private String player;

    private long oId;
    private int sumBill;

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
    @DynamoDBAttribute
    private boolean payed;

    @DynamoDBTypeConvertedEnum
    private CollectType type;

    public CollectPayment(String name, long oId, int sumBill, String player, boolean payed, CollectType type) {
        this.name = name;
        this.oId = oId;
        this.sumBill = sumBill;
        this.player = player;
        this.payed = payed;
        this.type = type;
    }

    public CollectPayment(String name, String player) {
        this.name = name;
        this.player = player;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getoId() {
        return oId;
    }

    public void setoId(long oId) {
        this.oId = oId;
    }

    public int getSumBill() {
        return sumBill;
    }

    public void setSumBill(int sumBill) {
        this.sumBill = sumBill;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public CollectType getType() {
        return type;
    }

    public void setType(CollectType type) {
        this.type = type;
    }

    public static class Builder {
        private String name;
        private long oId;
        private int sumBill;
        private String player;
        private boolean payed;
        private CollectType type;

        @JsonProperty(HASH_KEY_FIELD)
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @JsonProperty("oId")
        public Builder withOperator(long oId) {
            this.oId = oId;
            return this;
        }

        @JsonProperty("sumBill")
        public Builder withBill(int sumBill) {
            this.sumBill = sumBill;
            return this;
        }

        @JsonProperty(PAYED_FIELD)
        public Builder withPayed(boolean payed) {
            this.payed = payed;
            return this;
        }

        @JsonProperty(TYPE_FIELD)
        public Builder withType(CollectType type) {
            this.type = type;
            return this;
        }

        @JsonProperty(RANGE_KEY_FIELD)
        public Builder withPlayer(String player) {
            this.player = player;
            return this;
        }

        public CollectPayment build() {
            return new CollectPayment(name, oId, sumBill, player, payed, type);
        }
    }
}
