package com.k0s.service;

import com.k0s.dao.UserDao;
import com.k0s.entity.user.User;
import com.k0s.security.PasswordCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SecurityService {
    private final UserService userService;
    private final Map<String, User> session = new HashMap<>();

    public SecurityService(UserService userService) {
        this.userService= userService;
    }


    public String login(String name, String password){
        User user = userService.getUser(name);
        String cryptedPassword = PasswordCrypt.encryptPassword(password, user.getSalt());
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
