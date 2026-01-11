package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Locators
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-btn");
    private final By errorMessage = By.cssSelector(".error-message");
    private final By forgotPasswordLink = By.linkText("Forgot Password?");
    private final By rememberMeCheckbox = By.id("remember-me");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return actions.isDisplayed(usernameInput) && actions.isDisplayed(loginButton);
    }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        actions.type(usernameInput, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        actions.type(passwordInput, password);
        return this;
    }

    @Step("Click login button")
    public HomePage clickLogin() {
        actions.click(loginButton);
        return new HomePage(driver);
    }

    @Step("Login with credentials: {username}")
    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    @Step("Check remember me")
    public LoginPage checkRememberMe() {
        actions.click(rememberMeCheckbox);
        return this;
    }

    public String getErrorMessage() {
        return actions.getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return actions.isDisplayed(errorMessage);
    }

    @Step("Click forgot password link")
    public void clickForgotPassword() {
        actions.click(forgotPasswordLink);
    }
}
