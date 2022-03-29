package com.k0s.dao.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Slf4j
public class ConnectionFactory implements DataSource {
    private final HikariConfig config = new HikariConfig();
    private final HikariDataSource ds;
    private final Properties properties;

    @SneakyThrows
    public ConnectionFactory(Properties properties){
        this.properties = properties;
        setConnectionConfig(System.getenv("DATABASE_URL"));
        ds = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            log.info("Connection error", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private void setConnectionConfig(String env){
        if(env == null){
            config.setJdbcUrl(properties.getProperty("local.url"));
            config.setUsername(properties.getProperty("local.user") );
            config.setPassword(properties.getProperty("local.password"));
        } else {
            URI dbUri = new URI(env);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            config.setJdbcUrl( dbUrl );
            config.setUsername( username );
            config.setPassword( password );
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
