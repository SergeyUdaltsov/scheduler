package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 5/2/2021
 */
public interface IDashboardHelper extends IHelper {

    CommandType getNextCommandType();

    String getHelperParamsValue();
}
