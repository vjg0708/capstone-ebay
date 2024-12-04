package com.ebay.testcases;

import com.ebay.utilities.DriverManager;
import com.ebay.utilities.PropertyManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;

public class BaseClass {

    protected WebDriver driver;
    protected DriverManager driverManager;
    protected PropertyManager propertyManager;
    protected String url;
    protected String browser;

    @BeforeSuite
    public void startTestSuite() throws IOException {

        System.out.println("Before Suite");
        driverManager = new DriverManager();
        propertyManager = new PropertyManager("C:\\Users\\harrish.vijay\\OneDrive - ascendion\\Desktop\\Capstone projects\\capstone_project-EBAY\\automation-ebay\\src\\test\\resources\\ebay.properties");
        url = propertyManager.getUrl();
        browser = propertyManager.getBrowser();
        driver = driverManager.createDriverInstance(browser);

    }

    @AfterSuite
    public void endTestSuite(){

        System.out.println("After suite");
        if (driver!=null){
            driverManager.closeDriver(driver);
        }
    }
}
