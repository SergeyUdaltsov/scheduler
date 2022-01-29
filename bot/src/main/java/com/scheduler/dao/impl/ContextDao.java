package com.scheduler.dao.impl;

import com.scheduler.dao.IContextDao;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public class ContextDao extends BaseDao<Context> implements IContextDao {
    private IDynamoDbFactory dynamoDbFactory;

    public ContextDao(IDynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Context.class);
        this.dynamoDbFactory = dynamoDbFactory;
    }

    @Override
    public Context getContext(long userId) {
        Context context = new Context();
        context.setUserId(userId);
        return getEntityByQueryObject(context);
    }

    @Override
    public void updateContextCommand(long userId, CommandType commandType, CommandType previousCommandType,
                                     Map<String, Object> params) {
    }
}
