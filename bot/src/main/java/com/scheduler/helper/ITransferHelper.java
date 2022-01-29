package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 6/1/2021
 */
public interface ITransferHelper extends IHelper {

    CommandType getNextCommandType();
}
