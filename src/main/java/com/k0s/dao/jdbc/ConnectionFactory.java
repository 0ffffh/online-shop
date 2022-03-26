package com.k0s.dao.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionFactory implements DataSource {
    private final Properties properties;

    public ConnectionFactory(Properties properties){
        this.properties = properties;
    }

    public Connection getConnection() {
        try{
            return getHerokuConnection();

//            return DriverManager.getConnection(
//                properties.getProperty("url"),
//                properties.getProperty("user"),
//                properties.getProperty("password"));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Can't connect to database " + properties.getProperty("url") + " try connect to heroku db...");
            try {
                return getHerokuConnection();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Can't connect to database ");
            }


        }
    }

    private Connection getHerokuConnection() throws  SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
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
