package com.scheduler.service.impl;

import com.scheduler.dao.IUserDao;
import com.scheduler.model.User;
import com.scheduler.service.IUserService;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public class UserService implements IUserService {

    private final IUserDao userDao;

    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void saveUser(User user) {
        userDao.save(user);
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }
}
