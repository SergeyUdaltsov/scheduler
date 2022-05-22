package com.scheduler.service;

import com.scheduler.model.User;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public interface IUserService {

    void saveUser(User user);

    User getUserById(long id);

    User getUserByName(String name);
}
