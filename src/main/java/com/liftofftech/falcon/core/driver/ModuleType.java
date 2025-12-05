package com.liftofftech.falcon.core.driver;

/**
 * Enum representing different modules in the application.
 * Each module maps to a configuration key used for URL construction.
 */
public enum ModuleType {
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio");

    private final String configKey;

    ModuleType(String configKey) {
        this.configKey = configKey;
    }

    /**
     * Returns the configuration key for this module.
     * Used to lookup module-specific settings in config files.
     * 
     * @return configuration key (e.g., "image", "video", "audio")
     */
    public String getConfigKey() {
        return configKey;
    }
}

