package com.scheduler.service;

import com.scheduler.model.MessageHolder;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public interface IRoleFacade {

    boolean isActionAllowed(String action, long operatorId);

    List<BotApiMethod> filterByNotAllowed(List<MessageHolder> holders, long operatorId);
}
