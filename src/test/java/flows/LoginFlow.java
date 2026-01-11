package flows;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.LoginPage;

public class LoginFlow {

    private final LoginPage loginPage;

    public LoginFlow(WebDriver driver) {
        this.loginPage = new LoginPage(driver);
    }

    @Step("Perform login with username: {username}")
    public HomePage loginAs(String username, String password) {
        return loginPage.login(username, password);
    }

    @Step("Perform login with remember me option")
    public HomePage loginWithRememberMe(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.checkRememberMe();
        return loginPage.clickLogin();
    }

    @Step("Attempt invalid login")
    public LoginPage attemptInvalidLogin(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        return loginPage;
    }

    @Step("Verify login error message")
    public boolean verifyErrorMessage(String expectedMessage) {
        return loginPage.getErrorMessage().contains(expectedMessage);
    }

    @Step("Navigate to forgot password")
    public void goToForgotPassword() {
        loginPage.clickForgotPassword();
    }

    public boolean isLoginPageDisplayed() {
        return loginPage.isPageLoaded();
    }
}
