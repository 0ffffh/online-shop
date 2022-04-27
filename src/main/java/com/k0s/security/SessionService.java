package com.k0s.security;

import com.k0s.security.user.User;
import com.k0s.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

public class SessionService {
    private final UserService userService;
    protected final Properties properties;

    public SessionService(UserService userService, Properties properties) {
        this.userService = userService;
        this.properties = properties;
    }

    public Session getSession(String name, String password) {
        User user = userService.getUser(name);
        if(user == null){
            return null;
        }
        String token = getToken(user.getPassword(), password, user.getSalt());
        return token == null ? null : Session.builder()
                .token(token)
                .user(user)
                .expireDate(LocalDateTime.now().plusSeconds(Long.parseLong(properties.getProperty("security.sessionTimeout"))))
                .cart(new ArrayList<>())
                .build();
    }


    private String getToken(String userPassword, String password, String salt) {
        String cryptPassword = PasswordCrypt.encryptPassword(password, salt);
        if (userPassword.equals(cryptPassword)) {
            return UUID.randomUUID().toString();
        }
        return null;
    }
}
