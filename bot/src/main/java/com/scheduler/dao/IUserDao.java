package com.scheduler.dao;

import com.scheduler.model.User;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public interface IUserDao {

    User getUserById(long id);

    User getUserByName(String name);

    void save(User user);
}
