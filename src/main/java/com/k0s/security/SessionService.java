package com.k0s.security;

import com.k0s.security.user.User;
import com.k0s.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
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
        Optional<User> user = Optional.ofNullable(userService.getUser(name));
        if (user.isPresent()){
            Optional<String> token = Optional.ofNullable(
                    getToken(user.get().getPassword(), password, user.get().getSalt()));
            if (token.isPresent()){
                Session session = new Session();
                session.setUser(user.get());
                session.setToken(token.get());
                session.setExpireDate(LocalDateTime.now().plusSeconds(
                        Long.parseLong(properties.getProperty("security.sessionTimeout"))));
                session.setCart(new ArrayList<>());
                return session;
            }
        }
        return null;
    }


    private String getToken(String userPassword, String password, String salt) {
        String cryptPassword = PasswordCrypt.encryptPassword(password, salt);
        if (userPassword.equals(cryptPassword)) {
            return UUID.randomUUID().toString();
        }
        return null;
    }
}
