package flows;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.SearchPage;

public class BookingFlow {

    private final HomePage homePage;
    private final SearchPage searchPage;

    public BookingFlow(WebDriver driver) {
        this.homePage = new HomePage(driver);
        this.searchPage = new SearchPage(driver);
    }

    @Step("Search and book room: {roomType}")
    public void searchAndBook(String roomType, String checkInDate, String checkOutDate) {
        homePage.goToSearchPage();
        searchPage.enterSearchQuery(roomType);
        searchPage.selectCheckInDate(checkInDate);
        searchPage.selectCheckOutDate(checkOutDate);
        searchPage.clickSearch();
    }

    @Step("Search with filters")
    public SearchPage searchWithFilters(String query, String filter, String minPrice, String maxPrice) {
        homePage.goToSearchPage();
        searchPage.enterSearchQuery(query);
        searchPage.filterBy(filter);
        searchPage.setPriceRange(minPrice, maxPrice);
        searchPage.applyFilters();
        return searchPage.clickSearch();
    }

    @Step("Quick search from home page")
    public SearchPage quickSearch(String query) {
        return homePage.search(query);
    }

    @Step("Select first available result")
    public void selectFirstResult() {
        searchPage.clickResultAtIndex(0);
    }

    @Step("Verify search results exist")
    public boolean hasSearchResults() {
        return searchPage.getResultsCount() > 0;
    }

    public int getResultsCount() {
        return searchPage.getResultsCount();
    }
}
