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
    private final Properties properties = new Properties();

    public PropertiesReader(String path) {
        readProperties(path);
    }

    public Properties getProperties() {
        return this.properties;
    }


    private void readProperties(String path) {
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("Can't read config file {}", file.getAbsolutePath(), e);
            log.info("Try load default config");
            try (InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH)) {
                properties.load(inputStream);
            } catch (IOException ex) {
                log.error("Load config failed");
                throw new RuntimeException(ex);
            }
        }
    }
}

