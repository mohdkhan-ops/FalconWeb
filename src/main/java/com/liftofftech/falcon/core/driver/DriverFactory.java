package com.liftofftech.falcon.core.driver;

import com.liftofftech.falcon.core.config.FrameworkConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class DriverFactory {

    private DriverFactory() {
        // utility
    }

    public static WebDriver createDriver() {
        BrowserType browser = FrameworkConfig.browser();
        if (FrameworkConfig.remoteExecution()) {
            return createRemoteDriver(browser);
        }
        return createLocalDriver(browser);
    }

    private static WebDriver createLocalDriver(BrowserType browser) {
        return switch (browser) {
            case EDGE -> buildEdgeDriver();
            case CHROME -> buildChromeDriver();
        };
    }

    private static WebDriver createRemoteDriver(BrowserType browser) {
        try {
            return switch (browser) {
                case EDGE -> new RemoteWebDriver(new URL(FrameworkConfig.gridUrl()), edgeOptions());
                case CHROME -> new RemoteWebDriver(new URL(FrameworkConfig.gridUrl()), chromeOptions());
            };
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Grid URL is invalid: " + FrameworkConfig.gridUrl(), e);
        }
    }

    private static WebDriver buildChromeDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(chromeOptions());
    }

    private static WebDriver buildEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver(edgeOptions());
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        applyCommonOptions(options);
        if (FrameworkConfig.platform() == PlatformType.MWEB) {
            Map<String, Object> mobileEmulation = new HashMap<>();
            
            // Chrome mobile emulation: Use either deviceName OR deviceMetrics+userAgent, not both
            // Option 1: Use predefined device name (simpler, uses Chrome's built-in device metrics)
            String deviceName = FrameworkConfig.mobileDeviceName();
            if (deviceName != null && !deviceName.isEmpty()) {
                mobileEmulation.put("deviceName", deviceName);
            } else {
                // Option 2: Use custom device metrics and user agent
                Map<String, Object> deviceMetrics = new HashMap<>();
                deviceMetrics.put("width", FrameworkConfig.mobileWidth());
                deviceMetrics.put("height", FrameworkConfig.mobileHeight());
                deviceMetrics.put("pixelRatio", FrameworkConfig.mobilePixelRatio());
                mobileEmulation.put("deviceMetrics", deviceMetrics);
                mobileEmulation.put("userAgent", FrameworkConfig.mobileUserAgent());
            }

            options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }
        return options;
    }

    private static EdgeOptions edgeOptions() {
        EdgeOptions options = new EdgeOptions();
        applyCommonOptions(options);
        return options;
    }

    private static void applyCommonOptions(ChromiumOptions<?> options) {
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        // Reduce resource usage for parallel execution
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-extensions");
        // Prevent browser crashes during parallel execution
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        if (FrameworkConfig.headless()) {
            options.addArguments("--headless=new");
        }
    }
}

