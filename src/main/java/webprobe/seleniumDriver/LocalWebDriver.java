package webprobe.seleniumDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import webprobe.enums.BrowserType;

/**
 * Запуск локального экземпляра Selenium Web Driver
 */
public class LocalWebDriver {

    private WebDriver driver;

//**************************** Геттеры   *******************************************************************************
    public WebDriver getDriver() {
        return driver;
    }

//**************************** Конструктор   ***************************************************************************

    public LocalWebDriver(BrowserType browserType){
        browserLaunch(browserType);
    }

//**************************** Запуск локального браузера ***************************************************************
    private void chromeLaunch(){
        driver = new ChromeDriver();
    }

    private void chromeHeadlessLaunch(){
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("window-size=1920,925");
        driver = new ChromeDriver(options);
    }

    private void fireFoxLaunch(){
        driver = new FirefoxDriver();
    }

    private void fireFoxHeadLessLaunch(){
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        options.setLegacy(false);
        driver = new FirefoxDriver(options);
    }

    private void ieLaunch(){        driver = new InternetExplorerDriver();   }
    private void edgeLaunch(){      driver = new EdgeDriver();  }
    private void safariLaunch(){    driver = new SafariDriver();    }

    private void browserLaunch(BrowserType browserType){
        switch (browserType){
            case CHROME             : chromeLaunch(); break;
            case CHROME_HEADLESS    : chromeHeadlessLaunch(); break;
            case FIREFOX            : fireFoxLaunch(); break;
            case FIREFOX_HEADLESS   : fireFoxHeadLessLaunch(); break;
            case EDGE               : edgeLaunch(); break;
            case IE                 : ieLaunch(); break;
            case SAFARI             : safariLaunch(); break;
            default                 : throw new RuntimeException("Unknown browser type!");
        }
    }
}