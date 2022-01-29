package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 6/16/2021
 */
public interface IPlayerHelper extends IHelper {

    CommandType getNextCommandType();
}
