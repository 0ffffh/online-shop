package com.k0s.service;

import com.k0s.dao.UserDao;
import com.k0s.entity.user.User;
import com.k0s.security.PasswordCrypt;

import java.util.*;

public class UserService {
    private final UserDao userDao;
    private final Map<String, User> session = new HashMap<>();

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String login(String name, String password){
        User user = userDao.getUser(name);
        String cryptedPassword = PasswordCrypt.cryptPassword(password, user.getSalt());
        if(user.getPassword().equals(cryptedPassword)){
            String uuid = UUID.randomUUID().toString();
            session.put(uuid, user);
            return uuid;
        }
        return null;
    }

    public boolean isValidSession(String token) {
        if (token == null){
            return false;
        }
        return session.containsKey(token);
    }


}
