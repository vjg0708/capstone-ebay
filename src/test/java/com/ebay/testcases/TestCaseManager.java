package com.ebay.testcases;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.ebay.utilities.ReportManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.time.Duration;

public class TestCaseManager {

    WebDriver driver;
    WebDriverWait wait;

    public TestCaseManager(WebDriver driver){

        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void validateElementVisibility(ExtentTest test,
                                          WebElement element, ReportManager report) throws IOException {

        wait.until(ExpectedConditions.visibilityOf(element));
        try {
            Assert.assertTrue(element.isDisplayed());
            test.log(Status.PASS,"visible");
        }
        catch (Exception e){

            Assert.fail(e.getMessage());
            test.log(Status.FAIL,"Not visible")
                    .addScreenCaptureFromPath(report.takeScreenShot(driver));
        }
    }

    public void validateDataFromElement(String actual, WebElement element,
                                        String expected, ExtentTest test,
                                        ReportManager report) throws IOException {
        wait.until(ExpectedConditions.visibilityOf(element));
        try {

            Assert.assertEquals(actual,expected);
            test.log(Status.PASS,"Valid");
        }
        catch (Exception e){

            Assert.fail(e.getMessage());
            test.log(Status.FAIL,"invalid")
                    .addScreenCaptureFromPath(report.takeScreenShot(driver));
        }
    }
}
