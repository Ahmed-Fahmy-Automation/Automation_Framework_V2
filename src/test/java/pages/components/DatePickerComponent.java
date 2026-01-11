package pages.components;

import core.ActionsEx;
import core.Waits;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatePickerComponent {

    private final WebDriver driver;
    private final ActionsEx actions;
    private final Waits waits;

    // Locators
    private final By datePickerContainer = By.cssSelector(".datepicker-container");
    private final By prevMonthButton = By.cssSelector(".datepicker-prev");
    private final By nextMonthButton = By.cssSelector(".datepicker-next");
    private final By monthYearDisplay = By.cssSelector(".datepicker-month-year");
    private final By dayButtons = By.cssSelector(".datepicker-day:not(.disabled)");
    private final By todayButton = By.cssSelector(".datepicker-today");
    private final By clearButton = By.cssSelector(".datepicker-clear");

    public DatePickerComponent(WebDriver driver) {
        this.driver = driver;
        this.actions = new ActionsEx(driver);
        this.waits = new Waits(driver);
    }

    @Step("Select date: {dateString}")
    public void selectDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        navigateToMonth(date);
        selectDay(date.getDayOfMonth());
    }

    @Step("Navigate to month containing date")
    private void navigateToMonth(LocalDate targetDate) {
        waits.waitForVisible(datePickerContainer);
        String currentMonthYear = actions.getText(monthYearDisplay);
        String targetMonthYear = targetDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        int maxAttempts = 24;
        int attempts = 0;

        while (!currentMonthYear.equalsIgnoreCase(targetMonthYear) && attempts < maxAttempts) {
            LocalDate currentDate = parseMonthYear(currentMonthYear);
            if (targetDate.isBefore(currentDate)) {
                actions.click(prevMonthButton);
            } else {
                actions.click(nextMonthButton);
            }
            waits.sleep(300);
            currentMonthYear = actions.getText(monthYearDisplay);
            attempts++;
        }
    }

    private LocalDate parseMonthYear(String monthYear) {
        return LocalDate.parse("01 " + monthYear, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }

    @Step("Select day: {day}")
    private void selectDay(int day) {
        List<WebElement> days = driver.findElements(dayButtons);
        for (WebElement dayElement : days) {
            if (dayElement.getText().equals(String.valueOf(day))) {
                dayElement.click();
                return;
            }
        }
        throw new RuntimeException("Day " + day + " not found in date picker");
    }

    @Step("Click today button")
    public void selectToday() {
        actions.click(todayButton);
    }

    @Step("Clear date selection")
    public void clear() {
        actions.click(clearButton);
    }

    @Step("Go to previous month")
    public void goToPreviousMonth() {
        actions.click(prevMonthButton);
    }

    @Step("Go to next month")
    public void goToNextMonth() {
        actions.click(nextMonthButton);
    }

    public String getCurrentMonthYear() {
        return actions.getText(monthYearDisplay);
    }

    public boolean isDisplayed() {
        return actions.isDisplayed(datePickerContainer);
    }
}
