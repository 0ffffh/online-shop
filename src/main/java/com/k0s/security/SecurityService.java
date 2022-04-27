package com.k0s.security;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

    public String login(String name, String password) {
        Session session = sessionService.getSession(name, password);
        if (session == null) {
            return null;
        }

        sessionList.add(session);
        return session.getToken();
    }

    public Session getSession(String token) {
        Session session = sessionList.stream().filter(e -> token.equals(e.getToken())).findFirst().orElse(null);

        if (isValidSession(session)) {
            return session;
        }
        return null;
    }

    public void logout(String token) {
        sessionList.removeIf(e -> token.equals(e.getToken()));
    }

    public List<String> getSkipList() {
        return Arrays.asList(properties.getProperty("security.skipPath").split(","));
    }

    public int getSessionMaxAge() {
        return Integer.parseInt(properties.getProperty("security.sessionTimeout"));
    }

    private boolean isValidSession(Session session) {
        if (session == null) {
            return false;
        }
        if (LocalDateTime.now().isBefore(session.getExpireDate())) {
            return true;
        }
        sessionList.remove(session);
        return false;
    }

    private void scheduleClearSessionList(long delay, long period) {
        schedule.scheduleAtFixedRate(this::clearSessionList, delay, period, TimeUnit.MINUTES);
    }

    private void clearSessionList() {
        log.info("Clearing session list");
        int countSessions = sessionList.size();
        sessionList.removeIf(e -> LocalDateTime.now().isAfter(e.getExpireDate()));
        log.info("Deleted {} inactive sessions", countSessions - sessionList.size());
    }

}



