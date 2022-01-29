package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 5/4/2021
 */
public interface IBillExecuteHelper extends IHelper {

    CommandType getNextCommandType();
}
