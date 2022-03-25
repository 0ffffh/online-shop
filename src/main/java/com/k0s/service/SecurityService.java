package com.k0s.service;

import com.k0s.entity.user.User;
import com.k0s.security.PasswordCrypt;
import com.k0s.security.Session;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityService {
    private final UserService userService;
    private final Map<String, Session> sessionList = new ConcurrentHashMap<>();

    private final Properties applicationProperties;

    public SecurityService(UserService userService, Properties applicationProperties) {
        this.userService = userService;
        this.applicationProperties = applicationProperties;
    }


    public String login(String name, String password) {
        try {
            User user = userService.getUser(name);
            String token = getToken(user.getPassword(), password, user.getSalt());
            sessionList.put(token, new Session(token, user, LocalDateTime.now().plusSeconds(Long.parseLong(applicationProperties.getProperty("security.sessionTimeout")))));
            return token;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public Session getSession(String token) {
        Session session = sessionList.get(token);
        if (isValidSession(session, token)) {
            return session;
        }
        return null;
    }

    public void logout(String token) {
        sessionList.remove(token);
    }

    public Properties getProperties() {
        return applicationProperties;
    }


    private boolean isValidSession(Session session, String token) {
        if (session == null) {
            return false;
        }
        if (LocalDateTime.now().isBefore(session.getExpireDate())) {
            return true;
        }
        sessionList.remove(token);
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



