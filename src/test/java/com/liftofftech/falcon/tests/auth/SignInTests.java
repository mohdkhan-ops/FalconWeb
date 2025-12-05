package com.liftofftech.falcon.tests.auth;

import com.liftofftech.falcon.core.base.BaseTest;
import com.liftofftech.falcon.pages.auth.SignInPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Falcon Web")
@Feature("Authentication")
public class SignInTests extends BaseTest {

    /**
     * Override to skip base URL navigation - we navigate to module directly
     */
    @Override
    protected void navigateToBaseUrl() {
        // Skip - we navigate to module in test
    }

    @Story("Sign in with email and password")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify user can sign in with valid email and password")
    public void shouldSignInWithEmailAndPassword() {
        SignInPage signInPage = new SignInPage();
        
        // Navigate to module
        signInPage.navigateToModule();
        
        // Perform sign in with email/password
        signInPage.signInWithEmail(SignInPage.TEST_EMAIL, SignInPage.TEST_PASSWORD);
        
        // Verify user is signed in
        Assert.assertTrue(signInPage.isUserSignedIn(),
            "User should be signed in after entering valid credentials.");
    }

    @Story("Sign out")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify user can sign out", dependsOnMethods = "shouldSignInWithEmailAndPassword")
    public void shouldSignOut() {
        SignInPage signInPage = new SignInPage();
        
        // Navigate to module (user should still be signed in from previous test)
        signInPage.navigateToModule();
        
        // Perform sign out
        signInPage.signOut();
        
        // Verify sign in button is visible (user is signed out)
        Assert.assertTrue(signInPage.isSignInButtonVisible(),
            "Sign in button should be visible after signing out.");
    }

    @Story("Sign in with Google")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify user can sign in with Google", enabled = false)
    public void shouldSignInWithGoogle() {
        SignInPage signInPage = new SignInPage();
        
        // Navigate to module
        signInPage.navigateToModule();
        
        // TODO: Implement Google sign in test
        // This requires handling Google OAuth popup
        // signInPage.signInWithGoogle();
        
        // Verify user is signed in
        // Assert.assertTrue(signInPage.isUserSignedIn(),
        //     "User should be signed in after Google authentication.");
    }
}

