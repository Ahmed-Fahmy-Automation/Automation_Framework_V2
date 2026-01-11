package tests;

import base.BaseTest;
import data.TestData;
import flows.LoginFlow;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {

    private LoginFlow loginFlow;
    private LoginPage loginPage;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(driver);
        loginFlow = new LoginFlow(driver);
    }

    @Test(description = "Verify successful login with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Valid Login")
    public void testSuccessfulLogin() {
        String username = TestData.getTestUsername();
        String password = TestData.getTestPassword();

        HomePage homePage = loginFlow.loginAs(username, password);

        Assert.assertTrue(homePage.isPageLoaded(), "Home page should be loaded after login");
        Assert.assertTrue(homePage.getWelcomeMessage().contains(username), 
            "Welcome message should contain username");
    }

    @Test(description = "Verify login fails with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid Login")
    public void testInvalidLogin() {
        loginFlow.attemptInvalidLogin("invalid_user", "wrong_password");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
    }

    @Test(description = "Verify login with remember me option")
    @Severity(SeverityLevel.NORMAL)
    @Story("Remember Me")
    public void testLoginWithRememberMe() {
        String username = TestData.getTestUsername();
        String password = TestData.getTestPassword();

        HomePage homePage = loginFlow.loginWithRememberMe(username, password);

        Assert.assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }

    @Test(description = "Verify empty credentials show error")
    @Severity(SeverityLevel.NORMAL)
    @Story("Validation")
    public void testEmptyCredentials() {
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be shown for empty credentials");
    }
}
