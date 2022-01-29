package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Serhii_Udaltsov on 5/4/2021
 */
@DynamoDBTable(tableName = "Bills")
@JsonDeserialize(builder = Bill.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bill {
    private static final String TYPE_FIELD = "t";
    private static final String MONTH_FIELD = "m";
    private static final String DESCRIPTION_FIELD = "desc";
    private static final String OPERATOR_FIELD = "o";
    private static final String SUM_FIELD = "s";

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = TYPE_FIELD)
    @DynamoDBTypeConvertedEnum
    @JsonProperty(TYPE_FIELD)
    private PaymentType type;

    @DynamoDBRangeKey
    @DynamoDBAttribute(attributeName = MONTH_FIELD)
    @JsonProperty(MONTH_FIELD)
    private int month;

    @DynamoDBAttribute(attributeName = DESCRIPTION_FIELD)
    @JsonProperty(DESCRIPTION_FIELD)
    private String description;

    @DynamoDBAttribute(attributeName = OPERATOR_FIELD)
    @JsonProperty(OPERATOR_FIELD)
    private String operator;

    @DynamoDBAttribute(attributeName = SUM_FIELD)
    @JsonProperty(SUM_FIELD)
    private int sum;

    public Bill() {
    }

    public Bill(PaymentType type, int month, String description,
                String operator, int sum) {
        this.type = type;
        this.month = month;
        this.description = description;
        this.operator = operator;
        this.sum = sum;
    }

    public static Builder builder() {
        return new Builder();
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "type=" + type +
                ", month='" + month + '\'' +
                ", description='" + description + '\'' +
                ", operator='" + operator + '\'' +
                ", sum=" + sum +
                '}';
    }

    public static class Builder {
        private int month;
        private int sum;
        private PaymentType type;
        private String operator;
        private String description;

        @JsonProperty(TYPE_FIELD)
        public Builder withType(PaymentType type) {
            this.type = type;
            return this;
        }

        @JsonProperty(OPERATOR_FIELD)
        public Builder withOperator(String operator) {
            this.operator = operator;
            return this;
        }

        @JsonProperty(MONTH_FIELD)
        public Builder withMonth(int month) {
            this.month = month;
            return this;
        }

        @JsonProperty(SUM_FIELD)
        public Builder withSum(int sum) {
            this.sum = sum;
            return this;
        }

        @JsonProperty(MONTH_FIELD)
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Bill build() {
            return new Bill(type, month, description, operator, sum);
        }
    }
}
