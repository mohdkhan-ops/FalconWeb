package com.liftofftech.falcon.tests.image;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.liftofftech.falcon.core.base.BaseTest;
import com.liftofftech.falcon.pages.image.AiImageGenerator;
import com.liftofftech.falcon.pages.auth.SignInPage;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

import static com.liftofftech.falcon.pages.image.AiImageGenerator.*;

@Epic("Falcon Web")
@Feature("AI Image Generator")
public class ImageGeneratorTests extends BaseTest {
    
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
        
        String expectedPlaceholder = "Describe the image you want to generate... or click on 'From image' to upload an image and generate a prompt from it";
        String actualPlaceholder = page.getAttribute(DESC_PROMPT, "placeholder");
        
        Assert.assertEquals(actualPlaceholder, expectedPlaceholder,
            "Prompt description area should display the correct placeholder text.");
    }

    @Story("Prompt description area accepts input")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify user is able to enter text in prompt description area")
    public void shouldAcceptTextInputInPromptField() {
        page.navigateToImageGenerator();
        
        String testInput = "A beautiful sunset over mountains with vibrant colors";
        page.type(DESC_PROMPT, testInput);
        String actualText = page.getAttribute(DESC_PROMPT, "value");
        
        Assert.assertEquals(actualText, testInput,
            "Prompt description area should accept and display the entered text.");
    }

    @Story("Prompt description area accepts maximum characters")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify prompt description area accepts 3500 characters")
    public void shouldAcceptMaximum3500Characters() {
        page.navigateToImageGenerator();
        
        String maxLengthInput = "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019. Then I moved in QA Profile." +
            "My name is Mohd Mohiuddin Khan. I am from U.P. I did my schooling from U.P. I had Completed my B.Tech from CSE in 2019.";
        
        page.type(DESC_PROMPT, maxLengthInput);
        String actualText = page.getAttribute(DESC_PROMPT, "value");
        
        Assert.assertEquals(actualText.length(), 3500,
            "Prompt description area should accept 3500 characters.");
        Assert.assertEquals(actualText, maxLengthInput,
            "Prompt description area should display the full 3500 character text.");
    }

    @Story("Generate prompt from uploaded image")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify user can generate prompt from uploaded image")
    public void shouldGeneratePromptFromUploadedImage() {
        page.navigateToImageGenerator();
        
        // Wait for button to be clickable, then click
        page.waitUntilClickable(GEN_PROMPT_FROM_IMAGE_CTA);
        page.click(GEN_PROMPT_FROM_IMAGE_CTA);

        // Wait for dialog to appear
        page.waitUntilVisible(UPLOAD_IMAGE_TO_GENERATE_PROMPT);
        
        // Click Upload files in the dialog
        page.click(UPLOAD_IMAGE_TO_GENERATE_PROMPT);

        // Wait for From device option and click
        page.waitUntilVisible(FILE_UPLOAD);
        page.click(FILE_UPLOAD);
        
        // Upload image file
        String testImagePath = System.getProperty("user.dir") + "/src/test/resources/testdata/AssassinsCreed.jpg";
        page.uploadFile(FILE_INPUT, testImagePath);

        // Wait for Done button and click
        page.waitUntilVisible(FILE_UPLOAD_DONE_CTA);
        page.click(FILE_UPLOAD_DONE_CTA);
        
        // Wait for AI to generate prompt
        boolean promptGenerated = page.waitForPromptGeneration(30);
        
        Assert.assertTrue(promptGenerated,
            "Prompt should be generated from the uploaded image.");
    }

    @Story("Improve prompt modifies the text")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify Improve prompt button modifies the entered prompt text")
    public void shouldImprovePromptModifyText() {
        SignInPage signInPage = new SignInPage();
        
        page.navigateToImageGenerator();
        
        // Sign in with email/password
        signInPage.signInWithEmail(SignInPage.TEST_EMAIL, SignInPage.TEST_PASSWORD);

        // Wait for image description heading to be visible
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Enter initial text in prompt
        String originalText = "A cat sitting on a table";
        page.type(DESC_PROMPT, originalText);
        
        // Click Improve prompt button
        page.waitUntilClickable(IMPROVE_PROMPT_CTA);
        page.click(IMPROVE_PROMPT_CTA);
        
        // Wait for generated prompt div to appear
        page.waitUntilVisible(GENERATED_PROMPT_DIV);
        
        // Get the text from generated prompt option
        String generatedPromptText = page.getText(GENERATED_PROMPT_DIV);
        
        // Click on the generated prompt to select it
        page.click(GENERATED_PROMPT_DIV);
        
        // Wait for prompt to be applied to DESC_PROMPT
        page.waitUntilVisible(DESC_PROMPT);
        
        // Get the text from DESC_PROMPT
        String descPromptText = page.getAttribute(DESC_PROMPT, "value");
        
        // Compare - GENERATED_PROMPT_DIV contains "Option 1\n" prefix, so use contains
        Assert.assertTrue(generatedPromptText.contains(descPromptText),
            "GENERATED_PROMPT_DIV should contain the DESC_PROMPT text.");
    }

    @Story("Error message displays when prompt is empty")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify error message appears when DESC_PROMPT is cleared")
    public void shouldDisplayErrorWhenPromptIsEmpty() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Enter some text in DESC_PROMPT
        String testText = "A beautiful sunset over the ocean";
        page.type(DESC_PROMPT, testText);
        
        // Remove all text using keyboard (CTRL+A + BACKSPACE to trigger React state)
        page.clearWithKeys(DESC_PROMPT);
        
        // Verify error message appears
        page.waitUntilVisible(IMAGE_DESC_REQUI_MSG);
        Assert.assertTrue(page.isDisplayed(IMAGE_DESC_REQUI_MSG),
            "Error message should be displayed when prompt is empty.");
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

    @Story("Model list shows only image related models")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify In model,only image models should be shown")
    public void shouldShowOnlyImageModelsInSearch() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Click on MODEL_DROPDOWN
        page.waitUntilClickable(MODEL_DROPDOWN);
        page.click(MODEL_DROPDOWN);
        
        // Wait for model list to appear
        page.waitUntilVisible(ALL_MODEL_LIST_DIV);
        
        // Search for audio module "Sonauto"
        page.waitUntilVisible(MODEL_LIST_SEARCH_INPUT);
        page.type(MODEL_LIST_SEARCH_INPUT, "Sonauto");
        
        // Verify no models found (audio module should not appear in image generator)
        page.waitUntilVisible(NO_MODEL);
        Assert.assertTrue(page.isDisplayed(NO_MODEL),
            "Audio model 'Sonauto' should not appear in image model list.");
        
        // Clear the search input
        page.clearWithKeys(MODEL_LIST_SEARCH_INPUT);
        
        // Search for image model "wan"
        page.type(MODEL_LIST_SEARCH_INPUT, "wan");
        
        // Verify MODEL_WAN is fetched/displayed
        page.waitUntilVisible(MODEL_WAN);
        Assert.assertTrue(page.isDisplayed(MODEL_WAN),
            "Image model 'wan' should appear in image model list.");
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

    @Story("URL query param updates on model selection")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify URL query param changes when selecting a model from ALL_MODEL_LIST_DIV")
    public void shouldUpdateUrlQueryParamOnModelSelection() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Click on MODEL_DROPDOWN to open the model list
        page.waitUntilClickable(MODEL_DROPDOWN);
        page.click(MODEL_DROPDOWN);
        
        page.waitUntilVisible(MODEL_LIST_SEARCH_INPUT);
        page.type(MODEL_LIST_SEARCH_INPUT, "nano");

        page.waitUntilVisible(MODEL_NANOBANANA_PRO);
        
        // Click on MODEL_NANOBANANA_PRO
        page.click(MODEL_NANOBANANA_PRO);
        
        // Wait for dropdown to update
        page.waitUntilVisible(MODEL_DROPDOWN);
        
        // Get dropdown text and verify it contains "nano" (case-insensitive)
        String dropdownText = page.getText(MODEL_DROPDOWN).toLowerCase();
        Assert.assertTrue(dropdownText.contains("nano"),
            "MODEL_DROPDOWN should contain 'nano' after selection. Actual: " + dropdownText);
        
        // Verify URL contains the expected query parameter
        String currentUrl = page.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("model=nano-banana-pro"),
            "URL should contain 'model=nano-banana-pro' query param after selecting Nano-Banana Pro model. Actual URL: " + currentUrl);

        // Click on MODEL_DROPDOWN to open the model list
        page.waitUntilClickable(MODEL_DROPDOWN);
        page.click(MODEL_DROPDOWN);
        
        page.waitUntilVisible(MODEL_LIST_SEARCH_INPUT);
        page.clearWithKeys(MODEL_LIST_SEARCH_INPUT);
        page.type(MODEL_LIST_SEARCH_INPUT, "wan");

        page.waitUntilVisible(MODEL_WAN);
        
        // Click on MODEL_WAN
        page.click(MODEL_WAN);
        
        // Wait for dropdown to update
        page.waitUntilVisible(MODEL_DROPDOWN);
        
        // Wait for URL to update with wan-25
        page.waitForUrlToContain("model=wan-25");
        
        // Get dropdown text and verify it contains "wan" (case-insensitive)
        String dropdownText2 = page.getText(MODEL_DROPDOWN).toLowerCase();
        Assert.assertTrue(dropdownText2.contains("wan"),
            "MODEL_DROPDOWN should contain 'wan' after selection. Actual: " + dropdownText2);
        
        // Verify URL contains the expected query parameter
        String currentUrl2 = page.getCurrentUrl();
        Assert.assertTrue(currentUrl2.contains("model=wan-25"),
            "URL should contain 'model=wan-25' query param after selecting WAN model. Actual URL: " + currentUrl2);
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
        java.util.List<String> actualOptions = page.getAllTexts(ASPECT_RATIO_OPTIONS);
        
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
        java.util.List<String> actualOptions = page.getAllTexts(RESOLUTION_OPTIONS);
        
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

    @Story("Default resolution is 1K on page load")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify default resolution is 1K when user lands on image page")
    public void shouldHaveDefaultResolutionAs1K() {
        page.navigateToImageGenerator();
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Verify RESOLUTION_DROPDOWN displays "1K" by default
        page.waitUntilVisible(RESOLUTION_DROPDOWN);
        String defaultResolution = page.getText(RESOLUTION_DROPDOWN);
        
        System.out.println("Default resolution: " + defaultResolution);
        
        Assert.assertTrue(defaultResolution.contains("1K"),
            "Default resolution should be '1K'. Actual: " + defaultResolution);
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

    @Story("Generate button produces selected number of images")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify generate button produces the selected number of images")
    public void shouldGenerateSelectedNumberOfImages() {
        SignInPage signInPage = new SignInPage();
        
        page.navigateToImageGenerator();
        
        // Sign in with email/password
        signInPage.signInWithEmail(SignInPage.TEST_EMAIL, SignInPage.TEST_PASSWORD);
        
        // Wait for page to load
        page.waitUntilVisible(IMAGE_DESC_HEADING);
        
        // Enter prompt text
        String promptText = "A beautiful landscape with mountains and lake";
        page.type(DESC_PROMPT, promptText);
        
        // Select 3 images from dropdown
        page.waitUntilClickable(NO_OF_IMAGES_DROPDOWN);
        page.click(NO_OF_IMAGES_DROPDOWN);
        
        page.waitUntilVisible(NO_OF_IMAGES_3);
        page.click(NO_OF_IMAGES_3);
        
        // Verify selection is reflected in dropdown
        String selectedCount = page.getText(NO_OF_IMAGES_DROPDOWN_SELECTED);
        Assert.assertTrue(selectedCount.contains("3"),
            "Number of images dropdown should show '3'. Actual: " + selectedCount);
        
        // Verify Generate button is clickable
        page.waitUntilClickable(GENERATE_BUTTON);
        String buttonText = page.getText(GENERATE_BUTTON);
        System.out.println("Generate button text: " + buttonText);
        
        // Click Generate button
        page.click(GENERATE_BUTTON);
        System.out.println("Generate button clicked. Waiting for images...");
        
        // Wait for images to be generated (up to 90 seconds for stage environment)
        boolean imagesGenerated = page.waitForImageGeneration(90);
        
        if (!imagesGenerated) {
            System.out.println("Images not found within 90 seconds.");
            System.out.println("Checking if GENERATED_IMAGES elements exist...");
            int count = page.getElementCount(GENERATED_IMAGES);
            System.out.println("GENERATED_IMAGES count: " + count);
        }
        
        Assert.assertTrue(imagesGenerated,
            "Images should be generated within 90 seconds.");
        
        // Count generated images
        int imageCount = page.getElementCount(GENERATED_IMAGES);
        System.out.println("Number of generated images found: " + imageCount);
        
        // Verify 3 images were generated
        Assert.assertEquals(imageCount, 3,
            "Should generate 3 images as selected. Actual: " + imageCount);
        
        System.out.println("✓ Successfully generated " + imageCount + " images");
    }
}
