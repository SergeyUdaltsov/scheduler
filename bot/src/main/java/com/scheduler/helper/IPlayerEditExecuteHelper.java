package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 5/30/2021
 */
public interface IPlayerEditExecuteHelper extends IHelper{

    CommandType getNextCommandType();
}
