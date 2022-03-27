package com.k0s.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesReader {
    private static final String DEFAULT_CONFIG_PATH = "application.properties";
    private String path;
    private final Properties properties = new Properties();

    public PropertiesReader(){}
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
            log.info("Can't read config file {} try load default config {}", file.getAbsolutePath(), DEFAULT_CONFIG_PATH);
            try (InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH)) {
                properties.load(inputStream);
            } catch (IOException ex) {
                ex.printStackTrace();
                log.error("Cant load DB config", ex);
                throw new RuntimeException("Cant load DB config");
            }
        }
    }

    public void readProperties(String path) {
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            properties.load(inputStream);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            log.info("Can't read config file {}", file.getAbsolutePath());
        }
    }
}

