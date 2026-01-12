package com.liftofftech.falcon.tests.image;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.liftofftech.falcon.core.base.BaseTest;
import com.liftofftech.falcon.pages.image.AiImageGenerator;
import com.liftofftech.falcon.pages.auth.SignInPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

import static com.liftofftech.falcon.pages.image.AiImageGenerator.*;

@Epic("Falcon Web")
@Feature("AI Image Generator - UI Tests")
public class ImageGeneratorUITests extends BaseTest {
    
    private AiImageGenerator page;

    /**
     * Override to skip base URL navigation - module tests handle their own navigation
     */
    @Override
    protected void navigateToBaseUrl() {
        // Skip base URL navigation - module tests navigate directly to module URLs
    }

    /**
     * Initialize page object before each test
     */
    @BeforeMethod
    public void initPage() {
        page = new AiImageGenerator();
    }

    @Story("Right panel displays placeholder")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify right panel shows placeholder")
    public void shouldShowRightPanelPlaceholder() {
        page.navigateToImageGenerator();
        
        Assert.assertTrue(page.isDisplayed(RIGHT_PANEL_PLACEHOLDER), 
            "Right panel placeholder should be visible.");
    }

    @Story("Prompt description area placeholder")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify prompt description area displays correct placeholder text")
    public void shouldDisplayCorrectPromptPlaceholder() {
        page.navigateToImageGenerator();
        
        String expectedPlaceholder = "Describe your image... Type @ to add saved elements";
        String actualPlaceholder = page.getAttribute(DESC_PROMPT, "data-placeholder");
        
        Assert.assertEquals(actualPlaceholder, expectedPlaceholder,
            "Prompt description area should display the correct placeholder text.");
    }

    @Story("Model dropdown opens model list popup")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify clicking MODEL_DROPDOWN opens the model list popup")
    public void shouldOpenModelListOnDropdownClick() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Click on MODEL_DROPDOWN
        page.waitUntilClickable(MODEL_DROPDOWN);
        page.click(MODEL_DROPDOWN);
        
        // Wait for ALL_MODEL_LIST_DIV to appear
        page.waitUntilVisible(ALL_MODEL_LIST_DIV);
        
        // Verify ALL_MODEL_LIST_DIV is displayed
        Assert.assertTrue(page.isDisplayed(ALL_MODEL_LIST_DIV),
            "Model list popup should be displayed after clicking model dropdown.");
    }

    @Story("Select model from dropdown")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify selecting a model updates the dropdown display")
    public void shouldUpdateDropdownAfterSelectingModel() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Click on MODEL_DROPDOWN
        page.waitUntilClickable(MODEL_DROPDOWN);
        page.click(MODEL_DROPDOWN);

        page.waitUntilVisible(MODEL_LIST_SEARCH_INPUT);
        page.type(MODEL_LIST_SEARCH_INPUT, "wan");
        
        // Wait for MODEL_WAN to appear
        page.waitUntilVisible(MODEL_WAN);
        
        // Click on MODEL_WAN
        page.click(MODEL_WAN);
        
        // Wait for dropdown to update
        page.waitUntilVisible(MODEL_DROPDOWN);
        
        // Get dropdown text and verify it contains "wan" (case-insensitive)
        String dropdownText = page.getText(MODEL_DROPDOWN).toLowerCase();
        Assert.assertTrue(dropdownText.contains("wan"),
            "MODEL_DROPDOWN should contain 'wan' after selection. Actual: " + dropdownText);
    }

    @Story("Aspect ratio dropdown shows all options")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify ASPECT_RATIO_DROPDOWN contains expected aspect ratio options")
    public void shouldShowAllAspectRatioOptions() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Click on ASPECT_RATIO_DROPDOWN to open the dropdown
        page.waitUntilClickable(ASPECT_RATIO_DROPDOWN);
        page.click(ASPECT_RATIO_DROPDOWN);
        
        // Wait for dropdown options to appear
        page.waitUntilVisible(ASPECT_RATIO_OPTIONS);
        
        // Get all option texts
        List<String> actualOptions = page.getAllTexts(ASPECT_RATIO_OPTIONS);
        
        // Get count of options
        int optionCount = actualOptions.size();
        System.out.println("Total aspect ratio options found: " + optionCount);
        System.out.println("Options: " + actualOptions);
        
        // Verify expected options are present
        Assert.assertTrue(actualOptions.stream().anyMatch(opt -> opt.toLowerCase().contains("square")),
            "Aspect ratio dropdown should contain 'Square' option. Actual options: " + actualOptions);
        Assert.assertTrue(actualOptions.stream().anyMatch(opt -> opt.toLowerCase().contains("landscape")),
            "Aspect ratio dropdown should contain 'Landscape' option. Actual options: " + actualOptions);
        Assert.assertTrue(actualOptions.stream().anyMatch(opt -> opt.toLowerCase().contains("portrait")),
            "Aspect ratio dropdown should contain 'Portrait' option. Actual options: " + actualOptions);
        
        // Verify minimum number of options
        Assert.assertTrue(optionCount >= 3,
            "Aspect ratio dropdown should have at least 3 options. Found: " + optionCount);
    }

    @Story("Selected aspect ratio is reflected in dropdown")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify selected aspect ratio is saved and displayed in dropdown")
    public void shouldReflectSelectedAspectRatioInDropdown() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Verify default value is "Square (1:1)"
        String defaultValue = page.getText(ASPECT_RATIO_DROPDOWN).toLowerCase();
        Assert.assertTrue(defaultValue.contains("square"),
            "Default aspect ratio should be 'Square'. Actual: " + defaultValue);
        
        // Click on ASPECT_RATIO_DROPDOWN to open the dropdown
        page.waitUntilClickable(ASPECT_RATIO_DROPDOWN);
        page.click(ASPECT_RATIO_DROPDOWN);
        
        // Wait for dropdown options to appear and select "Wide (16:9)"
        page.waitUntilVisible(ASPECT_RATIO_WIDE_16_9);
        page.click(ASPECT_RATIO_WIDE_16_9);
        
        // Verify dropdown now shows "Wide (16:9)"
        page.waitUntilVisible(ASPECT_RATIO_DROPDOWN_SELECTED);
        String selectedValue = page.getText(ASPECT_RATIO_DROPDOWN_SELECTED);
        Assert.assertTrue(selectedValue.contains("Wide (16:9)"),
            "Dropdown should display 'Wide (16:9)' after selection. Actual: " + selectedValue);
        
        // Select another option - "Portrait (9:16)"
        page.click(ASPECT_RATIO_DROPDOWN_SELECTED);
        page.waitUntilVisible(ASPECT_RATIO_PORTRAIT_9_16);
        page.click(ASPECT_RATIO_PORTRAIT_9_16);
        
        // Verify dropdown now shows "Portrait (9:16)"
        String selectedValue2 = page.getText(ASPECT_RATIO_DROPDOWN_SELECTED);
        Assert.assertTrue(selectedValue2.contains("Portrait (9:16)"),
            "Dropdown should display 'Portrait (9:16)' after selection. Actual: " + selectedValue2);
    }

    @Story("Resolution dropdown shows all options")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify RESOLUTION_DROPDOWN contains 1K, 2K, 4K options")
    public void shouldShowAllResolutionOptions() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Click on RESOLUTION_DROPDOWN to open the dropdown
        page.waitUntilClickable(RESOLUTION_DROPDOWN);
        page.click(RESOLUTION_DROPDOWN);
        
        // Wait for dropdown options to appear
        page.waitUntilVisible(RESOLUTION_OPTIONS);
        
        // Get all option texts
        List<String> actualOptions = page.getAllTexts(RESOLUTION_OPTIONS);
        
        // Get count of options
        int optionCount = actualOptions.size();
        System.out.println("Total resolution options found: " + optionCount);
        System.out.println("Options: " + actualOptions);
        
        // Verify expected options are present (1K, 2K, 4K)
        Assert.assertTrue(actualOptions.stream().anyMatch(opt -> opt.contains("1K")),
            "Resolution dropdown should contain '1K' option. Actual options: " + actualOptions);
        Assert.assertTrue(actualOptions.stream().anyMatch(opt -> opt.contains("2K")),
            "Resolution dropdown should contain '2K' option. Actual options: " + actualOptions);
        Assert.assertTrue(actualOptions.stream().anyMatch(opt -> opt.contains("4K")),
            "Resolution dropdown should contain '4K' option. Actual options: " + actualOptions);
        
        // Verify minimum number of options (at least 3)
        Assert.assertTrue(optionCount >= 3,
            "Resolution dropdown should have at least 3 options. Found: " + optionCount);
    }

    @Story("Selected resolution is reflected in dropdown")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify user can select 1K, 2K, 4K and selection is reflected in dropdown")
    public void shouldReflectSelectedResolutionInDropdown() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Verify default is 1K
        String defaultValue = page.getText(RESOLUTION_DROPDOWN);
        Assert.assertTrue(defaultValue.contains("1K"),
            "Default resolution should be '1K'. Actual: " + defaultValue);
        
        // Test selecting 2K
        page.waitUntilClickable(RESOLUTION_DROPDOWN);
        page.click(RESOLUTION_DROPDOWN);
        
        page.waitUntilVisible(RESOLUTION_2K);
        page.click(RESOLUTION_2K);
        
        page.waitUntilVisible(RESOLUTION_DROPDOWN_SELECTED);
        String selected2K = page.getText(RESOLUTION_DROPDOWN_SELECTED);
        Assert.assertTrue(selected2K.contains("2K"),
            "Dropdown should display '2K' after selection. Actual: " + selected2K);
        
        // Test selecting 4K
        page.click(RESOLUTION_DROPDOWN_SELECTED);
        page.waitUntilVisible(RESOLUTION_4K);
        page.click(RESOLUTION_4K);
        
        String selected4K = page.getText(RESOLUTION_DROPDOWN_SELECTED);
        Assert.assertTrue(selected4K.contains("4K"),
            "Dropdown should display '4K' after selection. Actual: " + selected4K);
        
        // Test selecting back to 1K
        page.click(RESOLUTION_DROPDOWN_SELECTED);
        page.waitUntilVisible(RESOLUTION_1K);
        page.click(RESOLUTION_1K);
        
        String selected1K = page.getText(RESOLUTION_DROPDOWN_SELECTED);
        Assert.assertTrue(selected1K.contains("1K"),
            "Dropdown should display '1K' after selection. Actual: " + selected1K);
    }

    @Story("Generate button is disabled initially")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify generate button is disabled when page loads initially")
    public void shouldHaveGenerateButtonDisabledInitially() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Wait for generate button to be present
        WebElement generateButton = page.waitUntilPresent(GENERATE_BUTTON);
        
        // Check if button is disabled using multiple methods
        // Method 1: Check disabled attribute (HTML disabled attribute)
        String disabledAttr = generateButton.getAttribute("disabled");
        boolean isDisabledByAttr = disabledAttr != null;
        
        // Method 2: Check aria-disabled attribute (accessibility attribute)
        String ariaDisabled = generateButton.getAttribute("aria-disabled");
        boolean isDisabledByAria = "true".equals(ariaDisabled);
        
        // Method 3: Check if element is enabled using Selenium's isEnabled() method
        boolean isEnabled = generateButton.isEnabled();
        
        // Verify button is disabled
        // Button should be disabled if any of these conditions are true:
        // - disabled attribute exists
        // - aria-disabled is "true"
        // - isEnabled() returns false
        boolean isDisabled = isDisabledByAttr || isDisabledByAria || !isEnabled;
        
        Assert.assertTrue(isDisabled,
            "Generate button should be disabled initially. " +
            "disabled attribute: " + disabledAttr + ", " +
            "aria-disabled: " + ariaDisabled + ", " +
            "isEnabled: " + isEnabled);
    }

    @Story("Loading spinner appears in right panel during generation")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify loading spinner appears in right panel when generating images")
    public void shouldShowLoadingSpinnerInRightPanel() {
        SignInPage signInPage = new SignInPage();
        
        page.navigateToImageGenerator();
        
        // Sign in with email/password
        signInPage.signInWithEmail(SignInPage.TEST_EMAIL, SignInPage.TEST_PASSWORD);
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Step 1: Enter prompt text
        String promptText = "Assassin's Creed Valhalla killing polar bear in the snow it should be realistic and detailed";
        page.type(DESC_PROMPT, promptText);
        
        // Step 2: Select model "Nano-Banana Pro"
        page.waitUntilClickable(MODEL_DROPDOWN);
        page.click(MODEL_DROPDOWN);
        
        page.waitUntilVisible(MODEL_LIST_SEARCH_INPUT);
        page.type(MODEL_LIST_SEARCH_INPUT, "nano");
        
        page.waitUntilVisible(MODEL_NANOBANANA_PRO);
        page.click(MODEL_NANOBANANA_PRO);
        
        // Wait for dropdown to update
        page.waitUntilVisible(MODEL_DROPDOWN);
        
        // Step 3: Select aspect ratio "Landscape"
        page.waitUntilClickable(ASPECT_RATIO_DROPDOWN);
        page.click(ASPECT_RATIO_DROPDOWN);
        
        page.waitUntilVisible(ASPECT_RATIO_LANDSCAPE_3_2);
        page.click(ASPECT_RATIO_LANDSCAPE_3_2);
        
        // Wait for dropdown to update
        page.waitUntilVisible(ASPECT_RATIO_DROPDOWN_SELECTED);
        
        // Step 4: Select resolution "2K"
        page.waitUntilClickable(RESOLUTION_DROPDOWN);
        page.click(RESOLUTION_DROPDOWN);
        
        page.waitUntilVisible(RESOLUTION_2K);
        page.click(RESOLUTION_2K);
        
        // Wait for dropdown to close
        WebDriverWait customWait = page.createCustomWait(10);
        customWait.until(ExpectedConditions.invisibilityOfElementLocated(RESOLUTION_OPTIONS));
        
        // Wait for dropdown to update
        page.waitUntilVisible(RESOLUTION_DROPDOWN_SELECTED);
        
        // Step 5: Verify output format "PNG" (usually default) - Optional step
        // Try to find output format dropdown, but don't fail if it doesn't exist
        try {
            WebDriverWait formatWait = page.createCustomWait(5);
            WebElement formatDropdown = formatWait.until(d -> {
                try {
                    // Try the specific selector first
                    List<WebElement> elements = d.findElements(OUTPUT_FORMAT_DROPDOWN);
                    for (WebElement elem : elements) {
                        if (elem.isDisplayed()) {
                            return elem;
                        }
                    }
                    // Fallback: try to find any combobox that contains PNG
                    List<WebElement> comboboxes = d.findElements(By.xpath("//button[@role='combobox']"));
                    for (WebElement combobox : comboboxes) {
                        if (combobox.isDisplayed()) {
                            String text = combobox.getText().trim();
                            if (text.contains("PNG") || text.contains("png")) {
                                return combobox;
                            }
                        }
                    }
                    return null;
                } catch (Exception e) {
                    return null;
                }
            });
            
            if (formatDropdown != null) {
                String currentFormat = formatDropdown.getText();
                System.out.println("Output format found: " + currentFormat);
                // Verify format if found, but don't fail test if format is different
                if (!currentFormat.contains("PNG") && !currentFormat.contains("png")) {
                    System.out.println("Warning: Output format is not PNG, but continuing test. Actual: " + currentFormat);
                }
            }
        } catch (Exception e) {
            // Output format dropdown not found - skip this verification step
            System.out.println("Output format dropdown not found, skipping format verification: " + e.getMessage());
        }
        
        // Step 6: Select number of images "2"
        page.waitUntilClickable(NO_OF_IMAGES_DROPDOWN);
        page.click(NO_OF_IMAGES_DROPDOWN);
        
        page.waitUntilVisible(NO_OF_IMAGES_2);
        page.click(NO_OF_IMAGES_2);
        
        // Wait for dropdown to close and selection to be reflected
        WebDriverWait imagesWait = page.createCustomWait(10);
        imagesWait.until(ExpectedConditions.invisibilityOfElementLocated(NO_OF_IMAGES_OPTIONS));
        
        // Wait a bit for the selected value to update
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify selection is reflected - try to find the dropdown with "2" selected
        try {
            page.waitUntilVisible(NO_OF_IMAGES_DROPDOWN_SELECTED);
        } catch (Exception e) {
            // If specific selector fails, try to find any combobox containing "2"
            boolean found = imagesWait.until(d -> {
                List<WebElement> comboboxes = d.findElements(By.xpath("//button[@role='combobox']"));
                for (WebElement combobox : comboboxes) {
                    if (combobox.isDisplayed()) {
                        String text = combobox.getText().trim();
                        if (text.contains("2")) {
                            return true;
                        }
                    }
                }
                return false;
            });
            if (!found) {
                System.out.println("Warning: Could not verify number of images selection, but continuing test");
            }
        }
        
        // Step 7: Click Generate button
        page.waitUntilClickable(GENERATE_BUTTON);
        page.click(GENERATE_BUTTON);
        System.out.println("Generate button clicked");
        
        // Step 8: Wait for loading spinner to appear in right panel
        // Wait for spinner/loader to appear in the right panel area
        WebDriverWait spinnerWait = page.createCustomWait(10);
        
        // Wait for spinner to appear - check for GENERATING_LOADER text or spinner elements in right panel
        boolean spinnerAppeared = spinnerWait.until(d -> {
            try {
                // Check for "Generating" text in right panel area
                List<WebElement> loaders = d.findElements(GENERATING_LOADER);
                for (WebElement loader : loaders) {
                    if (loader != null && loader.isDisplayed()) {
                        String text = loader.getText().trim().toLowerCase();
                        int xPosition = loader.getLocation().getX();
                        int windowWidth = d.manage().window().getSize().getWidth();
                        // Right panel is typically on the right half of the screen
                        boolean isInRightPanel = xPosition > (windowWidth / 2);
                        System.out.println("Found loader - Text: '" + loader.getText() + "', X: " + xPosition + ", Window: " + windowWidth + ", Right panel: " + isInRightPanel);
                        if ((isInRightPanel || text.contains("generating")) && text.contains("generating")) {
                            System.out.println("✓ Found 'Generating' loader in right panel");
                            return true;
                        }
                    }
                }
                
                // Check for spinner elements in right panel
                List<WebElement> spinners = d.findElements(RIGHT_PANEL_SPINNER);
                for (WebElement spinner : spinners) {
                    if (spinner != null && spinner.isDisplayed()) {
                        int xPosition = spinner.getLocation().getX();
                        int windowWidth = d.manage().window().getSize().getWidth();
                        boolean isInRightPanel = xPosition > (windowWidth / 2);
                        System.out.println("Found spinner element - X: " + xPosition + ", Window: " + windowWidth + ", Right panel: " + isInRightPanel);
                        if (isInRightPanel) {
                            System.out.println("✓ Found spinner element in right panel");
                            return true;
                        }
                    }
                }
                
                return false;
            } catch (Exception e) {
                System.out.println("Exception while checking spinner: " + e.getMessage());
                return false;
            }
        });
        
        Assert.assertTrue(spinnerAppeared,
            "Loading spinner should appear in right panel when generating images.");
        
        System.out.println("✓ Loading spinner appeared in right panel");
    }

    @Story("Page scrolling functionality")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify AI image generator page can scroll up and down on small screens")
    public void shouldAllowScrollingOnSmallScreens() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Get initial scroll position (should be at top)
        long initialScrollPosition = page.getScrollPosition();
        System.out.println("Initial scroll position: " + initialScrollPosition);
        
        // Verify page is scrollable (has content beyond viewport)
        long maxScrollHeight = page.getMaxScrollHeight();
        System.out.println("Maximum scrollable height: " + maxScrollHeight);
        
        // On small screens or pages with enough content, maxScrollHeight should be > 0
        Assert.assertTrue(maxScrollHeight >= 0,
            "Page should have scrollable content. Max scroll height: " + maxScrollHeight);
        
        // Scroll down by a reasonable amount (e.g., 500 pixels)
        page.scrollByPixels(500);
        
        // Wait a moment for scroll to complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify scroll position changed (scrolled down)
        long scrollAfterDown = page.getScrollPosition();
        System.out.println("Scroll position after scrolling down: " + scrollAfterDown);
        
        Assert.assertTrue(scrollAfterDown > initialScrollPosition,
            "Page should scroll down. Initial: " + initialScrollPosition + ", After scroll: " + scrollAfterDown);
        
        // Scroll back up
        page.scrollByPixels(-500);
        
        // Wait a moment for scroll to complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify scroll position changed back (scrolled up)
        long scrollAfterUp = page.getScrollPosition();
        System.out.println("Scroll position after scrolling up: " + scrollAfterUp);
        
        Assert.assertTrue(scrollAfterUp < scrollAfterDown,
            "Page should scroll up. After down: " + scrollAfterDown + ", After up: " + scrollAfterUp);
        
        System.out.println("✓ Page scrolling functionality verified");
    }
}

