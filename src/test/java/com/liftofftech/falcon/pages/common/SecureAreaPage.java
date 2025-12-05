package com.liftofftech.falcon.pages.common;

import com.liftofftech.falcon.core.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class SecureAreaPage extends BasePage {

    private static final By HEADER = By.tagName("h2");

    @Step("Verify secure area banner is visible")
    public String getHeader() {
        return waitUntilVisible(HEADER).getText();
    }
}

