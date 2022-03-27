package com.k0s.dao.jdbc;

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

    public Connection getConnection() {
        try{
            return getHerokuConnection();
//            return getLocalConnection();

        } catch (SQLException e) {
            e.printStackTrace();
//            log.info("Can't connect to database " + properties.getProperty("url") + " try connect to local default database...");
            log.error("Can't connect to database URL =  {} try connect to local default database.", properties.getProperty("local.url"));
            try {
                return getLocalConnection();
            } catch (Exception ex) {
                log.error("Can't connect to local database URL = {}, {}",properties.getProperty("local.url"), ex);
                throw new RuntimeException("Can't connect to local database ");
            }
        }
    }

    private Connection getLocalConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("local.url"),
                properties.getProperty("local.user"),
                properties.getProperty("local.password"));
    }

    private Connection getHerokuConnection() throws  SQLException {
        try{
            return DriverManager.getConnection(
                    properties.getProperty("heroku.url"),
                    properties.getProperty("heroku.user"),
                    properties.getProperty("heroku.password"));
        } catch (SQLException e){
            e.printStackTrace();
            log.error("Can't connect to database URL = {} from config file, try connect to environment database URL =  {}", properties.getProperty("heroku.url"), System.getenv("JDBC_DATABASE_URL"));
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            return DriverManager.getConnection(dbUrl);
        }

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
