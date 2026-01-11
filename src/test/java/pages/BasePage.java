package pages;

import core.ActionsEx;
import core.Waits;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected WebDriver driver;
    protected ActionsEx actions;
    protected Waits waits;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new ActionsEx(driver);
        this.waits = new Waits(driver);
    }

    public abstract boolean isPageLoaded();

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }
}
