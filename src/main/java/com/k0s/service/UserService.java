package com.k0s.service;

import com.k0s.dao.UserDao;
import com.k0s.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(@Autowired UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser(String name) {
        return userDao.get(name);
    }
}




