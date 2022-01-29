package com.scheduler.service.impl;

import com.scheduler.dao.IUserDao;
import com.scheduler.model.Collect;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.User;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public class UserService implements IUserService {

    private IUserDao userDao;

    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public MessageHolder buildYearMessageHolder(long id) {
        User user = getUserById(id);
        List<String> years = user.getYears();
        return MessageUtils.holder(years, "Выбери год", KeyBoardType.TWO_ROW, false, true);
    }

    @Override
    public void addCollectToUser(Update update, Collect collect) {
        long userId = MessageUtils.getUserIdFromUpdate(update);
        User user = getUserById(userId);
        user.getCollects().add(collect);
        userDao.save(user);
    }

    @Override
    public void removeCollect(Update update, String collectName) {
        long userId = MessageUtils.getUserIdFromUpdate(update);
        User user = getUserById(userId);
        user.getCollects().removeIf(c -> collectName.equals(c.getName()));
        userDao.save(user);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }
}
