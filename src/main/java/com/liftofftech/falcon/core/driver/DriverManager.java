package com.liftofftech.falcon.core.driver;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
        // utility
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized for this thread.");
        }
        return driver;
    }

    public static void unload() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                // Log but don't throw - driver cleanup should not fail tests
                System.err.println("Warning: Error during driver cleanup: " + e.getMessage());
            } finally {
                DRIVER.remove();
            }
        }
    }
}

