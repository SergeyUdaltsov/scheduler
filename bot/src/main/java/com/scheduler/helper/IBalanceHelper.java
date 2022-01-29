package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 5/11/2021
 */
public interface IBalanceHelper extends IHelper {

    CommandType getNextCommandType();
}
