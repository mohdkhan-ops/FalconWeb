package com.liftofftech.falcon.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Centralized configuration accessor. Values are resolved in the following priority:
 *  1. System property (-Dkey=value)
 *  2. Environment variable (case-insensitive, with dots converted to underscores)
 *  3. config.properties entry under src/test/resources
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

    /**
     * Gets environment variable value, checking both exact key and key with dots converted to underscores.
     * Also checks uppercase version for common environment variable naming conventions.
     */
    private static String getEnv(String key) {
        // Check exact key
        String value = System.getenv(key);
        if (value != null) {
            return value;
        }
        
        // Check with dots converted to underscores (e.g., "headless" -> "HEADLESS")
        String envKey = key.replace('.', '_').toUpperCase();
        value = System.getenv(envKey);
        if (value != null) {
            return value;
        }
        
        // Check lowercase version
        value = System.getenv(key.toLowerCase());
        if (value != null) {
            return value;
        }
        
        return null;
    }

    public static String get(String key) {
        // Priority 1: System property
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        
        // Priority 2: Environment variable
        value = getEnv(key);
        if (value != null) {
            return value;
        }
        
        // Priority 3: Config file property
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        // Priority 1: System property
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        
        // Priority 2: Environment variable
        value = getEnv(key);
        if (value != null) {
            return value;
        }
        
        // Priority 3: Config file property or default
        return Objects.requireNonNullElse(PROPERTIES.getProperty(key), defaultValue);
    }
}

