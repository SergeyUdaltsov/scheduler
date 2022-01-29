package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public interface ICollectActionHelper extends IHelper {

    String getHelperParamsValue();

    CommandType getNextCommandType();
}
