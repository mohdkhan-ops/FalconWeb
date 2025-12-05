package com.liftofftech.falcon.core.config;

import com.liftofftech.falcon.core.driver.BrowserType;
import com.liftofftech.falcon.core.driver.ModuleType;
import com.liftofftech.falcon.core.driver.PlatformType;

import java.time.Duration;

public final class FrameworkConfig {

    private FrameworkConfig() {
        // utility
    }

    public static BrowserType browser() {
        return BrowserType.from(ConfigManager.get("browser", "chrome"));
    }

    public static PlatformType platform() {
        return PlatformType.from(ConfigManager.get("platform", "dweb"));
    }

    public static String baseUrl() {
        return ConfigManager.get("base.url");
    }

    public static boolean remoteExecution() {
        return Boolean.parseBoolean(ConfigManager.get("remote", "false"));
    }

    public static String gridUrl() {
        return ConfigManager.get("grid.url", "http://localhost:4444/wd/hub");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(ConfigManager.get("headless", "false"));
    }

    public static Duration implicitWait() {
        return Duration.ofSeconds(Long.parseLong(ConfigManager.get("implicit.wait", "0")));
    }

    public static Duration pageLoadTimeout() {
        return Duration.ofSeconds(Long.parseLong(ConfigManager.get("page.load.timeout", "30")));
    }

    public static boolean screenshotOnFailure() {
        return Boolean.parseBoolean(ConfigManager.get("screenshot.on.failure", "true"));
    }

    public static String mobileDeviceName() {
        return ConfigManager.get("mobile.device.name", "Pixel 7");
    }

    public static int mobileWidth() {
        return Integer.parseInt(ConfigManager.get("mobile.width", "412"));
    }

    public static int mobileHeight() {
        return Integer.parseInt(ConfigManager.get("mobile.height", "915"));
    }

    public static double mobilePixelRatio() {
        return Double.parseDouble(ConfigManager.get("mobile.pixel.ratio", "2.63"));
    }

    public static String mobileUserAgent() {
        return ConfigManager.get("mobile.user.agent",
                "Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome Mobile Safari/537.36");
    }

    /**
     * Returns the current environment (stage, prod, or local).
     * 
     * @return environment name
     */
    public static String environment() {
        return ConfigManager.get("environment", "local");
    }

    /**
     * Returns the base domain for the current environment.
     * - Prod: galaxy.ai
     * - Stage: rework.vip
     * - Local: galaxy.ai (default)
     * 
     * @return base domain
     */
    public static String baseDomain() {
        return ConfigManager.get("base.domain", "galaxy.ai");
    }

    /**
     * Returns the subdomain for a given module.
     * Looks up configuration: module.{moduleName}.subdomain
     * 
     * @param module the module type
     * @return subdomain (e.g., "image", "video", "audio")
     */
    public static String moduleSubdomain(ModuleType module) {
        String configKey = "module." + module.getConfigKey() + ".subdomain";
        return ConfigManager.get(configKey);
    }

    /**
     * Returns the base path for a given module.
     * Looks up configuration: module.{moduleName}.base.path
     * 
     * @param module the module type
     * @return base path (e.g., "/ai-image-generator", "/ai-video-generator")
     */
    public static String moduleBasePath(ModuleType module) {
        String configKey = "module." + module.getConfigKey() + ".base.path";
        return ConfigManager.get(configKey);
    }

    /**
     * Returns whether email notifications are enabled.
     * 
     * @return true if email is enabled, false otherwise
     */
    public static boolean emailEnabled() {
        return Boolean.parseBoolean(ConfigManager.get("email.enabled", "false"));
    }

    /**
     * Returns SMTP host for email.
     * 
     * @return SMTP host
     */
    public static String emailSmtpHost() {
        return ConfigManager.get("email.smtp.host", "smtp.gmail.com");
    }

    /**
     * Returns SMTP port for email.
     * 
     * @return SMTP port
     */
    public static String emailSmtpPort() {
        return ConfigManager.get("email.smtp.port", "587");
    }

    /**
     * Returns email username (sender email).
     * 
     * @return email username
     */
    public static String emailUsername() {
        return ConfigManager.get("email.username");
    }

    /**
     * Returns email password or app password.
     * 
     * @return email password
     */
    public static String emailPassword() {
        return ConfigManager.get("email.password");
    }

    /**
     * Returns sender email address.
     * 
     * @return sender email
     */
    public static String emailFrom() {
        return ConfigManager.get("email.from", emailUsername());
    }

    /**
     * Returns comma-separated list of recipient email addresses.
     * 
     * @return recipient emails
     */
    public static String emailRecipients() {
        return ConfigManager.get("email.recipients");
    }

    /**
     * Returns email subject line.
     * 
     * @return email subject
     */
    public static String emailSubject() {
        String environment = environment();
        return ConfigManager.get("email.subject", 
            "Falcon Test Execution Report - " + environment.toUpperCase());
    }
}

