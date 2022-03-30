package com.k0s.service;

import com.k0s.dao.Dao;

import com.k0s.entity.user.User;

public class UserService {
    private final Dao<User> userDao;

    public UserService(Dao<User> userDao) {
        this.userDao = userDao;
    }

    public User getUser(String name) {
        return userDao.get(name);
    }
}




