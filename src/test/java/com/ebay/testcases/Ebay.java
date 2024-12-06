package com.ebay.testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.ebay.pomfactory.PomObject;
import com.ebay.testdata.ReadLogin;
import com.ebay.utilities.ReportManager;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.NoSuchElementException;

public class Ebay extends BaseClass {

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
    Set<Cookie> cookies;
    TestCaseManager testCase;

    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String password;

    @BeforeClass
    public void setup() {

        //System.out.println("Before Class");
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        extentReports = new ExtentReports();
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        sparkReporter = new ExtentSparkReporter("C:\\Users\\harrish.vijay\\OneDrive - ascendion\\Desktop\\Capstone projects\\capstone_project-EBAY\\automation-ebay\\src\\test\\reports\\report_" + currentTime + ".html");

        reportManager = new ReportManager(extentReports, sparkReporter);
        reportManager.initiateReport("Web Automation", "Capstone Project");

        ebayPom = new PomObject(driver);
        js = (JavascriptExecutor) driver;


    }

    @BeforeMethod
    public void setUpBefore(Method method) {

        testCase = new TestCaseManager(driver);
        //System.out.println("Before method");
        if (method.getName().equalsIgnoreCase("userLogin")) {

            System.out.println("Intiating driver for login");
            currentDriver = driverManager.createDriverInstance(browser);
            currentDriver.get(url);
            currentDriver.manage().window().maximize();
            currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            stateChange = 1;
        } else {

            currentDriver = driver;
        }
    }

    @Test(priority = 0)
    public void homePage() throws IOException {

        ExtentTest homepage = reportManager.createTestCase("Home page",
                "To check whether the home page is displayed");

        String actualTitle = driver.getTitle();
        System.out.println(actualTitle);
        mainWindow = driver.getWindowHandle();

    }

    @Test(dataProvider = "loginCredentials", dataProviderClass = ReadLogin.class, priority = 1)
    public void userLogin(String username, String password) throws InterruptedException, IOException {

        ExtentTest checkLogin = reportManager.createTestCase("Login Validation",
                "To check the login workflow");
        PomObject loginPom = null;

        if (stateChange == 1) {

            loginPom = new PomObject(currentDriver);
        } else {

            loginPom = ebayPom;
        }

        Thread.sleep(5000);
        WebElement signIn = loginPom.getWebElement("xpath", "//span[@id='gh-ug']//a[contains(text(),'Sign in')]");
        testCase.validateElementVisibility(checkLogin,signIn,reportManager);
        signIn.click();
        //loginPom.clickElement("xpath", "//span[@id='gh-ug']//a[contains(text(),'Sign in')]");

        WebDriverWait wait = new WebDriverWait(currentDriver, Duration.ofSeconds(50));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userid")));

        WebElement userName = loginPom.getWebElement("id", "userid");
        userName.sendKeys(username);
        //loginPom.typeInput("id", "userid", username);
        Thread.sleep(3000);
        setUsername(username);
        testCase.validateDataFromElement(username,userName,"harrishvijay.gk8720@gmail.com",
                checkLogin,reportManager);
//        Assert.assertEquals(username, "harrishvijay.gk8720@gmail.com");
        WebElement continueBtn = loginPom.getWebElement("id", "signin-continue-btn");
        testCase.validateElementVisibility(checkLogin,continueBtn,reportManager);
        loginPom.clickElement("id", "signin-continue-btn");

        WebElement passWord = loginPom.getWebElement("id", "pass");
        passWord.sendKeys(password);

        //loginPom.typeInput("id", "pass", password);
        setPassword(password);
        testCase.validateDataFromElement(password,passWord,getPassword(),checkLogin,
                reportManager);
        loginPom.clickElement("id", "sgnBt");

        cookies = currentDriver.manage().getCookies();
        //System.out.println("Cookie added");

        JavascriptExecutor jsLogin = (JavascriptExecutor) currentDriver;

        localStorage = (String) jsLogin.executeScript("return JSON.stringify(localStorage)");
        sessionStorage = (String) jsLogin.executeScript("return JSON.stringify(sessionStorage)");
        //System.out.println("Session added");

        WebElement profile = loginPom.getWebElement("xpath", "//button[@id='gh-ug']");
        String accountText = profile.getText();
        //System.out.println("Account Text :"+accountText);

        loginPom.interactWithElement("xpath",
                "//button[@id='gh-ug']", "hover");
        loginPom.clickElement("xpath", "//a[normalize-space()='Sign out']");


    }

    @Test(priority = 2)
    public void productSearch() throws IOException {

        ExtentTest productSearch = reportManager.createTestCase("Product Search",
                "To verify the workflow of the search feature");
        for (Cookie cookie : cookies) {

            driver.manage().addCookie(cookie);
        }

        js.executeScript("let data = arguments[0];" +
                        "Object.entries(JSON.parse(data)).forEach(([key, value]) => localStorage.setItem(key, value));"
                , localStorage);

        js.executeScript("let data = arguments[0];" +
                        "Object.entries(JSON.parse(data)).forEach(([key, value]) => localStorage.setItem(key, value));"
                , sessionStorage);


        driver.navigate().refresh();

        WebDriverWait wait = new WebDriverWait(currentDriver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));

        ebayPom.typeInput("id", "pass", getPassword());
        ebayPom.clickElement("id", "sgnBt");

        WebElement search = ebayPom.getWebElement("id", "gh-ac");
        testCase.validateElementVisibility(productSearch,search,reportManager);

        String searchText = "apple iphone";
        search.sendKeys("apple iphone");
        testCase.validateDataFromElement("apple iphone",search,searchText,
                productSearch,reportManager);
        search.sendKeys(Keys.ENTER);

        String resultsPage = driver.getTitle();
        //System.out.println("Results page : "+resultsPage);


    }

    @Test(priority = 3)
    public void filterProduct() throws InterruptedException, IOException {

        ExtentTest productFilters = reportManager.createTestCase("Filter products",
                "To verify the workflow of the filters of the products");

        WebElement buyNow = ebayPom.getWebElement("xpath", "//ul[@class='fake-tabs__items']/li[3][normalize-space()='Buy It Now']");
        testCase.validateElementVisibility(productFilters,buyNow,reportManager);
        buyNow.click();
        //ebayPom.clickElement("xpath", "//ul[@class='fake-tabs__items']/li[3][normalize-space()='Buy It Now']");

        js.executeScript("window.scrollBy(0,480)");
        WebElement checkModel = ebayPom.getWebElement("xpath", "//input[@aria-label='Apple iPhone 14 Pro Max']");
        checkModel.click();

        //ebayPom.clickElement("xpath", "//input[@aria-label='Apple iPhone 14 Pro Max']");

        Thread.sleep(3000);

        js.executeScript("window.scrollBy(0,550)");
        WebElement checkStorage = ebayPom.getWebElement("xpath", "//input[@aria-label='256 GB']");
        checkStorage.click();



        //ebayPom.clickElement("xpath", "//input[@aria-label='256 GB']");

        Thread.sleep(3000);
        js.executeScript("window.scrollTo(0,0)");

        System.out.println("After filter");
        Thread.sleep(4000);


    }

    @Test(priority = 4)
    public void productDetails() throws InterruptedException, IOException {

        ExtentTest productDescription = reportManager.createTestCase("Product Details",
                "To verify that the products are displayed properly");
        js.executeScript("window.scrollBy(0,280)");
        //System.out.println("Inside ProductDetails");

        WebElement product = ebayPom.getWebElement("xpath", "//div[@id='srp-river-results']/ul/li[2]/div/div[2]/a");
        //WebElement productDiv = ebayPom.getWebElement("xpath","/html/body/div[5]/div[4]/div[3]/div[1]/div[3]/ul/li[3]");
        testCase.validateElementVisibility(productDescription,product,reportManager);
        product.click();
//            product.findElement(By.xpath("//div[@class='s-item__image-section']")).click();
//            Thread.sleep(3000);
        Set<String> windowhandle = driver.getWindowHandles();
        List<String> getHandler = new ArrayList<>(windowhandle);

        for (String handle : getHandler) {

            System.out.println("Handle :" + handle);
        }

        driver.switchTo().window(getHandler.get(1));

        Thread.sleep(3000);

        String productPageTitle = driver.getTitle();
        System.out.println("Page Title : " + productPageTitle);

//        Assert.assertEquals(productPageTitle, "Apple iPhone 14 - 256 GB - Black (Verizon) | eBay",
//                "product not displayed properly");


//        WebElement productPrice = ebayPom.getWebElement("xpath", "//span[normalize-space()='US $850.00']");
//        String price = productPrice.getText();
//        testCase.validateDataFromElement(price,productPrice,"US $850.00",
//                productDescription,reportManager);
//        Assert.assertEquals(price, "US $850.00", "price not displayed");


    }

    @Test(priority = 5)
    public void cart() throws InterruptedException, IOException {

        //System.out.println("Inside Cart");
        ExtentTest cartFeature = reportManager.createTestCase("Product Cart",
                "To verify the workflow of the product cart");

        js.executeScript("window.scrollBy(0,340)");

        WebElement cart = ebayPom.getWebElement("xpath", "//li//a[@id='atcBtn_btn_1']");
        testCase.validateElementVisibility(cartFeature,cart,reportManager);
        cart.click();
        //ebayPom.clickElement("xpath", "//div[@data-testid='x-evo-atf-right-river']/div/div[5]/ul/li[2]//a");
        WebElement cartPage = ebayPom.getWebElement("id", "mainContent");
        testCase.validateElementVisibility(cartFeature,cartPage,reportManager);
        //Assert.assertTrue(cartPage.isDisplayed(), "Cart not visible");

        WebElement removeItem = ebayPom.getWebElement("xpath", "//button[normalize-space()='Remove']");
        testCase.validateElementVisibility(cartFeature,removeItem,reportManager);
         removeItem.click();

        //ebayPom.clickElement("xpath", "//button[normalize-space()='Remove']");


        ebayPom.clickElement("xpath", "//a[normalize-space()='Start shopping']");

        WebElement cartIcon = ebayPom.getWebElement("xpath", "//a[@aria-label='Your shopping cart']");
        testCase.validateElementVisibility(cartFeature,cartIcon,reportManager);
        cartIcon.click();
        //ebayPom.clickElement("xpath", "//a[@aria-label='Your shopping cart']");

        WebElement shoppingCart = ebayPom.getWebElement("xpath", "//h1[normalize-space()='Shopping cart']");
        testCase.validateDataFromElement(shoppingCart.getText(),shoppingCart,
                "Shopping cart",cartFeature,reportManager);
        //Assert.assertEquals(shoppingCart.getText(), "Shopping cart", "Text not visible");

        ebayPom.clickElement("xpath", "//a[normalize-space()='Start shopping']");


    }

    @Test(priority = 6)
    public void sell() throws InterruptedException, IOException {

        ExtentTest sellFeature = reportManager.createTestCase("Sell Products",
                "To verify the workflow of the sell feature");

        ebayPom.clickElement("xpath", "//*[@id='gh-p-2']/a");
        WebElement listItem = ebayPom.getWebElement("xpath", "//li//a[normalize-space()='List an item']");
        testCase.validateElementVisibility(sellFeature,listItem,reportManager);
        listItem.click();

        String product = "smart phone";
        WebElement searchProduct = ebayPom.getWebElement("id", "s0-1-1-24-7-@keyword-@box-@input-textbox");
        searchProduct.sendKeys("smart phone");
        testCase.validateDataFromElement("smart phone",searchProduct,product,
                sellFeature,reportManager);
        searchProduct.sendKeys(Keys.ENTER);

        Thread.sleep(5000);
        js.executeScript("window.scrollBy(0,300)");

        WebElement brand = ebayPom.getWebElement("xpath", "//*[@id='mainContent']/div/div/div[1]/div[2]/div/div[3]/div[1]/ul/li[1]/a");
        testCase.validateElementVisibility(sellFeature,brand,reportManager);
        brand.click();

        WebElement model = ebayPom.getWebElement("xpath", "//*[@id='mainContent']/div/div/div[1]/div[3]/div/div[3]/div[1]/ul/li[1]/a");
        testCase.validateElementVisibility(sellFeature,model,reportManager);
        model.click();

        WebElement carrier = ebayPom.getWebElement("xpath", "//*[@id='mainContent']/div/div/div[1]/div[4]/div/div[3]/div[1]/ul/li[4]/a");
        testCase.validateElementVisibility(sellFeature,carrier,reportManager);
        carrier.click();

        WebElement capacity = ebayPom.getWebElement("xpath", "//*[@id='mainContent']/div/div/div[1]/div[5]/div/div[2]/div[1]/ul/li[2]/a");
        testCase.validateElementVisibility(sellFeature,capacity,reportManager);
        capacity.click();

        WebElement color = ebayPom.getWebElement("xpath", "//*[@id='mainContent']/div/div/div[1]/div[6]/div/div[2]/div[1]/ul/li[3]/a");
        testCase.validateElementVisibility(sellFeature,color,reportManager);
        color.click();

        WebElement condition = ebayPom.getWebElement("xpath", "//*[@id='mainContent']/div/div/div[1]/div[8]/div/div[2]/div[1]/ul/li[1]/a");
        testCase.validateElementVisibility(sellFeature,condition,reportManager);
        condition.click();

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(5000);
        js.executeScript("window.scrollBy(200,0)");
        Thread.sleep(3000);

        WebElement continueButton = ebayPom.getWebElement("xpath", "//button[normalize-space()='Continue to list']");
        testCase.validateElementVisibility(sellFeature,continueButton,reportManager);
        continueButton.click();

        Thread.sleep(3000);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        WebElement save = ebayPom.getWebElement("xpath", " //button[@aria-label='Save for later']");
        testCase.validateElementVisibility(sellFeature,save,reportManager);
        save.click();

        js.executeScript("window.scrollBy(0,400)");
        Thread.sleep(4000);
        WebElement checkList = ebayPom.getWebElement("xpath", "//div[@class='checkbox']//input[@id='shui-dt-checkall']");
        checkList.click();

        WebElement deleteDraft = ebayPom.getWebElement("xpath", "//button[normalize-space()='Delete drafts']");
        testCase.validateElementVisibility(sellFeature,deleteDraft,reportManager);
        deleteDraft.click();

        //delete draft dialog : //*[@id="shdrafts-cntr"]/div/div[3]/div/span[2]/div/div[2]

        WebElement deleteButton = ebayPom.getWebElement("xpath", "//button[normalize-space()='Delete']");
        testCase.validateElementVisibility(sellFeature,deleteButton,reportManager);
        deleteButton.click();


    }

    @Test(priority = 7)
    public void dailyDeals() throws IOException {

        ExtentTest Deals = reportManager.createTestCase("Daily Deals",
                "To verify the that offers are available for products");
        //System.out.println("Inside daily deals");
        WebElement dailyDeals = ebayPom.getWebElement("xpath", "//a[normalize-space()='Daily Deals']");
        testCase.validateElementVisibility(Deals,dailyDeals,reportManager);
        dailyDeals.click();
        //ebayPom.clickElement("xpath", "//a[normalize-space()='Daily Deals']");

        WebElement spotlightDeals = ebayPom.getWebElement("xpath", "//h2[normalize-space()='Spotlight Deal']");
        testCase.validateDataFromElement(spotlightDeals.getText(),spotlightDeals,"SPOTLIGHT DEAL",
                Deals,reportManager);
        //Assert.assertEquals(spotlightDeals.getText(), "SPOTLIGHT DEAL", "Deals not available");

        WebElement offerPrice = ebayPom.getWebElement("xpath", "//span[normalize-space()='27% off']");
        testCase.validateDataFromElement(offerPrice.getText(),offerPrice,"27% OFF",
                Deals,reportManager);
        //Assert.assertEquals(offerPrice.getText(), "27% OFF", "Offer Price not available");


    }

    @Test(priority = 8)
    public void helpCorner() throws InterruptedException, IOException {

        ExtentTest help = reportManager.createTestCase("Help and Services",
                "To verify the help and services section is working properly");
        //System.out.println("Inside Help corner");
        WebElement helpServices = ebayPom.getWebElement("xpath", "//a[@class='gh-p'][normalize-space()='Help & Contact']");
       testCase.validateElementVisibility(help,helpServices,reportManager);
        helpServices.click();
        //ebayPom.clickElement("xpath", "//a[@class='gh-p'][normalize-space()='Help & Contact']");

        WebElement search = ebayPom.getWebElement("xpath", "//input[@id='sr-input']");
        //Assert.assertTrue(search.isDisplayed(), "Search not available");
        String msg1 = "buying as guest";
        search.sendKeys("buying as guest");
        testCase.validateDataFromElement("buying as guest",search,msg1,help,
                reportManager);
        search.sendKeys(Keys.ENTER);

        WebElement listTitle = ebayPom.getWebElement("xpath", "//li[2]//span[normalize-space()='Buying as a guest']");
        //Assert.assertEquals(listTitle.getText(), "Buying as a guest", "Option not available");

        WebElement helpMsg1 = ebayPom.getWebElement("xpath", "//li//span[normalize-space()='Buying as a guest']");
        testCase.validateElementVisibility(help,helpMsg1,reportManager);
        helpMsg1.click();
        //ebayPom.clickElement("xpath", "//li//span[normalize-space()='Buying as a guest']");
        WebElement title = ebayPom.getWebElement("xpath", "//h1[normalize-space()='Buying as a guest']");
        testCase.validateDataFromElement(title.getText(),title,"Buying as a guest",help,
                reportManager);
        //Assert.assertEquals(title.getText(), "Buying as a guest", "Title not available");
        driver.navigate().back();

        Thread.sleep(3000);
        System.out.println(driver.getTitle());


        WebElement search2 = ebayPom.getWebElement("xpath", "//input[@id='sr-input']");
        search2.clear();
        search2.sendKeys("hacked account");
        search2.sendKeys(Keys.ENTER);


        WebElement listTitle2 = ebayPom.getWebElement("xpath", "//li//span[normalize-space()='Get help with a hacked account']");
        //Assert.assertEquals(listTitle2.getText(), "Get help with a hacked account", "Option not available");

        WebElement helpMsg2 = ebayPom.getWebElement("xpath", "//li//span[normalize-space()='Get help with a hacked account']");
        testCase.validateElementVisibility(help,helpMsg2,reportManager);
        helpMsg2.click();
        //ebayPom.clickElement("xpath", "//li//span[normalize-space()='Get help with a hacked account']");
        WebElement title2 = ebayPom.getWebElement("xpath", "//h1[normalize-space()='Get help with a hacked account']");
        testCase.validateDataFromElement(title2.getText(), title2,"Get help with a hacked account",
                help,reportManager);
        //Assert.assertEquals(title2.getText(), "Get help with a hacked account", "Title not available");


    }

    @Test(priority = 9)
    public void signOut() throws IOException {

        ExtentTest logOut = reportManager.createTestCase("Sign Out",
                "To verify the sign out feature is working properly");

        WebElement profile = ebayPom.getWebElement("xpath", "//button[@id='gh-ug']");
        String accountText = profile.getText();
        testCase.validateDataFromElement(accountText,profile,"Hi Harrish!",
                logOut,reportManager);
        //Assert.assertEquals(accountText, "Hi Harrish!", "Profile name not visible");

        ebayPom.interactWithElement("xpath",
                "//button[@id='gh-ug']", "hover");
        WebElement signOutLink = ebayPom.getWebElement("xpath", "//a[normalize-space()='Sign out']");
        signOutLink.click();
        //ebayPom.clickElement("xpath", "//a[normalize-space()='Sign out']");

        WebElement signOut = ebayPom.getWebElement("xpath", "//div[@id='signout-banner-text']/h1");
        testCase.validateDataFromElement(signOut.getText(),signOut,"You've signed out.",
                logOut,reportManager);
        //Assert.assertEquals(signOut.getText(), "You've signed out.", "Sign out not visible");


    }

    @AfterMethod
    public void tearDownAfter(Method method) {

        //System.out.println("After method");

        if (method.getName().equalsIgnoreCase("userLogin")) {

            currentDriver.quit();
        }
    }

    @AfterClass
    public void tearDown() throws InterruptedException {

        Thread.sleep(5000);
        reportManager.closeReport();
        System.out.println("After Class");
        driver.close();
    }
}
