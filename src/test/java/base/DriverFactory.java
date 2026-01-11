package base;

import config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Factory class for creating WebDriver instances.
 * Supports Chrome, Firefox, Edge, Safari, and Remote WebDriver.
 * Uses ThreadLocal for thread-safe parallel execution.
 */
public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Creates a WebDriver instance based on configuration.
     *
     * @return WebDriver instance
     */
    public static WebDriver createDriver() {
        String browser = Config.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(Config.getProperty("headless", "false"));
        String remoteUrl = Config.getProperty("remote.url");

        WebDriver driver;

        if (remoteUrl != null && !remoteUrl.isEmpty()) {
            driver = createRemoteDriver(browser, remoteUrl, headless);
        } else {
            driver = createLocalDriver(browser, headless);
        }

        configureTimeouts(driver);
        driverThreadLocal.set(driver);
        logger.info("WebDriver created successfully: {}", browser);
        
        return driver;
    }

    /**
     * Creates a local WebDriver instance.
     *
     * @param browser  Browser type
     * @param headless Whether to run in headless mode
     * @return WebDriver instance
     */
    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                return new EdgeDriver(edgeOptions);

            case "safari":
                return new SafariDriver();

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = getChromeOptions(headless);
                return new ChromeDriver(chromeOptions);
        }
    }

    /**
     * Creates a remote WebDriver instance for Selenium Grid.
     *
     * @param browser   Browser type
     * @param remoteUrl Selenium Grid URL
     * @param headless  Whether to run in headless mode
     * @return WebDriver instance
     */
    private static WebDriver createRemoteDriver(String browser, String remoteUrl, boolean headless) {
        try {
            URL gridUrl = new URL(remoteUrl);
            
            switch (browser) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) {
                        firefoxOptions.addArguments("-headless");
                    }
                    return new RemoteWebDriver(gridUrl, firefoxOptions);

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) {
                        edgeOptions.addArguments("--headless");
                    }
                    return new RemoteWebDriver(gridUrl, edgeOptions);

                case "chrome":
                default:
                    ChromeOptions chromeOptions = getChromeOptions(headless);
                    return new RemoteWebDriver(gridUrl, chromeOptions);
            }
        } catch (MalformedURLException e) {
            logger.error("Invalid remote URL: {}", remoteUrl);
            throw new RuntimeException("Invalid remote URL: " + remoteUrl, e);
        }
    }

    /**
     * Gets Chrome options with common configurations.
     *
     * @param headless Whether to run in headless mode
     * @return ChromeOptions
     */
    private static ChromeOptions getChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        
        return options;
    }

    /**
     * Configures implicit and page load timeouts.
     *
     * @param driver WebDriver instance
     */
    private static void configureTimeouts(WebDriver driver) {
        int implicitWait = Integer.parseInt(Config.getProperty("implicit.wait", "10"));
        int pageLoadTimeout = Integer.parseInt(Config.getProperty("page.load.timeout", "30"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
    }

    /**
     * Gets the current thread's WebDriver instance.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Removes the current thread's WebDriver instance.
     */
    public static void removeDriver() {
        driverThreadLocal.remove();
    }
}
