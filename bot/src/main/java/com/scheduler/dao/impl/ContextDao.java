package com.scheduler.dao.impl;

import com.scheduler.dao.IContextDao;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.Language;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public class ContextDao extends BaseDao<Context> implements IContextDao {

    public ContextDao(IDynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Context.class);
    }

    @Override
    public void updateLocale(long id, Language language) {
        Context context = getContext(id);
        context.setLanguage(language);
        save(context);
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
