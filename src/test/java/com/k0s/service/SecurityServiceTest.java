package com.k0s.service;

import com.k0s.dao.UserDao;
import com.k0s.dao.jdbc.ConnectionFactory;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.entity.user.Role;
import com.k0s.security.Session;
import com.k0s.util.PropertiesReader;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    static PropertiesReader propertiesReader;
    static ConnectionFactory connectionFactory;
    static UserDao userDao;
    static UserService userService;

    static SecurityService securityService;



//    @BeforeEach
    @BeforeAll
    static void setUp()  {

        propertiesReader = new PropertiesReader("test-db.properties");
        propertiesReader.readProperties();
        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        userDao = new JdbcUserDao(connectionFactory);
        userService = new UserService(userDao);
        securityService = new SecurityService(userService, propertiesReader.getProperties());
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection);

        Flyway flyway = Flyway.configure()
                .dataSource(properties.getProperty("url"),
                        properties.getProperty("user"),
                        properties.getProperty("password"))
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();

    }

    @Test
    @DisplayName("login() test")
    void login() {
        assertNotNull(securityService.login("user", "user"));
        assertNull(securityService.login("notRegisteredUser", "user"));
        assertNull(securityService.login(null, "user"));
        assertNull(securityService.login("user", null));

    }

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

}