package base;

import config.Config;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;

/**
 * Base test class that provides common setup and teardown functionality.
 * All test classes should extend this class.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;

    /**
     * Suite-level setup - runs once before all tests in the suite.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        logger.info("========== Starting Test Suite ==========");
        Config.loadConfig();
    }

    /**
     * Test-level setup - runs before each test method.
     * Initializes WebDriver based on configuration.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Setting up WebDriver for test");
        driver = DriverFactory.createDriver();
        driver.manage().window().maximize();
        
        String baseUrl = Config.getProperty("base.url");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            driver.get(baseUrl);
            logger.info("Navigated to base URL: {}", baseUrl);
        }
    }

    /**
     * Test-level teardown - runs after each test method.
     * Captures screenshot on failure and quits WebDriver.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                captureScreenshot(result.getName());
            }
            driver.quit();
            DriverFactory.removeDriver();
            logger.info("WebDriver closed successfully");
        }
    }

    /**
     * Suite-level teardown - runs once after all tests in the suite.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("========== Test Suite Completed ==========");
    }

    /**
     * Captures a screenshot and attaches it to Allure report.
     *
     * @param testName Name of the test for the screenshot
     */
    protected void captureScreenshot(String testName) {
        try {
            if (driver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(testName + "_screenshot", "image/png", 
                    new ByteArrayInputStream(screenshot), ".png");
                logger.info("Screenshot captured for test: {}", testName);
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    /**
     * Gets the current WebDriver instance.
     *
     * @return WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
}
