package com.k0s.service;

import com.k0s.entity.user.User;
import com.k0s.security.PasswordCrypt;
import com.k0s.security.Session;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SecurityService {
    private final UserService userService;
    private final Map<String, Session> sessionList = new ConcurrentHashMap<>();

    private final Properties properties;

    public SecurityService(UserService userService, Properties properties) {
        this.userService = userService;
        this.properties = properties;
    }


    public String login(String name, String password) {
        try {
            User user = userService.getUser(name);
            String token = getToken(user.getPassword(), password, user.getSalt());
            sessionList.put(token, Session.builder()
                    .token(token)
                    .user(user)
                    .expireDate(LocalDateTime.now().plusSeconds(Long.parseLong(properties.getProperty("security.sessionTimeout"))))
                    .cart(new ArrayList<>())
                    .build());
            return token;
        } catch (RuntimeException e) {
            log.info("User <{}> login fail: {}", name, e.getMessage());
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
        return properties;
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



