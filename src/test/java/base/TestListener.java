package base;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * TestNG listener for test execution events.
 * Integrates with Allure for reporting and logging.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite finished: {}", context.getName());
        logger.info("Passed tests: {}", context.getPassedTests().size());
        logger.info("Failed tests: {}", context.getFailedTests().size());
        logger.info("Skipped tests: {}", context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}.{}", 
            result.getTestClass().getName(), 
            result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test PASSED: {}.{}", 
            result.getTestClass().getName(), 
            result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test FAILED: {}.{}", 
            result.getTestClass().getName(), 
            result.getMethod().getMethodName());
        logger.error("Failure reason: {}", result.getThrowable().getMessage());
        
        // Capture screenshot on failure
        captureScreenshotOnFailure(result);
        
        // Attach exception to Allure report
        Allure.addAttachment("Exception", result.getThrowable().toString());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test SKIPPED: {}.{}", 
            result.getTestClass().getName(), 
            result.getMethod().getMethodName());
        
        if (result.getThrowable() != null) {
            logger.warn("Skip reason: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: {}.{}", 
            result.getTestClass().getName(), 
            result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        logger.error("Test FAILED with timeout: {}.{}", 
            result.getTestClass().getName(), 
            result.getMethod().getMethodName());
        captureScreenshotOnFailure(result);
    }

    /**
     * Captures a screenshot when a test fails and attaches it to Allure report.
     *
     * @param result Test result containing test instance
     */
    private void captureScreenshotOnFailure(ITestResult result) {
        Object testInstance = result.getInstance();
        
        if (testInstance instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testInstance).getDriver();
            
            if (driver != null && driver instanceof TakesScreenshot) {
                try {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment(
                        "Failure_Screenshot_" + result.getMethod().getMethodName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png"
                    );
                    logger.info("Screenshot captured for failed test: {}", 
                        result.getMethod().getMethodName());
                } catch (Exception e) {
                    logger.error("Failed to capture screenshot: {}", e.getMessage());
                }
            }
        }
    }
}
