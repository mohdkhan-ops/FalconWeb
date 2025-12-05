package com.liftofftech.falcon.core.driver;

import java.util.Locale;

public enum PlatformType {
    DWEB,
    MWEB;

    public static PlatformType from(String value) {
        if (value == null) {
            return DWEB;
        }
        return PlatformType.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}

