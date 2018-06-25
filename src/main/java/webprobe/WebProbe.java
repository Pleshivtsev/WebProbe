package webprobe;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import webprobe.enums.BrowserType;
import webprobe.enums.RemoteBrowserType;
import webprobe.seleniumDriver.GridWebDriver;
import webprobe.seleniumDriver.LocalWebDriver;

import java.util.concurrent.TimeUnit;

/**
 * Main WebProbe class
 */
public class WebProbe {
    private WebDriver driver;           // Selenium WebDriver
    private Long currentThread;         // Текущий поток (для отладки)
    private String originalWindow;      // Идентификатор начального окна браузера
    private Boolean busy;               // Флаг - экземпляр занят.
    private long implWaitDefault;       // Неявная задержка по умолчанию

//**************************** Конструкторы   *******************************************************************************
    public WebProbe(WebDriver driver){
        this.driver = driver;
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

    public WebElement click(By by){
        WebElement webElement = driver.findElement(by);
        webElement.click();
        return webElement;
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

//**************************** Функции ************************************************************

    public void capture()   {  busy = true;     }
    public void release()   {  busy = false;    }
    public Boolean isBusy() {  return busy;    }

}// end of class WebProbe