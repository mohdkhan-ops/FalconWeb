package com.liftofftech.falcon.core.base;

import com.liftofftech.falcon.core.config.FrameworkConfig;
import com.liftofftech.falcon.core.driver.DriverManager;
import com.liftofftech.falcon.core.driver.ModuleType;
import com.liftofftech.falcon.core.navigation.ModuleNavigator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, FrameworkConfig.pageLoadTimeout());
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks on an element after waiting for it to be visible.
     * 
     * @param locator the element locator
     */
    public void click(By locator) {
        waitUntilVisible(locator).click();
    }

    /**
     * Types text into an element after clearing it.
     * 
     * @param locator the element locator
     * @param value the text to type
     */
    public void type(By locator, String value) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    /**
     * Clears the text from an input/textarea element.
     * 
     * @param locator the element locator
     */
    

    /**
     * Clears text using keyboard shortcuts.
     * Uses COMMAND+A on Mac, CTRL+A on other platforms.
     * Useful for React/Angular fields where clear() doesn't trigger state changes.
     * 
     * @param locator the element locator
     */
    public void clearWithKeys(By locator) {
        WebElement element = waitUntilVisible(locator);
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            element.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } else {
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        }
        element.sendKeys(Keys.BACK_SPACE);
    }

    /**
     * Types text using JavaScript (avoids keyboard events that might trigger navigation).
     * Useful for large text inputs or when sendKeys is slow/unreliable.
     * Properly triggers React onChange handlers.
     * 
     * @param locator the element locator
     * @param value the text to set
     */
    public void typeUsingJS(By locator, String value) {
        WebElement element = waitUntilPresent(locator);
        // Use native setter to trigger React's onChange handler
        ((JavascriptExecutor) driver).executeScript(
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLTextAreaElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
            "var event = new Event('input', { bubbles: true });" +
            "arguments[0].dispatchEvent(event);" +
            "var changeEvent = new Event('change', { bubbles: true });" +
            "arguments[0].dispatchEvent(changeEvent);",
            element, value);
    }
    

    /**
     * Gets an attribute value from an element.
     * 
     * @param locator the element locator
     * @param attribute the attribute name
     * @return the attribute value
     */
    public String getAttribute(By locator, String attribute) {
        return waitUntilVisible(locator).getAttribute(attribute);
    }

    /**
     * Gets the visible text of an element.
     * 
     * @param locator the element locator
     * @return the visible text
     */
    public String getText(By locator) {
        return waitUntilVisible(locator).getText();
    }

    /**
     * Checks if an element is displayed.
     * 
     * @param locator the element locator
     * @return true if element is displayed
     */
    public boolean isDisplayed(By locator) {
        return waitUntilVisible(locator).isDisplayed();
    }

    /**
     * Waits until element is visible.
     * 
     * @param locator the element locator
     * @return the visible WebElement
     */
    public WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Scrolls to element using JavaScript.
     * Uses scrollIntoView with smooth behavior.
     */
    public void scrollToElement(By locator) {
        WebElement element = waitUntilPresent(locator);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'nearest'});", element);
        // Small wait for scroll animation
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    /**
     * Scrolls to element by moving it to viewport center using coordinate calculation.
     * More reliable for elements that scrollIntoView doesn't work with.
     */
    public void scrollToElementByCoordinates(By locator) {
        WebElement element = waitUntilPresent(locator);
        int yOffset = element.getLocation().getY() - (driver.manage().window().getSize().getHeight() / 2);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, " + yOffset + ");");
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    /**
     * Scrolls down by specified pixels.
     */
    public void scrollByPixels(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + pixels + ");");
    }

    /**
     * Clicks element using JavaScript (bypasses visibility/overlay issues).
     */
    public void clickUsingJS(By locator) {
        WebElement element = waitUntilPresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Clicks element inside Shadow DOM using JavaScript.
     * @param shadowHostSelector CSS selector for the shadow host element
     * @param innerSelector CSS selector for element inside shadow DOM
     */
    public void clickShadowElement(String shadowHostSelector, String innerSelector) {
        String script = 
            "return document.querySelector('" + shadowHostSelector + "')" +
            ".shadowRoot.querySelector('" + innerSelector + "');";
        WebElement element = (WebElement) ((JavascriptExecutor) driver).executeScript(script);
        if (element != null) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } else {
            throw new RuntimeException("Shadow DOM element not found: " + innerSelector);
        }
    }

    /**
     * Gets file input from Shadow DOM and uploads file.
     * @param shadowHostSelector CSS selector for the shadow host element
     * @param filePath absolute path to the file
     */
    public void uploadFileToShadowDOM(String shadowHostSelector, String filePath) {
        String script = 
            "return document.querySelector('" + shadowHostSelector + "')" +
            ".shadowRoot.querySelector('input[type=\"file\"]');";
        WebElement fileInput = (WebElement) ((JavascriptExecutor) driver).executeScript(script);
        if (fileInput != null) {
            fileInput.sendKeys(filePath);
        } else {
            throw new RuntimeException("File input not found in Shadow DOM");
        }
    }

    /**
     * Waits until element is clickable.
     * 
     * @param locator the element locator
     * @return the clickable WebElement
     */
    public WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until element is present in DOM.
     * 
     * @param locator the element locator
     * @return the present WebElement
     */
    public WebElement waitUntilPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Uploads a file by sending the file path to a file input element.
     * Does not wait for visibility since file inputs are typically hidden.
     * 
     * @param locator the file input locator
     * @param filePath the absolute path to the file
     */
    public void uploadFile(By locator, String filePath) {
        WebElement fileInput = waitUntilPresent(locator);
        fileInput.sendKeys(filePath);
    }

    /**
     * Gets the current URL of the browser.
     * 
     * @return the current URL as a string
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Checks if the current URL contains a specific query parameter with expected value.
     * 
     * @param paramName the query parameter name
     * @param expectedValue the expected value
     * @return true if the URL contains the parameter with expected value
     */
    public boolean urlContainsQueryParam(String paramName, String expectedValue) {
        String currentUrl = getCurrentUrl();
        String expectedParam = paramName + "=" + expectedValue;
        return currentUrl.contains(expectedParam);
    }

    /**
     * Waits for the URL to contain a specific string.
     * 
     * @param urlPart the string that should be present in the URL
     */
    public void waitForUrlToContain(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
    }

    /**
     * Gets all elements matching the locator.
     * 
     * @param locator the element locator
     * @return list of WebElements
     */
    public List<WebElement> getElements(By locator) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator);
    }

    /**
     * Gets the text of all elements matching the locator.
     * 
     * @param locator the element locator
     * @return list of text strings from all matching elements
     */
    public List<String> getAllTexts(By locator) {
        return getElements(locator).stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());
    }

    /**
     * Gets the count of elements matching the locator.
     * 
     * @param locator the element locator
     * @return number of elements found
     */
    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    /**
     * Navigates to a module-specific URL with optional query parameters.
     * Uses ModuleNavigator to construct the URL based on current environment configuration.
     * 
     * @param module the module type (IMAGE, VIDEO, AUDIO, etc.)
     * @param queryParams optional query parameters (can be null or empty)
     */
    protected void navigateToModule(ModuleType module, Map<String, String> queryParams) {
        String url = ModuleNavigator.buildModuleUrl(module, queryParams);
        driver.get(url);
    }
}

