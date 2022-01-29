package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 5/8/2021
 */
public interface IBillEditExecuteHelper extends IHelper {

    CommandType getNextCommandType();
}
