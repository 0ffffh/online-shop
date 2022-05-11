package com.k0s.security;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SecurityService {
    private final SessionService sessionService;
    private final List<Session> sessionList = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
    private final Properties properties;

    public SecurityService(SessionService sessionService, Properties properties) {
        this.sessionService = sessionService;
        this.properties = properties;
        scheduleClearSessionList(
                Long.parseLong(properties.getProperty("session.clearPeriod")),
                Long.parseLong(properties.getProperty("session.clearDelay")));
    }


    public Session login(String name, String password) {
        Optional<Session> optionalSession = Optional.ofNullable(sessionService.getSession(name, password));
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

    public List<String> getSkipList() {
        return Arrays.asList(properties.getProperty("security.skipPath").split(","));
    }

    private void scheduleClearSessionList(long delay, long period) {
        schedule.scheduleAtFixedRate(this::clearSessionList, delay, period, TimeUnit.MINUTES);
    }

    private void clearSessionList() {
        log.info("Clearing session list");
        for (Session session : sessionList) {
            if (removeIfInvalidSession(session)) {
                log.info("Removed inactive {}  session. User <{}> expire date {}",
                        session.getUser().getRole(), session.getUser().getName(), session.getExpireDate().toString());
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

}



