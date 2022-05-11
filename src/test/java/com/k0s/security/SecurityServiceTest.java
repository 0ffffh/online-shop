package com.k0s.security;

import com.k0s.dao.jdbc.DataSourceFactory;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.security.user.Role;
import com.k0s.service.UserService;
import com.k0s.util.PropertiesReader;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    private static PropertiesReader propertiesReader;
    private static DataSourceFactory dataSourceFactory;
    private static DataSource dataSource;
    private static JdbcUserDao jdbcUserDao;
    private static UserService userService;
    private static SecurityService securityService;
    private static SessionService sessionService;

    @BeforeAll
    static void setUp() {


        propertiesReader = new PropertiesReader("test-db.properties");

        Properties properties = propertiesReader.getProperties();
        dataSourceFactory = new DataSourceFactory();
        dataSource = dataSourceFactory.createDataSource(properties);
        jdbcUserDao = new JdbcUserDao(dataSource);
        userService = new UserService(jdbcUserDao);
        sessionService = new SessionService(userService, properties);

        securityService = new SecurityService(sessionService, properties);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();
        flyway.migrate();
    }

    @Test
    void testSecurityService() {
        Session session = securityService.login("user", "user");

        assertNotNull(session);
        assertEquals("user", session.getUser().getName());
        assertEquals(Role.USER, session.getUser().getRole());

        Session actual = securityService.getSession(session.getToken());

        assertEquals(actual.getToken(), session.getToken());
        assertEquals(actual.getUser().getName(), session.getUser().getName());
        assertEquals(actual.getUser().getRole(), session.getUser().getRole());

        securityService.logout(session.getToken());

        actual = securityService.getSession(session.getToken());
        assertNull(actual);


        Session badUser = securityService.login("Ivan", "password");
        assertNull(badUser);

        Session badpassword = securityService.login("user", "password");
        assertNull(badUser);

    }


}