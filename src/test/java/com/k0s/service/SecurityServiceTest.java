package com.k0s.service;


import com.k0s.dao.UserDao;
import com.k0s.dao.jdbc.ConnectionFactory;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.security.user.Role;
import com.k0s.security.SecurityService;
import com.k0s.security.Session;
import com.k0s.security.SessionService;
import com.k0s.util.PropertiesReader;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    static PropertiesReader propertiesReader;
    static ConnectionFactory connectionFactory;
//    static Dao<User> userDao;
    static UserDao userDao;

    static UserService userService;

    static SecurityService securityService;

    static SessionService sessionService;



//    @BeforeEach
    @BeforeAll
    static void setUp()  {

        propertiesReader = new PropertiesReader("test-db.properties");

        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        userDao = new JdbcUserDao(connectionFactory);
        userService = new UserService(userDao);
        sessionService = new SessionService(userService, properties);
        securityService = new SecurityService(sessionService, propertiesReader.getProperties());
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection);

        Flyway flyway = Flyway.configure()
                .dataSource(connectionFactory)
                .load();
        flyway.migrate();

    }

//    @Test
//    @DisplayName("login() test")
//    void login() {
//        assertNotNull(securityService.login("user", "user"));
//        assertNull(securityService.login("notRegisteredUser", "user"));
//        assertNull(securityService.login(null, "user"));
////        assertNull(securityService.login("user", null));
//
//    }

    @Test
    void getSession() {
        String token = securityService.login("user", "user");
        Session session = securityService.getSession(token);
        assertNotNull(session);
        assertEquals("user", session.getUser().getName());
        assertEquals(Role.USER, session.getUser().getRole());

        assertNull(securityService.getSession("some token"));

    }

    @Test
    void logout() {
        String token = securityService.login("user", "user");
        Session session = securityService.getSession(token);
        assertNotNull(session);

        securityService.logout(token);
        assertNull(securityService.getSession(token));

    }

    @Test
    void Clear() throws InterruptedException {
//        String token = securityService.login("user", "user");
//        Session session = securityService.getSession(token);
//        assertNotNull(session);
//        assertEquals("user", session.getUser().getName());
//        assertEquals(Role.USER, session.getUser().getRole());
//
//        assertNull(securityService.getSession("some token"));

//        for (int i = 0; i < 100; i++) {
//            securityService.login("user", "user");
//        }

//        ExecutorService executorService = Executors.newFixedThreadPool(100);
//
//        for (int i = 0; i < 1000; i++) {
//            executorService.execute(() -> {
//                System.out.println(securityService.login("user", "user"));
//            });
//        }
//
//        ExecutorService executorService2 = Executors.newFixedThreadPool(100);
//        for (int i = 0; i < 1000; i++) {
//            executorService2.execute(()->
//                    System.out.println(userService.getUser("root")));
//        }


//
//        executorService.shutdown();
//
//        Thread.sleep(5000L);
////
//        Map sessionlist = securityService.getSessionlist();
//        System.out.println(sessionlist.size());
//        securityService.scheduleClear();
//
//        Thread.sleep(2000L);
//        sessionlist = securityService.getSessionlist();
//        System.out.println(sessionlist.size());


    }

}