package com.k0s.dao.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
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
    private static final String DATABASE_FLAG = "local";
    private final HikariDataSource dataSource;

    public ConnectionFactory(Properties properties) {
        dataSource = new HikariDataSource(getConfig(properties));
    }

    @Override
    @SneakyThrows
    public Connection getConnection() {
        return dataSource.getConnection();
    }

    @SneakyThrows
    private @NonNull HikariConfig getConfig(@NonNull Properties properties) {
        if (properties.getProperty(DATABASE_FLAG) != null) {
            log.info("Connecting to local database: {}", properties.getProperty("local.url"));
            return setConfig(properties.getProperty("local.url"),
                    properties.getProperty("local.user"),
                    properties.getProperty("local.password"));
        }

        String env = System.getenv("DATABASE_URL");
        if (env == null) {
            throw new RuntimeException("System variable $DATABASE_URL not set. Set $DATABASE_URL or use local database (set <local> flag in application properties).");
        }
        log.info("Connecting to {}", env);
        URI dbUri = new URI(env);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return setConfig(dbUrl, username, password);
    }


    private @NonNull HikariConfig setConfig(String dbUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(username);
        config.setPassword(password);
        return config;
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
