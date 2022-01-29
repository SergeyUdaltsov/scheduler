package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 5/6/2021
 */
public interface IPaymentEditHelper extends IHelper {

    CommandType getNextCommandType();
}
