package com.scheduler.dagger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.scheduler.dao.IContextDao;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.dao.ISettingDao;
import com.scheduler.dao.IUserDao;
import com.scheduler.dao.IUserSessionDao;
import com.scheduler.dao.impl.ContextDao;
import com.scheduler.dao.impl.DynamoDbFactory;
import com.scheduler.dao.impl.SettingDao;
import com.scheduler.dao.impl.UserDao;
import com.scheduler.dao.impl.UserSessionDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@Module
public class DaggerDaoProvider {

    @Provides
    @Singleton
    public AmazonDynamoDB dynamoDb() {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        builder.withRegion("eu-central-1");
        return builder.build();
    }

    @Provides
    @Singleton
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB dynamoDb) {
        return new DynamoDBMapper(dynamoDb);
    }

    @Provides
    @Singleton
    public IDynamoDbFactory dynamoFactory(AmazonDynamoDB dynamoDb) {
        return new DynamoDbFactory(dynamoDb);
    }

    @Provides
    @Singleton
    public IContextDao contextDao(IDynamoDbFactory dynamoDbFactory) {
        return new ContextDao(dynamoDbFactory);
    }

    @Provides
    @Singleton
    public ISettingDao settingDao(IDynamoDbFactory dynamoDbFactory) {
        return new SettingDao(dynamoDbFactory);
    }

    @Provides
    @Singleton
    public IUserDao userDao(IDynamoDbFactory dynamoDbFactory) {
        return new UserDao(dynamoDbFactory);
    }

    @Provides
    @Singleton
    public IUserSessionDao sessionDao(IDynamoDbFactory dynamoDbFactory) {
        return new UserSessionDao(dynamoDbFactory);
    }

}
