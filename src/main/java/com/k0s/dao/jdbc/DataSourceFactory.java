package com.k0s.dao.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.net.URI;
import java.util.Properties;

@Slf4j
public class DataSourceFactory {
    private static final String DATABASE_FLAG = "local";

    public DataSource createDataSource(Properties properties){
        return new HikariDataSource(getConfig(properties));
    }

    public DataSource createDataSource(String dbUrl, String username, String password){
        return new HikariDataSource(setConfig(dbUrl, username, password));
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

}
