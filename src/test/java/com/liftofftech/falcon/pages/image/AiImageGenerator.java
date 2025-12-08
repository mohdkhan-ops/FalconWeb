package com.liftofftech.falcon.pages.image;

import com.liftofftech.falcon.core.base.BasePage;
import com.liftofftech.falcon.core.driver.ModuleType;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

/**
 * Page Object for AI Image Generator module.
 * Handles navigation and interactions with the image generation interface.
 * 
 * Locators are public static so tests can use base class actions directly:
 * Example: imageGenerator.click(AiImageGenerator.GEN_PROMPT_FROM_IMAGE_CTA)
 */
public class AiImageGenerator extends BasePage {

    // ========== ELEMENT LOCATORS (Public for direct use with base actions) ==========
    
    public static final By HEADING_AI_IMAGE = By.xpath("//h1[normalize-space()='AI Image Generator']");
    public static final By IMAGE_DESC_HEADING = By.xpath("//label[@for='prompt']");
    public static final By DESC_PROMPT = By.xpath("//textarea[@id='prompt']");
    public static final By GEN_PROMPT_FROM_IMAGE_CTA = By.xpath("//button[normalize-space()='Generate prompt from image']");
    
    // Dialog locators for "Upload Image to Generate Prompt"
    public static final By GENERATED_PROMPT_DIV = By.xpath("//div[@aria-label='Select enhanced prompt option 1']");
    public static final By CHOOSE_FILE_BUTTON = By.xpath("//div[@role='dialog']//button[normalize-space()='Choose File']");
    public static final By DIALOG_FILE_INPUT = By.xpath("//div[@role='dialog']//input[@type='file']");
    public static final By CLOSE_DIALOG_BUTTON = By.xpath("//div[@role='dialog']//button[normalize-space()='Close']");
    
    // Legacy locators (keeping for backward compatibility)
    public static final By UPLOAD_IMAGE_TO_GENERATE_PROMPT = By.xpath("//uc-file-uploader-regular[@ctx-name='image-to-prompt-dialog']//span[contains(text(),'Upload files')]");
    public static final By FILE_UPLOAD = By.xpath("//uc-file-uploader-regular[@ctx-name='image-to-prompt-dialog']//div[@class='uc-txt'][normalize-space()='From device']");
    public static final By FILE_INPUT = By.cssSelector("input[type='file']");
    public static final By FILE_UPLOAD_DONE_CTA = By.xpath("(//button[contains(@class,'uc-done-btn')])[3]");
    public static final By IMAGE_DESC_REQUIRED_VALIDATION_MSG = By.xpath("//p[@class='text-sm text-red-500']");
    public static final By CHAR_COUNT = By.xpath("//p[normalize-space()='0/3500']");
    public static final By IMPROVE_PROMPT_CTA = By.xpath("//button[normalize-space()='Improve prompt']");
    public static final By PROMPT_LIBRARY_LINK = By.xpath("//a[normalize-space()='prompt library']");
    public static final By MODEL_DROPDOWN = By.xpath("//div[@class='flex min-w-0 flex-1 items-center']//div[@class='flex min-w-0 flex-1 items-center']");
    public static final By ALL_MODEL_LIST_DIV = By.xpath("//div[@class='py-4']");
    public static final By MODEL_LIST_SEARCH_DIV = By.xpath("//div[@class='flex flex-col space-y-3 sm:flex-row sm:space-x-3 sm:space-y-0']//div[@class='flex flex-wrap gap-1.5']");
    public static final By MODEL_LIST_SEARCH_INPUT = By.xpath("//input[@placeholder='Search models...']");
    public static final By MODEL_LIST_ITEM_ALL = By.xpath("//div[@id='radix-_r_l_']//div[4]");
    public static final By NO_MODEL= By.xpath("//div[normalize-space()='No models found matching your filters.']");
    public static final By MODEL_WAN = By.xpath("//body/div[@role='dialog']/div[@dir='ltr']/div/div/div/div/button[1]/div[2]/div[1]");
    public static final By MODEL_NANOBANANA_PRO = By.xpath("//span[@class='font-medium' and contains(text(),'Nano-Banana Pro')]");
    public static final By UPLOAD_FILES_UI = By.xpath("//span[normalize-space()='Upload files']");
    public static final By UPLOADED_IMAGE_DIV = By.xpath("//div[contains(@class,'absolute') and .//button[normalize-space()='Remove']]");
    public static final By UPLOAD_IMAGE_VALIDATION = By.xpath("//p[@class='mt-1 text-sm text-muted-foreground']");
    public static final By ASPECT_RATIO_DROPDOWN = By.xpath("//button[@role='combobox' and contains(normalize-space(.), 'Square')]");
    public static final By ASPECT_RATIO_DROPDOWN_SELECTED = By.xpath("(//button[@role='combobox'])[1]"); // First combobox is aspect ratio
    public static final By ASPECT_RATIO_OPTIONS = By.xpath("//div[@role='listbox']//div[@role='option']");
    public static final By ASPECT_RATIO_WIDE_16_9 = By.xpath("//div[@role='listbox']//div[@role='option'][contains(.,'Wide (16:9)')]");
    public static final By ASPECT_RATIO_LANDSCAPE_3_2 = By.xpath("//div[@role='listbox']//div[@role='option'][contains(.,'Landscape (3:2)')]");
    public static final By ASPECT_RATIO_PORTRAIT_9_16 = By.xpath("//div[@role='listbox']//div[@role='option'][contains(.,'Portrait (9:16)')]");
    public static final By RESOLUTION_DROPDOWN = By.xpath("//button[@role='combobox' and .//span[normalize-space()='1K']]");
    public static final By RESOLUTION_DROPDOWN_SELECTED = By.xpath("(//button[@role='combobox'])[2]"); // Second combobox is resolution
    public static final By RESOLUTION_OPTIONS = By.xpath("//div[@role='listbox']//div[@role='option']");
    public static final By RESOLUTION_1K = By.xpath("//div[@role='listbox']//div[@role='option'][contains(.,'1K')]");
    public static final By RESOLUTION_2K = By.xpath("//div[@role='listbox']//div[@role='option'][contains(.,'2K')]");
    public static final By RESOLUTION_4K = By.xpath("//div[@role='listbox']//div[@role='option'][contains(.,'4K')]");
    public static final By OUTPUT_FORMAT_DROPDOWN = By.xpath("//button[@role='combobox' and .//span[normalize-space()='PNG']]");
    public static final By NO_OF_IMAGES_DROPDOWN = By.xpath("//button[@role='combobox' and .//span[normalize-space()='1']]");
    public static final By NO_OF_IMAGES_DROPDOWN_SELECTED = By.xpath("(//button[@role='combobox'])[4]"); // Fourth combobox is number of images
    public static final By NO_OF_IMAGES_OPTIONS = By.xpath("//div[@role='listbox']//div[@role='option']");
    public static final By NO_OF_IMAGES_1 = By.xpath("//div[@role='listbox']//div[@role='option'][normalize-space()='1']");
    public static final By NO_OF_IMAGES_2 = By.xpath("//div[@role='listbox']//div[@role='option'][normalize-space()='2']");
    public static final By NO_OF_IMAGES_3 = By.xpath("//div[@role='listbox']//div[@role='option'][normalize-space()='3']");
    public static final By NO_OF_IMAGES_4 = By.xpath("//div[@role='listbox']//div[@role='option'][normalize-space()='4']");
    public static final By GENERATE_BUTTON = By.xpath("//button[normalize-space()='Generate' or contains(@class,'generate')]");
    public static final By GENERATED_IMAGES = By.xpath("//div[contains(@class,'grid') and contains(@class,'grid-cols')]//img[contains(@alt,'Generated design')]");
    public static final By GENERATING_LOADER = By.xpath("//*[contains(text(),'Generating') or contains(text(),'Processing')]");
    public static final By RIGHT_PANEL_SPINNER = By.xpath("//*[name()='svg' and contains(@class,'animate-spin')]");
    public static final By VIEW_HISTORY_CTA = By.xpath("//button[@class='gap-2 whitespace-nowrap rounded-[18px] transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0 inline-flex items-center justify-center border border-gray-200 dark:border-gray-700 bg-background text-sm font-medium shadow-sm hover:bg-accent hover:text-accent-foreground dark:hover:bg-neutral-700 h-9 px-4 py-2 w-full']");
    public static final By GENERATED_IMAGE_UI = By.xpath("//div[@class='relative flex h-full w-full items-center justify-center']");
    public static final By RIGHT_PANEL_PLACEHOLDER = By.xpath("//h2[normalize-space()='Your creations will appear here']");
    public static final By IMAGE_DESC_REQUI_MSG = By.xpath("//p[normalize-space()='Image description is required']");
    public static final By IMAGE_DWNLD_CTA = By.xpath("(//*[name()='svg'][@class='lucide lucide-download w-4 h-4 text-zinc-900 dark:text-zinc-100'])[1]");
    /**
     * Navigates to the AI Image Generator page with optional model parameter.
     * Automatically handles stage vs prod environments based on configuration.
     * 
     * @param model the model name (e.g., "nano-banana-pro")
     */
    @Step("Navigate to AI Image Generator")
    public void navigateToImageGenerator(String model) {
        Map<String, String> queryParams = model != null ? Map.of("model", model) : null;
        navigateToModule(ModuleType.IMAGE, queryParams);
    }

    /**
     * Navigates to the AI Image Generator page without query parameters.
     */
    @Step("Navigate to AI Image Generator")
    public void navigateToImageGenerator() {
        navigateToImageGenerator(null);
    }

    // ========== SPECIFIC METHODS (Only for complex logic, not simple actions) ==========

    /**
     * Waits for the prompt to be generated (textarea value becomes non-empty).
     * Creates a new WebDriverWait with custom timeout.
     * 
     * @param timeoutSeconds maximum time to wait for prompt generation
     * @return true if prompt was generated within timeout, false otherwise
     */
    @Step("Wait for prompt to be generated")
    public boolean waitForPromptGeneration(int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            customWait.until(d -> {
                String value = d.findElement(DESC_PROMPT).getAttribute("value");
                return value != null && !value.trim().isEmpty();
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for the prompt text to change from the original value.
     * 
     * @param originalText the original text to compare against
     * @param timeoutSeconds maximum time to wait for prompt to change
     * @return true if prompt changed within timeout, false otherwise
     */
    @Step("Wait for prompt to change")
    public boolean waitForPromptToChange(String originalText, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            customWait.until(d -> {
                String currentValue = d.findElement(DESC_PROMPT).getAttribute("value");
                return currentValue != null && !currentValue.equals(originalText);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for images to be generated.
     * 
     * @param timeoutSeconds maximum time to wait for image generation
     * @return true if images were generated within timeout, false otherwise
     */
    @Step("Wait for images to be generated")
    public boolean waitForImageGeneration(int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            // Wait for loader to disappear and images to appear
            customWait.until(d -> {
                try {
                    // Check if images are present
                    return !d.findElements(GENERATED_IMAGES).isEmpty();
                } catch (Exception e) {
                    return false;
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

