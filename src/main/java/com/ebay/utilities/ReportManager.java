package com.ebay.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ReportManager {

    protected ExtentReports extentReports;
    protected ExtentSparkReporter sparkReporter;

    public ReportManager(ExtentReports extentReports,
                         ExtentSparkReporter sparkReporter){

        this.extentReports = extentReports;
        this.sparkReporter = sparkReporter;

    }

    public void initiateReport(String documentName,
                               String reportName){

        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle(documentName);
        sparkReporter.config().setReportName(reportName);

        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("OS",System.getProperty("os.name"));
        extentReports.setSystemInfo("Environment","TestEnvironment");

    }
    public ExtentTest createTestCase(String name, String description){

        return extentReports.createTest(name, description);
    }


    public String takeScreenShot(WebDriver driver) throws IOException {

        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String screenshotPath = "C:\\Users\\harrish.vijay\\OneDrive - ascendion\\Desktop\\Capstone projects\\capstone_project-EBAY\\automation-ebay\\reports\\screenshots\\"+System.currentTimeMillis()+".png";
        File destFile = new File(screenshotPath);
        FileUtils.copyFile(screenshot, destFile);

        return destFile.getPath();
    }

    public ExtentTest passTestCase(ExtentTest test, String message){

        return test.pass(message);
    }

    public ExtentTest failTestCase(ExtentTest test,
                                   WebDriver driver,
                                   String message,
                                   String description) throws IOException {

        return test.fail(message)
                .addScreenCaptureFromPath(takeScreenShot(driver), description);
    }






}
