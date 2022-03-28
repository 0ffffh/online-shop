package com.k0s.dao.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Slf4j
public class ConnectionFactory implements DataSource {
    private final Properties properties;

    public ConnectionFactory(Properties properties){
        this.properties = properties;
    }

    @SneakyThrows
    public Connection getConnection() {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl == null){
            log.info("Connecting to local database URL = {}", properties.getProperty("local.url"));
            try {
                return getLocalConnection();
            } catch (SQLException e){
                log.error("Connection to local database URL = {}, fail :  {}",properties.getProperty("local.url"), e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return DriverManager.getConnection(dbUrl);
    }

    private Connection getLocalConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("local.url"),
                properties.getProperty("local.user"),
                properties.getProperty("local.password"));
    }


    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
