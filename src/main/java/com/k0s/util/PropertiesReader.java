package com.k0s.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private static final String DEFAULT_DB_CONFIG_PATH = "db/db.properties";
    private final String path;
    private final Properties properties = new Properties();

    public PropertiesReader(String path) {
        this.path = path;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void readProperties() {
        File file = new File(this.path);
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            properties.load(inputStream);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Can't read config file " + file.getAbsolutePath() + " try load default config " + DEFAULT_DB_CONFIG_PATH);
            try (InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream(DEFAULT_DB_CONFIG_PATH)) {
                properties.load(inputStream);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Cant load config DB properties " + e);
            }
        }
    }

    public void readProperties(String path) {
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            properties.load(inputStream);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Can't read config file " + file.getAbsolutePath());
        }
    }
}

