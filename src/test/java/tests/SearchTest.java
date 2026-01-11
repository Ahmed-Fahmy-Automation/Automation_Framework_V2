package tests;

import base.BaseTest;
import flows.BookingFlow;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.SearchPage;

@Epic("Booking")
@Feature("Search")
public class SearchTest extends BaseTest {

    private BookingFlow bookingFlow;

    @BeforeMethod
    public void initPages() {
        bookingFlow = new BookingFlow(driver);
    }

    @Test(description = "Verify search returns results")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Search Results")
    public void testSearchReturnsResults() {
        SearchPage results = bookingFlow.quickSearch("Deluxe Room");

        Assert.assertTrue(bookingFlow.hasSearchResults(), "Search should return results");
        Assert.assertTrue(results.getResultsCount() > 0, "Results count should be greater than 0");
    }

    @Test(description = "Verify search with filters")
    @Severity(SeverityLevel.NORMAL)
    @Story("Search Filters")
    public void testSearchWithFilters() {
        SearchPage results = bookingFlow.searchWithFilters("Room", "Available", "100", "500");

        Assert.assertTrue(results.isPageLoaded(), "Search page should be loaded");
    }

    @Test(description = "Verify no results message for invalid search")
    @Severity(SeverityLevel.NORMAL)
    @Story("No Results")
    public void testNoResultsMessage() {
        SearchPage results = bookingFlow.quickSearch("xyz123nonexistent");

        Assert.assertTrue(results.hasNoResults(), "No results message should be displayed");
    }
}
