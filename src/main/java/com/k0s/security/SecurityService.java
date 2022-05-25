package com.k0s.security;


import com.k0s.security.user.User;
import com.k0s.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class SecurityService {

    private final UserService userService;

    private final List<Session> sessionList = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

    @Value("${security.sessionTimeout}")
    private String securitySessionTimeout;

    @Value("${session.clearPeriod}")
    private String sessionClearPeriod;

    @Value("${session.clearDelay}")
    private String sessionClearDelay;

    @Value("${security.skipPath}")
    private String skipPaths;


    public SecurityService(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    private void scheduleClearSessionList() {
        schedule.scheduleAtFixedRate(this::clearSessionList, Long.parseLong(sessionClearDelay), Long.parseLong(sessionClearPeriod), TimeUnit.MINUTES);
    }


    public Session login(String name, String password) {
        Optional<Session> optionalSession = Optional.ofNullable(createSession(name, password));
        if (optionalSession.isPresent()) {
            sessionList.add(optionalSession.get());
            return optionalSession.get();
        }
        return null;
    }

    public Session getSession(String token) {
        Optional<Session> optionalSession = sessionList.stream().filter(e -> token.equals(e.getToken())).findFirst();
        if (optionalSession.isPresent()) {
            if (!removeIfInvalidSession(optionalSession.get())) {
                return optionalSession.get();
            }
        }
        return null;
    }

    public void logout(String token) {
        sessionList.removeIf(e -> token.equals(e.getToken()));
    }

    public List<String> getSkipPathList() {
        return Arrays.stream(skipPaths.split(",")).toList();
    }

    private void clearSessionList() {
        log.info("Clearing session list");
        for (Session session : sessionList) {
            if (removeIfInvalidSession(session)) {
                log.info("Removed inactive {}  session. User <{}> expire date {}", session.getUser().getRole(), session.getUser().getName(), session.getExpireDate().toString());
            }
        }
//        sessionList.removeIf(e -> LocalDateTime.now().isAfter(e.getExpireDate()));
    }

    private boolean removeIfInvalidSession(Session session) {
        if (session.getExpireDate().isBefore(LocalDateTime.now())) {
            return sessionList.remove(session);
        }
        return false;
    }


    private Session createSession(String name, String password) {
        Optional<User> user = Optional.ofNullable(userService.getUser(name));
        if (user.isPresent()) {
            Optional<String> token = Optional.ofNullable(getToken(user.get().getPassword(), password, user.get().getSalt()));
            if (token.isPresent()) {
                Session session = new Session();
                session.setUser(user.get());
                session.setToken(token.get());
                session.setExpireDate(LocalDateTime.now().plusSeconds(
                        Long.parseLong(securitySessionTimeout)));
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



