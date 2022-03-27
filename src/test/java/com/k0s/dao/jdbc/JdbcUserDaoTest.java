package com.k0s.dao.jdbc;


import com.k0s.dao.UserDao;

import com.k0s.entity.user.Role;
import com.k0s.entity.user.User;
import com.k0s.util.PropertiesReader;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


class JdbcUserDaoTest {

    PropertiesReader propertiesReader;
    ConnectionFactory connectionFactory;
    UserDao userDao;


    @BeforeEach
    void setUp() throws SQLException {

        propertiesReader = new PropertiesReader("test-db.properties");
        propertiesReader.readProperties();
        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        userDao = new JdbcUserDao(connectionFactory);
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection);

        Flyway flyway = Flyway.configure()
                .dataSource(connectionFactory)
                .load();
        flyway.migrate();

    }

    @Test
    void getUser() {
        String userName = "user";
        User user = userDao.getUser(userName);
        System.out.println(user);

        assertEquals(userName, user.getName());
        assertEquals(Role.USER, user.getRole());


        assertThrows(RuntimeException.class, () -> userDao.getUser("notExistUser"));
        assertThrows(RuntimeException.class, () -> userDao.getUser(null));
    }
}