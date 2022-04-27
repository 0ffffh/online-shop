package com.k0s.util;

import com.k0s.web.util.ClasspathFileReader;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertiesReader{
    private static final String DEFAULT_CONFIG_PATH = "application.properties";
    private final Properties properties = new Properties();

    public PropertiesReader(String path) {
        readProperties(Objects.requireNonNullElse(path, DEFAULT_CONFIG_PATH));
    }

    public Properties getProperties() {
        return this.properties;
    }


    private void readProperties(String path) {
            try (InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream(path)) {
                properties.load(inputStream);
            } catch (IOException e) {
                log.info("Load config failed", e);
                throw new RuntimeException(e);
            }
        }
}


