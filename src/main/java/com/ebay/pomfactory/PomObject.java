package com.ebay.pomfactory;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class PomObject {

    protected WebDriver driver;

    public PomObject(WebDriver driver){

        this.driver = driver;
    }

    public By getLocator(String type, String value){

        if(type.equalsIgnoreCase("id")){

            return By.id(value);
        }
        else if (type.equalsIgnoreCase("xpath")) {

            return By.xpath(value);
        }
        else if (type.equalsIgnoreCase("linktext")){

            return By.linkText(value);
        }
        else throw new RuntimeException("Locator invalid");

    }

    public WebElement getWebElement(String type, String value){

        return driver.findElement(getLocator(type,value));
    }

    public void clickElement(WebElement element){

        element.click();
    }

    public void clickElement(String type, String value){

        driver.findElement(getLocator(type,value)).click();
    }

    public boolean checkElementIsDisplayed(WebElement element){

        return element.isDisplayed();
    }

    public boolean checkElementIsDisplayed(String type, String value){

        return driver.findElement(getLocator(type,value)).isDisplayed();
    }

    public void typeInput(WebElement element,
                          String text){

        element.sendKeys(text);
    }

    public void typeInput(String type, String value,
                          String text){

        driver.findElement(getLocator(type,value)).sendKeys(text);
    }

    public void clearInput(WebElement inputElement){

        inputElement.clear();
    }

    public void clearInput(String type, String value){

        driver.findElement(getLocator(type,value)).clear();
    }

    public void interactWithElement(WebElement element,
                                    String action) {

        switch (action) {

            case "enter" -> element.sendKeys(Keys.ENTER);
            case "backspace" -> element.sendKeys(Keys.BACK_SPACE);
            case "select all" -> element.sendKeys(Keys.CONTROL + "A");
            case "hover" -> {
                Actions actions = new Actions(driver);
                actions.moveToElement(element).perform();
            }

        }

    }
    public void interactWithElement(String type, String value, String action){

            switch (action){

                case "enter"-> driver.findElement(getLocator(type,value)).sendKeys(Keys.ENTER);
                case "backspace"->driver.findElement(getLocator(type,value)).sendKeys(Keys.BACK_SPACE);
                case "select all"->driver.findElement(getLocator(type,value)).sendKeys(Keys.CONTROL+"A");
                case "hover"->{
                    Actions actions = new Actions(driver);
                    actions.moveToElement(driver.findElement(getLocator(type,value))).perform();
                }

            }
    }


}
