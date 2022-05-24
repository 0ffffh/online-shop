//package com.k0s.dao.jdbc;
//
//import org.junit.jupiter.api.Test;
//
//import javax.sql.DataSource;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.Properties;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class DataSourceFactoryTest {
//
//    @Test
//    void createDataSource() throws SQLException {
//        DataSourceFactory dataSourceFactory = new DataSourceFactory();
//        DataSource dataSource = dataSourceFactory.createDataSource("jdbc:h2:mem:test", "user", "password");
//        assertNotNull(dataSource);
//
//        Connection connection = dataSource.getConnection();
//        assertNotNull(connection);
//        connection.close();
//    }
//
//    @Test
//    void testCreateDataSource() throws SQLException {
//        DataSourceFactory dataSourceFactory = new DataSourceFactory();
//        Properties properties = new Properties();
//
//        properties.setProperty("local", "");
//        properties.setProperty("local.url", "jdbc:h2:mem:test");
//        properties.setProperty("local.user", "user");
//        properties.setProperty("local.password", "password");
//        DataSource dataSource = dataSourceFactory.createDataSource(properties);
//        assertNotNull(dataSource);
//
//        Connection connection = dataSource.getConnection();
//        assertNotNull(connection);
//        connection.close();
//    }
//}