package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 5/9/2021
 */
public interface IBillEditExecuteBillTypeHelper extends IHelper {

    CommandType getNextCommandType();
}
