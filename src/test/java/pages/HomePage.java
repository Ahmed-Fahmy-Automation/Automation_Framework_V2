package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.components.HeaderComponent;

public class HomePage extends BasePage {

    // Locators
    private final By welcomeMessage = By.cssSelector(".welcome-message");
    private final By searchBox = By.id("search-input");
    private final By searchButton = By.id("search-btn");
    private final By bookNowButton = By.cssSelector(".book-now-btn");
    private final By featuredRooms = By.cssSelector(".featured-rooms .room-card");

    private final HeaderComponent header;

    public HomePage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return actions.isDisplayed(welcomeMessage);
    }

    public HeaderComponent getHeader() {
        return header;
    }

    public String getWelcomeMessage() {
        return actions.getText(welcomeMessage);
    }

    @Step("Search for: {query}")
    public SearchPage search(String query) {
        actions.type(searchBox, query);
        actions.click(searchButton);
        return new SearchPage(driver);
    }

    @Step("Click Book Now button")
    public void clickBookNow() {
        actions.click(bookNowButton);
    }

    public int getFeaturedRoomsCount() {
        waits.waitForVisible(featuredRooms);
        return driver.findElements(featuredRooms).size();
    }

    @Step("Navigate to search page")
    public SearchPage goToSearchPage() {
        header.clickSearch();
        return new SearchPage(driver);
    }
}
