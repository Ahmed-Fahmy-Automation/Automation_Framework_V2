package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.components.DatePickerComponent;

import java.util.List;
import java.util.stream.Collectors;

public class SearchPage extends BasePage {

    // Locators
    private final By searchInput = By.id("search-query");
    private final By searchButton = By.id("search-submit");
    private final By searchResults = By.cssSelector(".search-results .result-item");
    private final By noResultsMessage = By.cssSelector(".no-results");
    private final By filterDropdown = By.id("filter-select");
    private final By sortDropdown = By.id("sort-select");
    private final By priceRangeMin = By.id("price-min");
    private final By priceRangeMax = By.id("price-max");
    private final By applyFiltersButton = By.id("apply-filters");
    private final By checkInDateInput = By.id("check-in-date");
    private final By checkOutDateInput = By.id("check-out-date");
    private final By resultsCount = By.cssSelector(".results-count");

    private final DatePickerComponent datePicker;

    public SearchPage(WebDriver driver) {
        super(driver);
        this.datePicker = new DatePickerComponent(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return actions.isDisplayed(searchInput);
    }

    @Step("Enter search query: {query}")
    public SearchPage enterSearchQuery(String query) {
        actions.type(searchInput, query);
        return this;
    }

    @Step("Click search button")
    public SearchPage clickSearch() {
        actions.click(searchButton);
        waits.waitForVisible(searchResults, 10);
        return this;
    }

    @Step("Search for: {query}")
    public SearchPage search(String query) {
        enterSearchQuery(query);
        return clickSearch();
    }

    public List<String> getSearchResultTitles() {
        waits.waitForVisible(searchResults);
        return driver.findElements(searchResults).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public int getResultsCount() {
        return driver.findElements(searchResults).size();
    }

    public boolean hasNoResults() {
        return actions.isDisplayed(noResultsMessage);
    }

    @Step("Filter by: {filterOption}")
    public SearchPage filterBy(String filterOption) {
        actions.selectByText(filterDropdown, filterOption);
        return this;
    }

    @Step("Sort by: {sortOption}")
    public SearchPage sortBy(String sortOption) {
        actions.selectByText(sortDropdown, sortOption);
        return this;
    }

    @Step("Set price range: {min} - {max}")
    public SearchPage setPriceRange(String min, String max) {
        actions.type(priceRangeMin, min);
        actions.type(priceRangeMax, max);
        return this;
    }

    @Step("Apply filters")
    public SearchPage applyFilters() {
        actions.click(applyFiltersButton);
        return this;
    }

    @Step("Select check-in date: {date}")
    public SearchPage selectCheckInDate(String date) {
        actions.click(checkInDateInput);
        datePicker.selectDate(date);
        return this;
    }

    @Step("Select check-out date: {date}")
    public SearchPage selectCheckOutDate(String date) {
        actions.click(checkOutDateInput);
        datePicker.selectDate(date);
        return this;
    }

    public String getResultsCountText() {
        return actions.getText(resultsCount);
    }

    @Step("Click on result at index: {index}")
    public void clickResultAtIndex(int index) {
        List<WebElement> results = driver.findElements(searchResults);
        if (index < results.size()) {
            results.get(index).click();
        }
    }
}
