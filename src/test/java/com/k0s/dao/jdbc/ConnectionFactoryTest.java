package com.k0s.dao.jdbc;

import com.k0s.util.PropertiesReader;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ConnectionFactoryTest {

    @Test
    void getConnection() {
        Connection conn = mock(Connection.class);
        Properties properties = mock(Properties.class);
        MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class);

        driverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(conn);
        when(properties.getProperty(any())).thenReturn(anyString());


        ConnectionFactory connectionFactory = new ConnectionFactory(properties);
        Connection connection = connectionFactory.getConnection();

        driverManager.verify(() -> DriverManager.getConnection(anyString(), anyString(), anyString()));
        verify(properties, times(3)).getProperty(anyString());
        assertNotNull(connection);

    }

    @Test
    void getConnectionH2() {
        PropertiesReader propertiesReader = new PropertiesReader("test-db.properties");
        propertiesReader.readProperties();
        Properties properties = propertiesReader.getProperties();
        ConnectionFactory connectionFactory = new ConnectionFactory(properties);
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
}