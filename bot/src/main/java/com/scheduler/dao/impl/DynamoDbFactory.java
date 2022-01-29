package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.scheduler.dao.IDynamoDbFactory;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public class DynamoDbFactory implements IDynamoDbFactory {

    private AmazonDynamoDB dynamoDB;

    public DynamoDbFactory(AmazonDynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    @Override
    public DynamoDBMapper buildMapper() {
        return new DynamoDBMapper(dynamoDB);
    }

    @Override
    public DynamoDB buildDynamo() {
        return new DynamoDB(dynamoDB);
    }
}
