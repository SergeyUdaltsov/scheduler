package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
@DynamoDBTable(tableName = "Transfers")
public class Transfer {
    public static final String DATE_FIELD_NAME = "d";
    public static final String OPERATOR_FIELD_NAME = "oId";

    @DynamoDBHashKey(attributeName = OPERATOR_FIELD_NAME)
    @JsonProperty(OPERATOR_FIELD_NAME)
    private long operatorId;

    private int sum;

    @DynamoDBRangeKey(attributeName = DATE_FIELD_NAME)
    @JsonProperty(DATE_FIELD_NAME)
    private long date;

    @DynamoDBTypeConvertedEnum
    private PaymentType type;

    public Transfer() {
    }

    public Transfer(long date, int sum, long operatorId, PaymentType type) {
        this.date = date;
        this.sum = sum;
        this.operatorId = operatorId;
        this.type = type;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }
}
