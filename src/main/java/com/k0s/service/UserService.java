package com.k0s.service;

import com.k0s.dao.UserDao;
import com.k0s.entity.user.User;

import java.util.*;

public class UserService {
    private final UserDao userDao;
    private final Map<String, User> session = new HashMap();

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser(String name, String password) {
        return userDao.getUser(name, password);
    }

    public String login(String name, String password){
        User user = userDao.getUser(name, password);
        String uuid = UUID.randomUUID().toString();
        session.put(uuid, user);
        return uuid;
    }

    public String getSession(User user){
        String uuid = UUID.randomUUID().toString();
        session.put(uuid, user);
        return uuid;
    }

    public boolean isValidSession(String token) {
        if (token == null){
            return false;
        }
        return session.containsKey(token);
    }


}
