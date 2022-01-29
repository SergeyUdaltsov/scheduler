package com.scheduler.helper;

import com.scheduler.model.CommandType;

/**
 * @author Serhii_Udaltsov on 5/10/2021
 */
public interface IBillSundayIceEditHelper extends IHelper {

    CommandType getNextCommandType();
}
