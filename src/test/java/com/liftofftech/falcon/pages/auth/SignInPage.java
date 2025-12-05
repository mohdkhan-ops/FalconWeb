package com.liftofftech.falcon.pages.auth;

import com.liftofftech.falcon.core.base.BasePage;
import com.liftofftech.falcon.core.driver.ModuleType;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object for Sign In functionality.
 * Contains locators and actions for the sign in flow.
 */
public class SignInPage extends BasePage {

    // ========== TEST CREDENTIALS ==========
    public static final String TEST_EMAIL = "mohi2test@yahoo.com";
    public static final String TEST_PASSWORD = "Mohikhan@7867";

    // ========== ELEMENT LOCATORS ==========
    
    public static final By SETTINGS_CTA = By.xpath("//button[normalize-space()='Settings']");
    public static final By SIGNOUT_CTA = By.xpath("//button[normalize-space()='Sign Out']");
    public static final By SIGNIN_CTA = By.xpath("//button[normalize-space()='Sign in']");
    public static final By SIGNIN_DIV = By.xpath("//div[@class='cl-card cl-signIn-start 🔒️ cl-internal-d5pd3d']");
    public static final By INPUT_EMAIL = By.xpath("//input[@id='identifier-field']");
    public static final By INPUT_PASSWORD = By.xpath("//input[@id='password-field']");
    public static final By CONTINUE_BUTTON = By.xpath("//button[contains(@class,'cl-formButtonPrimary') and normalize-space()='Continue']");
    public static final By CONTINUE_WITH_GOOGLE_BUTTON = By.xpath("//button[@class='cl-socialButtonsBlockButton cl-button cl-socialButtonsBlockButton__google cl-button__google 🔒️ cl-internal-jwof54']");
    public static final By USER_NAME_DISPLAYED = By.xpath("//span[@class='cl-userPreviewMainIdentifierText cl-userPreviewMainIdentifierText__personalWorkspace 🔒️ cl-internal-1wakdmh']");

    // ========== NAVIGATION ==========

    @Step("Navigate to module for sign in")
    public void navigateToModule() {
        navigateToModule(ModuleType.IMAGE, null);
    }

    // ========== SIGN IN METHODS ==========

    /**
     * Performs sign in with email and password.
     * Multi-step form: Email → Continue → Password → Continue
     * 
     * @param email user email
     * @param password user password
     */
    @Step("Sign in with email: {email}")
    public void signInWithEmail(String email, String password) {
        // Step 1: Click Sign In button
        waitUntilClickable(SIGNIN_CTA);
        click(SIGNIN_CTA);
        
        // Step 2: Wait for sign in dialog
        waitUntilVisible(SIGNIN_DIV);
        
        // Step 3: Enter email
        waitUntilVisible(INPUT_EMAIL);
        type(INPUT_EMAIL, email);
        
        // Step 4: Click Continue to go to password step
        waitUntilClickable(CONTINUE_BUTTON);
        click(CONTINUE_BUTTON);
        
        // Step 5: Wait for and enter password
        waitUntilVisible(INPUT_PASSWORD);
        type(INPUT_PASSWORD, password);
        
        // Step 6: Click Continue to submit
        waitUntilClickable(CONTINUE_BUTTON);
        click(CONTINUE_BUTTON);
        
        // Step 7: Wait for sign-in dialog to close
        waitForDialogToClose();
    }

    /**
     * Waits for the sign-in dialog to close after successful login.
     */
    private void waitForDialogToClose() {
        try {
            org.openqa.selenium.support.ui.WebDriverWait dialogWait = 
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15));
            dialogWait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(SIGNIN_DIV));
        } catch (Exception e) {
            // Dialog might have already closed
        }
    }

    /**
     * Performs sign in with Google.
     * TODO: Implement Google OAuth sign in flow
     */
    @Step("Sign in with Google")
    public void signInWithGoogle() {
        waitUntilClickable(SIGNIN_CTA);
        click(SIGNIN_CTA);
        
        waitUntilVisible(SIGNIN_DIV);
        
        // TODO: Implement Google sign in
        // click(CONTINUE_WITH_GOOGLE_BUTTON);
        // Handle Google OAuth popup/redirect
    }

    // ========== SIGN OUT METHODS ==========

    /**
     * Performs sign out.
     */
    @Step("Sign out")
    public void signOut() {
        waitUntilClickable(SETTINGS_CTA);
        click(SETTINGS_CTA);
        
        waitUntilClickable(SIGNOUT_CTA);
        click(SIGNOUT_CTA);
    }

    // ========== VERIFICATION METHODS ==========

    /**
     * Checks if user is signed in by verifying username is displayed.
     */
    @Step("Verify user is signed in")
    public boolean isUserSignedIn() {
        try {
            waitUntilVisible(USER_NAME_DISPLAYED);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if sign in button is visible (user is signed out).
     */
    @Step("Verify sign in button is visible")
    public boolean isSignInButtonVisible() {
        try {
            waitUntilVisible(SIGNIN_CTA);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

