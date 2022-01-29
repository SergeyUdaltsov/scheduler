package com.scheduler.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public interface IDynamoDbFactory {

    DynamoDBMapper buildMapper();

    DynamoDB buildDynamo();
}
