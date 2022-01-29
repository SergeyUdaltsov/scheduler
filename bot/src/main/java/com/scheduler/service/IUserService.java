package com.scheduler.service;

import com.scheduler.model.Collect;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.User;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public interface IUserService {

    User getUserById(long id);

    MessageHolder buildYearMessageHolder(long id);

    User getUserByName(String name);

    void addCollectToUser(Update update, Collect collect);

    void removeCollect(Update update, String collectName);
}
