package com.liftofftech.falcon.core.reporting;

import com.liftofftech.falcon.core.driver.DriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.nio.charset.StandardCharsets;

public final class AllureAttachments {

    private AllureAttachments() {
        // utility
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public static byte[] attachScreenshot() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page Source", type = "text/html")
    public static byte[] attachPageSource() {
        return DriverManager.getDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
    }
}

