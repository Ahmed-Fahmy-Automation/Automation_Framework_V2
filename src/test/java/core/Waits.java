package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class for various wait operations.
 * Provides explicit waits with different conditions.
 */
public class Waits {

    private static final Logger logger = LogManager.getLogger(Waits.class);
    private final WebDriver driver;
    private final int defaultTimeout;
    private final int pollingInterval;

    /**
     * Constructor with default timeout of 10 seconds.
     *
     * @param driver WebDriver instance
     */
    public Waits(WebDriver driver) {
        this(driver, 10, 500);
    }

    /**
     * Constructor with custom timeout and polling interval.
     *
     * @param driver          WebDriver instance
     * @param timeoutSeconds  Timeout in seconds
     * @param pollingMillis   Polling interval in milliseconds
     */
    public Waits(WebDriver driver, int timeoutSeconds, int pollingMillis) {
        this.driver = driver;
        this.defaultTimeout = timeoutSeconds;
        this.pollingInterval = pollingMillis;
    }

    /**
     * Waits for element to be visible.
     *
     * @param locator Element locator
     * @return WebElement when visible
     */
    public WebElement waitForVisible(By locator) {
        return waitForVisible(locator, defaultTimeout);
    }

    /**
     * Waits for element to be visible with custom timeout.
     *
     * @param locator        Element locator
     * @param timeoutSeconds Custom timeout in seconds
     * @return WebElement when visible
     */
    public WebElement waitForVisible(By locator, int timeoutSeconds) {
        logger.debug("Waiting for element to be visible: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for element to be clickable.
     *
     * @param locator Element locator
     * @return WebElement when clickable
     */
    public WebElement waitForClickable(By locator) {
        return waitForClickable(locator, defaultTimeout);
    }

    /**
     * Waits for element to be clickable with custom timeout.
     *
     * @param locator        Element locator
     * @param timeoutSeconds Custom timeout in seconds
     * @return WebElement when clickable
     */
    public WebElement waitForClickable(By locator, int timeoutSeconds) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for element to be present in DOM.
     *
     * @param locator Element locator
     * @return WebElement when present
     */
    public WebElement waitForPresence(By locator) {
        return waitForPresence(locator, defaultTimeout);
    }

    /**
     * Waits for element to be present in DOM with custom timeout.
     *
     * @param locator        Element locator
     * @param timeoutSeconds Custom timeout in seconds
     * @return WebElement when present
     */
    public WebElement waitForPresence(By locator, int timeoutSeconds) {
        logger.debug("Waiting for element presence: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits for element to be invisible.
     *
     * @param locator Element locator
     * @return true if element becomes invisible
     */
    public boolean waitForInvisible(By locator) {
        return waitForInvisible(locator, defaultTimeout);
    }

    /**
     * Waits for element to be invisible with custom timeout.
     *
     * @param locator        Element locator
     * @param timeoutSeconds Custom timeout in seconds
     * @return true if element becomes invisible
     */
    public boolean waitForInvisible(By locator, int timeoutSeconds) {
        logger.debug("Waiting for element to be invisible: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for all elements to be visible.
     *
     * @param locator Element locator
     * @return List of WebElements when all are visible
     */
    public List<WebElement> waitForAllVisible(By locator) {
        return waitForAllVisible(locator, defaultTimeout);
    }

    /**
     * Waits for all elements to be visible with custom timeout.
     *
     * @param locator        Element locator
     * @param timeoutSeconds Custom timeout in seconds
     * @return List of WebElements when all are visible
     */
    public List<WebElement> waitForAllVisible(By locator, int timeoutSeconds) {
        logger.debug("Waiting for all elements to be visible: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Waits for element text to contain specified text.
     *
     * @param locator Element locator
     * @param text    Text to wait for
     * @return true if text is present
     */
    public boolean waitForTextPresent(By locator, String text) {
        return waitForTextPresent(locator, text, defaultTimeout);
    }

    /**
     * Waits for element text to contain specified text with custom timeout.
     *
     * @param locator        Element locator
     * @param text           Text to wait for
     * @param timeoutSeconds Custom timeout in seconds
     * @return true if text is present
     */
    public boolean waitForTextPresent(By locator, String text, int timeoutSeconds) {
        logger.debug("Waiting for text '{}' in element: {}", text, locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Waits for URL to contain specified text.
     *
     * @param urlFragment URL fragment to wait for
     * @return true if URL contains the text
     */
    public boolean waitForUrlContains(String urlFragment) {
        return waitForUrlContains(urlFragment, defaultTimeout);
    }

    /**
     * Waits for URL to contain specified text with custom timeout.
     *
     * @param urlFragment    URL fragment to wait for
     * @param timeoutSeconds Custom timeout in seconds
     * @return true if URL contains the text
     */
    public boolean waitForUrlContains(String urlFragment, int timeoutSeconds) {
        logger.debug("Waiting for URL to contain: {}", urlFragment);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.urlContains(urlFragment));
    }

    /**
     * Waits for page to fully load using JavaScript.
     */
    public void waitForPageLoad() {
        waitForPageLoad(defaultTimeout);
    }

    /**
     * Waits for page to fully load using JavaScript with custom timeout.
     *
     * @param timeoutSeconds Custom timeout in seconds
     */
    public void waitForPageLoad(int timeoutSeconds) {
        logger.debug("Waiting for page to fully load");
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(
                (ExpectedCondition<Boolean>) wd -> {
                    JavascriptExecutor js = (JavascriptExecutor) wd;
                    return js.executeScript("return document.readyState").equals("complete");
                }
        );
    }

    /**
     * Waits for AJAX calls to complete using jQuery.
     */
    public void waitForAjax() {
        waitForAjax(defaultTimeout);
    }

    /**
     * Waits for AJAX calls to complete using jQuery with custom timeout.
     *
     * @param timeoutSeconds Custom timeout in seconds
     */
    public void waitForAjax(int timeoutSeconds) {
        logger.debug("Waiting for AJAX calls to complete");
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(
                (ExpectedCondition<Boolean>) wd -> {
                    JavascriptExecutor js = (JavascriptExecutor) wd;
                    return (Boolean) js.executeScript(
                            "return typeof jQuery !== 'undefined' ? jQuery.active === 0 : true"
                    );
                }
        );
    }

    /**
     * Fluent wait with custom conditions and exception handling.
     *
     * @param condition Custom condition to wait for
     * @param <T>       Return type
     * @return Result of the condition
     */
    public <T> T fluentWait(Function<WebDriver, T> condition) {
        return fluentWait(condition, defaultTimeout, pollingInterval);
    }

    /**
     * Fluent wait with custom conditions, timeout, and polling.
     *
     * @param condition       Custom condition to wait for
     * @param timeoutSeconds  Timeout in seconds
     * @param pollingMillis   Polling interval in milliseconds
     * @param <T>             Return type
     * @return Result of the condition
     */
    public <T> T fluentWait(Function<WebDriver, T> condition, int timeoutSeconds, int pollingMillis) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        
        return wait.until(condition);
    }

    /**
     * Simple thread sleep (use sparingly).
     *
     * @param milliseconds Sleep duration in milliseconds
     */
    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted: {}", e.getMessage());
        }
    }

    /**
     * Waits for element attribute to have specific value.
     *
     * @param locator   Element locator
     * @param attribute Attribute name
     * @param value     Expected value
     * @return true if attribute has the value
     */
    public boolean waitForAttributeValue(By locator, String attribute, String value) {
        return waitForAttributeValue(locator, attribute, value, defaultTimeout);
    }

    /**
     * Waits for element attribute to have specific value with custom timeout.
     *
     * @param locator        Element locator
     * @param attribute      Attribute name
     * @param value          Expected value
     * @param timeoutSeconds Custom timeout in seconds
     * @return true if attribute has the value
     */
    public boolean waitForAttributeValue(By locator, String attribute, String value, int timeoutSeconds) {
        logger.debug("Waiting for attribute '{}' to have value '{}': {}", attribute, value, locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    /**
     * Waits for frame to be available and switches to it.
     *
     * @param locator Frame locator
     * @return WebDriver switched to frame
     */
    public WebDriver waitForFrameAndSwitch(By locator) {
        return waitForFrameAndSwitch(locator, defaultTimeout);
    }

    /**
     * Waits for frame to be available and switches to it with custom timeout.
     *
     * @param locator        Frame locator
     * @param timeoutSeconds Custom timeout in seconds
     * @return WebDriver switched to frame
     */
    public WebDriver waitForFrameAndSwitch(By locator, int timeoutSeconds) {
        logger.debug("Waiting for frame and switching: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    /**
     * Waits for alert to be present.
     *
     * @return Alert when present
     */
    public Alert waitForAlert() {
        return waitForAlert(defaultTimeout);
    }

    /**
     * Waits for alert to be present with custom timeout.
     *
     * @param timeoutSeconds Custom timeout in seconds
     * @return Alert when present
     */
    public Alert waitForAlert(int timeoutSeconds) {
        logger.debug("Waiting for alert to be present");
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.alertIsPresent());
    }
}
