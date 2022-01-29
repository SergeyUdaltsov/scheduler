package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.dao.IUserDao;
import com.scheduler.model.User;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public class UserDao extends BaseDao<User> implements IUserDao {

    public UserDao(IDynamoDbFactory dynamoFactory) {
        super(dynamoFactory, User.class);
    }

    @Override
    public User getUserById(long id) {
        return super.getEntityByPrimaryKey(new PrimaryKey(User.HASH_KEY_NAME, id));
    }

    @Override
    public User getUserByName(String name) {
        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression("n=:n")
                .withValueMap(new ValueMap()
                        .withString(":n", name));
        return super.getEntityByScanSpec(scanSpec);
    }
}
