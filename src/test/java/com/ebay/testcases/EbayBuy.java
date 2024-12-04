package com.ebay.testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.ebay.pomfactory.PomObject;
import com.ebay.testdata.ReadLogin;
import com.ebay.testdata.ReadRegister;
import com.ebay.utilities.ReportManager;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.NoSuchElementException;

public class EbayBuy extends BaseClass{

    WebDriver currentDriver;
    ExtentReports extentReports;
    ExtentSparkReporter sparkReporter;
    ReportManager reportManager;
    PomObject ebayPom;
    JavascriptExecutor js;
    int stateChange = 0;
    String mainWindow;
    String localStorage;
    String sessionStorage;
    Set<Cookie> cookies ;

    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String password;

    @BeforeClass
    public void setup(){

        System.out.println("Before Class");
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));


        ebayPom = new PomObject(driver);
        js = (JavascriptExecutor) driver;



    }

    @BeforeMethod
    public void setUpBefore(Method method){

        System.out.println("Before method");

        extentReports = new ExtentReports();
        sparkReporter = new ExtentSparkReporter("C:\\Users\\harrish.vijay\\OneDrive - ascendion\\Desktop\\Capstone projects\\capstone_project-EBAY\\automation-ebay\\src\\test\\reports\\report-"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))+".html");

        reportManager = new ReportManager(extentReports,sparkReporter);
        reportManager.initiateReport("Web Automation","Capstone Project");

        if(method.getName().equalsIgnoreCase("userLogin")){

            System.out.println("Intiating driver for login");
            currentDriver = driverManager.createDriverInstance(browser);
            currentDriver.get(url);
            currentDriver.manage().window().maximize();
            currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            stateChange = 1;
        }
        else {

            currentDriver = driver;
        }
    }

    @Test(priority = 0)
    public void homePage() throws IOException {

        ExtentTest homepage = reportManager.createTestCase("Home page",
                "To check whether the home page is displayed");
        try {

            String actualTitle = driver.getTitle();

            Assert.assertEquals("Electronics, Cars, Fashion, Collectibles & More | eBay",
                    actualTitle,
                    "Page Title not visible");
            reportManager.passTestCase(homepage,"Title is visible");
            mainWindow = driver.getWindowHandle();

        } catch (ElementNotInteractableException e) {

            Assert.fail("Element not found");
            reportManager.failTestCase(homepage,driver,
                    "Test failed","element is not visible to find");
        }
        catch (AssertionError e){

            Assert.fail("Page is not displayed");
            reportManager.failTestCase(homepage,driver,
                    "Test failed","element is not visible to find");
        }
    }

    @Test(dataProvider = "loginCredentials", dataProviderClass = ReadLogin.class,priority = 1)
    public void userLogin(String username, String password){

        PomObject loginPom = null;
        try {
            if(stateChange==1){

                loginPom = new PomObject(currentDriver);
            }
            else {

                loginPom = ebayPom;
            }

            loginPom.clickElement("xpath","//span[@id='gh-ug']//a[contains(text(),'Sign in')]");

            loginPom.typeInput("id","userid",username);
            setUsername(username);
            Assert.assertEquals(username,"harrishvijay.gk8720@gmail.com");
            loginPom.clickElement("id","signin-continue-btn");

            loginPom.typeInput("id","pass",password);
            setPassword(password);
            Assert.assertEquals(password,"Vijay@0780");
            loginPom.clickElement("id","sgnBt");

            cookies = currentDriver.manage().getCookies();
            System.out.println("Cookie added");

            JavascriptExecutor jsLogin = (JavascriptExecutor)currentDriver;

            localStorage = (String) jsLogin.executeScript("return JSON.stringify(localStorage)");
            sessionStorage = (String) jsLogin.executeScript("return JSON.stringify(sessionStorage)");
            System.out.println("Session added");

            WebElement profile = loginPom.getWebElement("xpath","//button[@id='gh-ug']");
            String accountText = profile.getText();
            System.out.println("Account Text :"+accountText);

            loginPom.interactWithElement("xpath",
                    "//button[@id='gh-ug']","hover");
            loginPom.clickElement("xpath","//a[normalize-space()='Sign out']");

        } catch (NoSuchElementException e) {

            Assert.fail("Element not found");
        }
        catch (AssertionError e){

            Assert.fail("Element not available");
        }
    }

    @Test(priority = 2)
    public void productSearch(){

        try {
            for (Cookie cookie : cookies){

                driver.manage().addCookie(cookie);
            }

            js.executeScript("let data = arguments[0];"+
                    "Object.entries(JSON.parse(data)).forEach(([key, value]) => localStorage.setItem(key, value));"
                ,localStorage);

            js.executeScript("let data = arguments[0];"+
                    "Object.entries(JSON.parse(data)).forEach(([key, value]) => localStorage.setItem(key, value));"
                ,sessionStorage);


            driver.navigate().refresh();

            ebayPom.typeInput("id","pass",getPassword());
            ebayPom.clickElement("id","sgnBt");

            WebElement search = ebayPom.getWebElement("id","gh-ac");
            search.sendKeys("apple iphone");
            search.sendKeys(Keys.ENTER);

            String resultsPage = driver.getTitle();
            System.out.println("Results page : "+resultsPage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test(priority = 3)
    public void filterProduct(){

        try {
            ebayPom.clickElement("xpath","//li//a[normalize-space()='Buy It Now']");

            ebayPom.clickElement("xpath","//button[@aria-controls=\"nid-vm8-23-content\"]");
            ebayPom.clickElement("xpath","//span[@id='nid-azq-19-content']//span[normalize-space()='New']");

            js.executeScript("window.scrollBy(0,380)");
            ebayPom.clickElement("xpath","//input[@aria-label=\"Apple iPhone 14 Pro Max\"]");
            js.executeScript("window.scrollBy(0,380)");
            ebayPom.clickElement("xpath","//input[@aria-label=\"256 GB\"]");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test(priority = 4)
    public void productDetails(){

        try {

            ebayPom.clickElement("xpath","//li[@id=\"item2fb61dfa53\"] ");

            Set<String> windowHandles = driver.getWindowHandles();
            List<String> handler = new ArrayList<>(windowHandles);

            driver.switchTo().window(handler.get(1));

            String productPageTitle = driver.getTitle();
            Assert.assertEquals(productPageTitle,"Apple iPhone 14 - 256 GB - Black (Verizon)",
                    "product not displayed properly");

            WebElement productPrice = ebayPom.getWebElement("xpath","//span[normalize-space()='US $850.00']");
            String price = productPrice.getText();
            Assert.assertEquals(price,"US $850.00","price not displayed");

            WebElement itemDescription = ebayPom.getWebElement("xpath","//div[@role=\"tabpanel\"]//h2[normalize-space()='Item specifics']");
            String descriptionTitle = itemDescription.getText();

            Assert.assertEquals(descriptionTitle,"Item specifics","description not available");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Test(priority = 5)
    public void cart(){

        try {

            ebayPom.clickElement("id"," //*[@id=\"atcBtn_btn_1\"]");
            WebElement cartPage = ebayPom.getWebElement("id","mainContent");
            Assert.assertTrue(cartPage.isDisplayed(),"Cart not visible");

            ebayPom.clickElement("xpath","//button[normalize-space()='Remove']");

            ebayPom.clickElement("xpath","//a[normalize-space()='Start shopping']");
            ebayPom.clickElement("xpath","//a[@aria-label='Your shopping cart']");

            WebElement shoppingCart = ebayPom.getWebElement("xpath","//h1[normalize-space()='Shopping cart']");
            Assert.assertEquals(shoppingCart.getText(),"Shopping cart","Text not visible");

            ebayPom.clickElement("xpath","//a[normalize-space()='Start shopping']");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 6)
    public void dailyDeals(){

        try {
            ebayPom.clickElement("xpath","//a[normalize-space()='Daily Deals']");

            WebElement spotlightDeals = ebayPom.getWebElement("xpath","//h2[normalize-space()='Spotlight Deal']");
            Assert.assertEquals(spotlightDeals.getText(),"Spotlight Deal","Deals not available");

            WebElement offerPrice = ebayPom.getWebElement("xpath","//span[normalize-space()='27% off']");
            Assert.assertEquals(offerPrice.getText(),"27% off","Offer Price not available");

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test(priority = 7)
    public void helpCorner(){

        try {

            ebayPom.clickElement("xpath","//a[@class='gh-p'][normalize-space()='Help & Contact']");

            WebElement search = ebayPom.getWebElement("xpath","//input[@id='sr-input']");
            Assert.assertTrue(search.isDisplayed(),"Search not available");

            search.sendKeys("buying as guest");
            search.sendKeys(Keys.ENTER);

            WebElement listTitle = ebayPom.getWebElement("xpath","//li[2]//span[normalize-space()='Buying as a guest']");
            Assert.assertEquals(listTitle.getText(),"Buying as a guest","Option not available");

            ebayPom.clickElement("xpath","//div[@id='wrapper']//li[2]//a[1]");
            WebElement title = ebayPom.getWebElement("xpath","//h1[normalize-space()='Buying as a guest']");
            Assert.assertEquals(title.getText(),"Buying as a guest","Title not available");

            driver.navigate().back();
            search.clear();
            search.sendKeys("hacked account");
            search.sendKeys(Keys.ENTER);

            WebElement listTitle2 = ebayPom.getWebElement("xpath","//li//span[normalize-space()='Get help with a hacked account']");
            Assert.assertEquals(listTitle2.getText(),"Get help with a hacked account","Option not available");

            ebayPom.clickElement("xpath","//li//span[normalize-space()='Get help with a hacked account']");
            WebElement title2 = ebayPom.getWebElement("xpath","//h1[normalize-space()='Get help with a hacked account']");
            Assert.assertEquals(title2.getText(),"Get help with a hacked account","Title not available");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterMethod
    public void tearDownAfter(Method method){

        System.out.println("After method");

        if(method.getName().equalsIgnoreCase("userLogin")){

            currentDriver.quit();
        }
    }

    @AfterClass
    public void tearDown() throws InterruptedException {

        Thread.sleep(5000);
        System.out.println("After Class");
        reportManager.closeReport();
        driver.close();
    }
}
