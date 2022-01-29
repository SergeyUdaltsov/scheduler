package com.scheduler.dao;

import com.scheduler.model.CommandType;
import com.scheduler.model.Context;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public interface IContextDao {

    void save(Context context);

    Context getContext(long userId);

    void updateContextCommand(long userId, CommandType commandType, CommandType previousCommandType,
                              Map<String, Object> params);
}
