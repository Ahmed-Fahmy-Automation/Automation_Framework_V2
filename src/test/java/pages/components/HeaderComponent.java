package pages.components;

import core.ActionsEx;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderComponent {

    private final ActionsEx actions;

    // Locators
    private final By logo = By.cssSelector(".header-logo");
    private final By homeLink = By.linkText("Home");
    private final By searchLink = By.linkText("Search");
    private final By bookingsLink = By.linkText("My Bookings");
    private final By profileDropdown = By.id("profile-dropdown");
    private final By logoutButton = By.id("logout-btn");
    private final By notificationIcon = By.cssSelector(".notification-icon");
    private final By notificationBadge = By.cssSelector(".notification-badge");
    private final By userNameDisplay = By.cssSelector(".user-name");

    public HeaderComponent(WebDriver driver) {
        this.actions = new ActionsEx(driver);
    }

    @Step("Click on logo")
    public void clickLogo() {
        actions.click(logo);
    }

    @Step("Click on Home link")
    public void clickHome() {
        actions.click(homeLink);
    }

    @Step("Click on Search link")
    public void clickSearch() {
        actions.click(searchLink);
    }

    @Step("Click on My Bookings link")
    public void clickMyBookings() {
        actions.click(bookingsLink);
    }

    @Step("Open profile dropdown")
    public void openProfileDropdown() {
        actions.click(profileDropdown);
    }

    @Step("Logout")
    public void logout() {
        openProfileDropdown();
        actions.click(logoutButton);
    }

    public String getUserName() {
        return actions.getText(userNameDisplay);
    }

    public boolean isUserLoggedIn() {
        return actions.isDisplayed(profileDropdown);
    }

    @Step("Click notification icon")
    public void clickNotifications() {
        actions.click(notificationIcon);
    }

    public int getNotificationCount() {
        if (actions.isDisplayed(notificationBadge)) {
            String count = actions.getText(notificationBadge);
            return Integer.parseInt(count);
        }
        return 0;
    }
}
