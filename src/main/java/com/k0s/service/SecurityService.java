package com.k0s.service;

import com.k0s.entity.user.User;
import com.k0s.security.PasswordCrypt;
import com.k0s.security.Session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SecurityService {
    private final UserService userService;
    //    private final List<Session> sessionList = new ArrayList<>();
    private final Map<String, Session> sessionList = new HashMap<>();

    public SecurityService(UserService userService) {
        this.userService = userService;
    }


    public String login(String name, String password) throws IllegalAccessException {

        User user = userService.getUser(name);

        String token = getToken(user.getPassword(), password, user.getSalt());

        if (token == null) {
            throw new IllegalAccessException("Access denied");
        }

//        sessionList.add(new Session(token, user, LocalDateTime.now().plusMinutes(60)));
        sessionList.put(token, new Session(token, user, LocalDateTime.now().plusMinutes(60)));
        return token;
    }

    public Session getSession(String token) {
        Session session = sessionList.get(token);
        if (isValidSession(session)){
            return session;
        }
        return null;
    }

    private boolean isValidSession(Session session) {
        if (session == null) {
            return false;
        }
        if (LocalDateTime.now().isBefore(session.getExpireDate())) {
            return true;
        } else {
            sessionList.remove(session);
        }
        return false;
    }


//    public boolean isValidSession(String token) {
//        if (token == null) {
//            return false;
//        }
//
//        //TODO: сделать через forach, както чистить sessionList
//        for (int i = 0; i < sessionList.size(); i++) {
//            if (token.equals(sessionList.get(i).getToken())) {
//                if (LocalDateTime.now().isBefore(sessionList.get(i).getExpireDate())) {
//                    return true;
//                } else {
//                    sessionList.remove(i);
//                }
//            }
//        }
//        System.out.println("Session invalid!");
//        return false;
//    }


    private String getToken(String userPassword, String password, String salt) {
        String cryptPassword = PasswordCrypt.encryptPassword(password, salt);
        if (userPassword.equals(cryptPassword)) {
            return UUID.randomUUID().toString();
        }
        return null;
    }

}



