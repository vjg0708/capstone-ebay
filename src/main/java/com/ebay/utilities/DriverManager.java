package com.ebay.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverManager {

    protected WebDriver driver;

    public DriverManager(WebDriver driver){

        this.driver = driver;
    }

    public WebDriver createDriverInstance(String browser){

        if (browser.equalsIgnoreCase("chrome")) {

            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            return driver;
        }

        else if (browser.equalsIgnoreCase("edge")) {

            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            return driver;
        }
        else{

            throw new IllegalArgumentException("browser not valid");
        }
    }

    public void closeDriver(){

        if(driver!=null){
            driver.close();
            driver.quit();
        }
    }

}
