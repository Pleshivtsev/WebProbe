package webprobe;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import webprobe.enums.BrowserType;
import webprobe.enums.RemoteBrowserType;
import webprobe.pages.Page;
import webprobe.pages.PageElement;
import webprobe.seleniumDriver.GridWebDriver;
import webprobe.seleniumDriver.LocalWebDriver;
import webprobe.utils.Assert;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Main WebProbe class
 */
public class WebProbe {
    private WebDriver driver;           // Selenium WebDriver
    private Long currentThread;         // Текущий поток (для отладки)
    private String name;                // Наименование экземпляра
    private String originalWindow;      // Идентификатор начального окна браузера
    private Boolean busy;               // Флаг - экземпляр занят.
    private long implWaitDefault;       // Неявная задержка по умолчанию

    private String currentTestName;
    private String currentStepName;
    private String log;

//**************************** Конструкторы   *******************************************************************************
    public WebProbe(WebDriver driver){
        this.driver = driver;
        setDefaults();
    }

    public WebProbe(BrowserType browserType){
        LocalWebDriver localWebDriver = new LocalWebDriver(browserType);
        this.driver = localWebDriver.getDriver();
        setDefaults();
    }

    public WebProbe(String gridUrl, Capabilities caps){
        GridWebDriver gridWebDriver = new GridWebDriver(gridUrl, caps);
        this.driver = gridWebDriver.getDriver();
        setDefaults();
    }

    public WebProbe(String gridUrl, RemoteBrowserType remoteBrowserType){
        GridWebDriver gridWebDriver = new GridWebDriver(gridUrl, remoteBrowserType);
        this.driver = gridWebDriver.getDriver();
        setDefaults();
    }

//**************************** Внутренние функции   ********************************************************************
    private void verifyDriverNotNull(){
        if (driver != null) return;
        else throw new RuntimeException("Driver was not initialized!");
    }

    private void setDefaults(){
        // Неявная задержка
        setDefaultImplicitlyWait(30);
        this.driver.manage().timeouts().implicitlyWait(implWaitDefault, TimeUnit.SECONDS);

        currentThread = Thread.currentThread().getId();
        originalWindow = driver.getWindowHandle();
    }

//**************************** Геттеры   *******************************************************************************

    public WebDriver getDriver() {
        return driver;
    }

    public Long getCurrentThread() {
        return currentThread;
    }

    public String getOriginalWindow() {
        return originalWindow;
    }

    public String getName() {
        return name;
    }

    public String getCurrentTestName() {
        return currentTestName;
    }

    public String getCurrentStepName() {
        return currentStepName;
    }

    public String getLog() {
        return log;
    }

    //**************************** Сеттеры   *******************************************************************************
    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentTestName(String currentTestName) {
        this.currentTestName = currentTestName;
    }

    public void setCurrentStepName(String currentStepName) {
        this.currentStepName = currentStepName;
    }

    public void setLog(String log) {
        this.log = log;
    }

    //**************************** Трансляторы функций Selenium ************************************************************
    public void stop(){
        driver.quit();
    }

    public void navigateTo(String url){
        verifyDriverNotNull();
        driver.navigate().to(url);
    }

    public WebElement sendKeys(By by, String str){
        WebElement webElement = driver.findElement(by);
        webElement.sendKeys(str);
        return webElement;
    }


    public void click(By by){
        WebElement webElement = driver.findElement(by);
        webElement.click();
    }

    public void click(By locator, Integer timeout){
        WebElement webElement = waitForElementToBeClickableBy(locator, timeout);
        webElement.click();
    }


    public void clickElementContainsText(List<WebElement> webElementList, String elementTextSubString, Integer timeout){
        Assert.shouldBeTrue(webElementList != null, "*** Elements list is null!");
        Assert.shouldBeTrue(webElementList.size() > 0, "*** Elements list is empty!");

        for (WebElement element: webElementList){
            if (element.getText().contains(elementTextSubString)){
                WebDriverWait wait = new WebDriverWait(driver, timeout);
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            }
        }

        Assert.pageAssert("*** There is no element containing text: " + elementTextSubString);
    }


    public void clickElementContainsText(By parentLocator, String elementTextSubString, Integer timeout){
        Assert.shouldBeTrue((elementTextSubString != null) && (elementTextSubString.trim().length()>0), "*** Element text substring null or empty!");
        List<WebElement> webElementList = waitForNumberOfElementsToBeMoreThan(parentLocator, 0 , timeout);
        clickElementContainsText(webElementList, elementTextSubString, timeout);
    }


    public WebElement getWebElementContainsText(List<WebElement> webElementList, String elementTextSubString){
        Assert.shouldBeTrue(webElementList != null, "*** Elements list is null!");
        Assert.shouldBeTrue(webElementList.size() > 0, "*** Elements list is empty!");

        for (WebElement element: webElementList){
            if (element.getText().contains(elementTextSubString)){
                return element;
            }
        }

        Assert.pageAssert("*** There is no element containing text: " + elementTextSubString);

        return null;
    }

    public WebElement getWebElementContainsText(By parentLocator, String elementTextSubString, Integer timeout){
        Assert.shouldBeTrue((elementTextSubString != null) && (elementTextSubString.trim().length()>0), "*** Element text substring null or empty!");
        List<WebElement> webElementList = waitForNumberOfElementsToBeMoreThan(parentLocator, 0 , timeout);
        return getWebElementContainsText(webElementList, elementTextSubString);
    }


    public String getText(By locator){
        return getDriver().findElement(locator).getText();
    }


//**************************** Задержки Selenium ************************************************************
    public void setDefaultImplicitlyWait(long timeout){
        implWaitDefault = timeout;
    }

    public void setImplicitlyWait(long timeout){
        this.driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    public void restoreImplicitlyWait(){
        this.driver.manage().timeouts().implicitlyWait(implWaitDefault, TimeUnit.SECONDS);
    }

    public void sleep(Integer seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//**************************** Работа с окнами и фреймами **************************************************************

    public void maximize(){
        driver.manage().window().maximize();
    }

    public void switchToFrameByName(String frameName){
        driver.switchTo().frame(frameName);
    }

    public void switchToFrameBy(By locator){
        WebElement webElement = driver.findElement(locator);
        driver.switchTo().frame(webElement);
    }

    public void waitForFrameAndSwitchToIt(By locator, Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public void switchToFrmameByIndex(int index){
        driver.switchTo().frame(index);
    }

    public void switchToRootFrame(){
        driver.switchTo().defaultContent();
    }

    //Ожидание появление нового окна
    private ExpectedCondition<String> anyWindowOtherThan(Set<String> oldWindows) {
        return driver -> {
            Set<String> handles = driver.getWindowHandles();
            handles.removeAll(oldWindows);
            return handles.size() > 0 ? handles.iterator().next() : null;
        };
    }

    //Ожидание появление нового окна и затем переключение в него
    public void waitForNewWindowAndSwitch(Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        Set<String> existingWindows = driver.getWindowHandles(); // запоминаем идентификаторы уже открытых окон
        String newWindow = wait.until(anyWindowOtherThan(existingWindows)); // ждем появления нового окна, с новым идентификатором
        driver.switchTo().window(newWindow); // переключаемся в новое окно
    }

    //Wait for new window based on earlier windows list info
    public void waitForNewWindowAndSwitch(Set<String> existingWindows ,Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        String newWindow = wait.until(anyWindowOtherThan(existingWindows)); // ждем появления нового окна, с новым идентификатором
        driver.switchTo().window(newWindow); // переключаемся в новое окно
    }

    //Переключение в оригинальное окно()
    public void switchToOriginalWindow(){
        driver.switchTo().window(originalWindow); // и возвращаемся в исходное окно
    }

    public void closeAndSwitchToOriginalWindow(){
        driver.close();
        switchToOriginalWindow();
    }


//**************************** Функции ************************************************************

    public void capture()   {  busy = true;     }
    public void release()   {  busy = false;    }
    public Boolean isBusy() {  return busy;    }

//**************************** Заполнение элемента ************************************************************
    public void fillWebElement(PageElement pageElement){
        pageElement.fillWebElement(driver);
    }
//**************************** Ожидания  элементов ************************************************************

    public void waitForElementToBeVisibleBy(By locator, Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToBeNotVisibleBy(By locator, Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickableBy(By locator, Integer timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementToBeClickable(WebElement webElement, Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }


    public void waitForTextOfElementChanged(PageElement pageElement, String oldText, Integer timeout){
        fillWebElement(pageElement);
        WebDriverWait wait = new WebDriverWait(driver,timeout);
        wait.until((RavenDarkholme) -> !(pageElement.getActualText().equals(oldText)));
    }

    public void waitForStalenessOfElement(WebElement webElement, Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.stalenessOf(webElement));
    }

    public void waitForNotExistingOfElementBy(By locator, Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.numberOfElementsToBe(locator, 0));
    }

    public List<WebElement> waitForNumberOfElementsToBeMoreThan(By locator, Integer number, Integer timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator,0));
    }



//**************************** Другие методы элемента   ******************************************************
    public void removeReadOnly(PageElement pageElement){
        WebElement webElement = pageElement.fillWebElement(driver);
        ((JavascriptExecutor) driver).executeScript
                ("arguments[0].removeAttribute('readonly','readonly')",webElement);
    }

    public void scrollToElement(WebElement webElement){
        JavascriptExecutor jse = (JavascriptExecutor)getDriver();
        jse.executeScript("arguments[0].scrollIntoView(true);",webElement);
    }

    public void scroll(Integer pixels){
        JavascriptExecutor jse = (JavascriptExecutor)getDriver();
        jse.executeScript("scroll(0,"+ pixels.toString() +");");
    }

}// end of class WebProbe