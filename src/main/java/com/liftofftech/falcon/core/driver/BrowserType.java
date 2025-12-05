package com.liftofftech.falcon.core.driver;

import java.util.Locale;

public enum BrowserType {
    CHROME,
    EDGE;

    public static BrowserType from(String value) {
        if (value == null) {
            return CHROME;
        }
        return BrowserType.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}

