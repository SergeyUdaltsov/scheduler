package com.scheduler.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
@DynamoDBDocument
public class Collect {

    private String name;
    private int sum;

    public Collect() {
    }

    public Collect(String name, int sum) {
        this.name = name;
        this.sum = sum;
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
}
