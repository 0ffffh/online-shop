package com.k0s.service;

import com.k0s.entity.user.User;
import com.k0s.security.PasswordCrypt;
import com.k0s.security.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecurityService {
    private final UserService userService;
    private final List<Session> sessionList = new ArrayList<>();

    public SecurityService(UserService userService) {
        this.userService = userService;
    }


    public String login(String name, String password) throws IllegalAccessException {
        User user = userService.getUser(name);
        String token = getToken(user.getPassword(), password, user.getSalt());

        if (token == null) {
            throw new IllegalAccessException("Access denied");
        }

        sessionList.add(new Session(token, user, LocalDateTime.now().plusMinutes(1)));
        return token;
    }

    public boolean isValidSession(String token) {
        if (token == null) {
            return false;
        }

        //TODO: сделать через forach, както чистить sessionList
        for (int i = 0; i < sessionList.size(); i++) {
            if (token.equals(sessionList.get(i).getToken())) {
                if (LocalDateTime.now().isBefore(sessionList.get(i).getExpireDate())) {
                    return true;
                } else {
                    sessionList.remove(i);
                }
            }
        }
        System.out.println("Session invalid!");
        return false;
    }

    private String getToken(String userPassword, String password, String salt) {
        String cryptPassword = PasswordCrypt.encryptPassword(password, salt);
        if (userPassword.equals(cryptPassword)) {
            return UUID.randomUUID().toString();
        }
        return null;
    }
}



