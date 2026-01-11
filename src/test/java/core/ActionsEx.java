package core;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ActionsEx {

    private static final Logger logger = LogManager.getLogger(ActionsEx.class);
    private final WebDriver driver;
    private final Waits waits;
    private final JavascriptExecutor js;

    public ActionsEx(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
        this.js = (JavascriptExecutor) driver;
    }

    @Step("Click on element: {locator}")
    public void click(By locator) {
        WebElement element = waits.waitForClickable(locator);
        element.click();
        logger.info("Clicked on element: {}", locator);
    }

    @Step("JavaScript click on element: {locator}")
    public void jsClick(By locator) {
        WebElement element = waits.waitForPresence(locator);
        js.executeScript("arguments[0].click();", element);
        logger.info("JS clicked on element: {}", locator);
    }

    @Step("Double-click on element: {locator}")
    public void doubleClick(By locator) {
        WebElement element = waits.waitForClickable(locator);
        new Actions(driver).doubleClick(element).perform();
        logger.info("Double-clicked on element: {}", locator);
    }

    @Step("Right-click on element: {locator}")
    public void rightClick(By locator) {
        WebElement element = waits.waitForClickable(locator);
        new Actions(driver).contextClick(element).perform();
        logger.info("Right-clicked on element: {}", locator);
    }

    @Step("Type '{text}' into element: {locator}")
    public void type(By locator, String text) {
        WebElement element = waits.waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.info("Typed '{}' into element: {}", text, locator);
    }

    @Step("Clear element: {locator}")
    public void clear(By locator) {
        WebElement element = waits.waitForVisible(locator);
        element.clear();
        logger.info("Cleared element: {}", locator);
    }

    @Step("Select by text '{text}' from: {locator}")
    public void selectByText(By locator, String text) {
        WebElement element = waits.waitForVisible(locator);
        new Select(element).selectByVisibleText(text);
        logger.info("Selected '{}' by text from: {}", text, locator);
    }

    @Step("Select by value '{value}' from: {locator}")
    public void selectByValue(By locator, String value) {
        WebElement element = waits.waitForVisible(locator);
        new Select(element).selectByValue(value);
        logger.info("Selected '{}' by value from: {}", value, locator);
    }

    @Step("Hover over element: {locator}")
    public void hover(By locator) {
        WebElement element = waits.waitForVisible(locator);
        new Actions(driver).moveToElement(element).perform();
        logger.info("Hovered over element: {}", locator);
    }

    @Step("Scroll to element: {locator}")
    public void scrollToElement(By locator) {
        WebElement element = waits.waitForPresence(locator);
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        logger.info("Scrolled to element: {}", locator);
    }

    public String getText(By locator) {
        WebElement element = waits.waitForVisible(locator);
        return element.getText();
    }

    public String getAttribute(By locator, String attribute) {
        WebElement element = waits.waitForPresence(locator);
        return element.getAttribute(attribute);
    }

    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void switchToFrame(By locator) {
        waits.waitForFrameAndSwitch(locator);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    public void acceptAlert() {
        waits.waitForAlert().accept();
    }

    public void dismissAlert() {
        waits.waitForAlert().dismiss();
    }

    public Object executeScript(String script, Object... args) {
        return js.executeScript(script, args);
    }

    public Waits getWaits() {
        return waits;
    }
}
