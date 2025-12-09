package com.liftofftech.falcon.core.base;

import com.liftofftech.falcon.core.config.FrameworkConfig;
import com.liftofftech.falcon.core.driver.DriverFactory;
import com.liftofftech.falcon.core.driver.DriverManager;
import com.liftofftech.falcon.core.driver.PlatformType;
import com.liftofftech.falcon.core.reporting.AllureAttachments;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        // Small delay to prevent resource contention in parallel execution
        try {
            Thread.sleep(100 * Thread.currentThread().getId() % 3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        WebDriver driver = DriverFactory.createDriver();
        DriverManager.setDriver(driver);
        driver.manage().timeouts().implicitlyWait(FrameworkConfig.implicitWait());
        driver.manage().timeouts().pageLoadTimeout(FrameworkConfig.pageLoadTimeout());

        if (FrameworkConfig.platform() == PlatformType.DWEB) {
            driver.manage().window().maximize();
        }
        
        // Navigate to base URL - module tests should override this method to skip navigation
        // and handle their own module-specific navigation
        navigateToBaseUrl();
    }

    /**
     * Navigates to the base URL. Can be overridden by module tests to skip navigation
     * and handle their own module-specific navigation instead.
     */
    protected void navigateToBaseUrl() {
        DriverManager.getDriver().get(FrameworkConfig.baseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
        if (!result.isSuccess() && FrameworkConfig.screenshotOnFailure() && DriverManager.hasDriver()) {
                try {
            AllureAttachments.attachScreenshot();
            AllureAttachments.attachPageSource();
                } catch (Exception e) {
                    // Ignore screenshot errors during parallel execution
                    System.err.println("Failed to attach screenshot: " + e.getMessage());
                }
            }
        } finally {
            // Always cleanup driver, even if screenshot fails
            try {
         DriverManager.unload();
            } catch (Exception e) {
                // Ignore cleanup errors - driver may already be closed
                System.err.println("Driver cleanup warning: " + e.getMessage());
            }
        }
    }
}

