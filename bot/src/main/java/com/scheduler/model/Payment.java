package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@DynamoDBTable(tableName = "Payments")
@JsonDeserialize(builder = Payment.Builder.class)
public class Payment {
    private static final String NAME_FIELD = "n";
    private static final String DATE_FIELD = "d";
    private static final String TYPE_FIELD = "t";
    private static final String OPERATOR_FIELD = "o";
    private static final String OPERATOR_ID_FIELD = "oId";
    public static final String SUM_FIELD = "s";
    public static final String BILL_FIELD = "sb";
    private static final String HIDDEN_FIELD = "h";
    public static final String INDEX_NAME = "oId-d-index";

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = NAME_FIELD)
    @JsonProperty(NAME_FIELD)
    private String name;

    @DynamoDBAttribute(attributeName = SUM_FIELD)
    @JsonProperty(SUM_FIELD)
    private int sum;

    @DynamoDBAttribute(attributeName = BILL_FIELD)
    @JsonProperty(BILL_FIELD)
    private int sumBill;

    @DynamoDBRangeKey
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = INDEX_NAME)
    @DynamoDBAttribute(attributeName = DATE_FIELD)
    @JsonProperty(DATE_FIELD)
    private long date;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = TYPE_FIELD)
    @JsonProperty(TYPE_FIELD)
    private PaymentType type;

    @DynamoDBAttribute(attributeName = OPERATOR_FIELD)
    @JsonProperty(OPERATOR_FIELD)
    private String operator;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "oId-d-index")
    @DynamoDBAttribute(attributeName = OPERATOR_ID_FIELD)
    @JsonProperty(OPERATOR_ID_FIELD)
    private long operatorId;

    @DynamoDBAttribute(attributeName = HIDDEN_FIELD)
    @JsonProperty(HIDDEN_FIELD)
    private boolean hidden;

    public Payment() {
    }

    public Payment(String name, int sum, long date, PaymentType type, String operator, long operatorId,
                   int sumBill, boolean hidden) {
        this.name = name;
        this.sum = sum;
        this.date = date;
        this.type = type;
        this.operator = operator;
        this.operatorId = operatorId;
        this.sumBill = sumBill;
        this.hidden = hidden;
    }

    public Payment(String name, long date) {
        this.name = name;
        this.date = date;
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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getSumBill() {
        return sumBill;
    }

    public void setSumBill(int sumBill) {
        this.sumBill = sumBill;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public static class Builder {
        private String name;
        private int sum;
        private long date;
        private PaymentType type;
        private String operator;
        private long operatorId;
        private int sumBill;
        private boolean hidden;

        @JsonProperty(NAME_FIELD)
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @JsonProperty(OPERATOR_FIELD)
        public Builder withOperator(String operator) {
            this.operator = operator;
            return this;
        }

        @JsonProperty(OPERATOR_ID_FIELD)
        public Builder withOperatorId(long operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        @JsonProperty(SUM_FIELD)
        public Builder withSum(int sum) {
            this.sum = sum;
            return this;
        }

        @JsonProperty(BILL_FIELD)
        public Builder withSumBill(int sumBill) {
            this.sumBill = sumBill;
            return this;
        }

        @JsonProperty(HIDDEN_FIELD)
        public Builder withHidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        @JsonProperty(DATE_FIELD)
        public Builder withDate(long date) {
            this.date = date;
            return this;
        }

        @JsonProperty(TYPE_FIELD)
        public Builder withType(PaymentType type) {
            this.type = type;
            return this;
        }

        public Payment build() {
            return new Payment(name, sum, date, type, operator, operatorId, sumBill, hidden);
        }
    }

    @Override
    public String toString() {
        return "Payment{" +
                "name='" + name + '\'' +
                ", sum=" + sum +
                ", sumBill=" + sumBill +
                ", date=" + date +
                ", type=" + type +
                ", operator='" + operator + '\'' +
                ", operatorId=" + operatorId +
                ", hidden=" + hidden +
                '}';
    }
}
