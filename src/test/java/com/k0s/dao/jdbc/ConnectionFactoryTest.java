package com.k0s.dao.jdbc;

import com.k0s.util.PropertiesReader;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.w3c.dom.stylesheets.LinkStyle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ConnectionFactoryTest {

    @Test
    void getConnection() {
        PropertiesReader propertiesReader = new PropertiesReader("test-db.properties");

        Properties properties = propertiesReader.getProperties();
        ConnectionFactory connectionFactory = new ConnectionFactory(properties);
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection);
    }
}