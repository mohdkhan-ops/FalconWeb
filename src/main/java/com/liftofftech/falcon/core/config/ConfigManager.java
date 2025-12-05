package com.liftofftech.falcon.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Centralized configuration accessor. Values are resolved in the following priority:
 *  1. System property (-Dkey=value)
 *  2. config.properties entry under src/test/resources
 */
public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    static {
        load();
    }

    private ConfigManager() {
        // utility
    }

    private static void load() {
        String configFile = System.getProperty("config.file", "property.config");
        try (InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(configFile)) {

            if (stream == null) {
                throw new IllegalStateException("Unable to locate configuration file: " + configFile);
            }
            PROPERTIES.load(stream);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read configuration file", e);
        }
    }

    public static String get(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    public static String get(String key, String defaultValue) {
        return System.getProperty(key,
                Objects.requireNonNullElse(PROPERTIES.getProperty(key), defaultValue));
    }
}

