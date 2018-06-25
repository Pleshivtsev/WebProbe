package webprobe.seleniumDriver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import webprobe.enums.RemoteBrowserType;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pleshivtsevsv on 31.01.2018.
 */
public class GridWebDriver {

    private WebDriver driver;
//**************************** Геттеры   *******************************************************************************
    public WebDriver getDriver() {
        return driver;
    }

//**************************** Инициализация   *************************************************************************
    private URL convertGridUrl(String stringUrl){
        URL gridUrl;
        try {
            gridUrl = new URL(stringUrl);
            return gridUrl;
        }
        catch (MalformedURLException e) {
            return null;
        }
    }

    private void initGridDriver(URL url, Capabilities capabilities){
        driver = new RemoteWebDriver(url, capabilities);
    }

    private void chromeLaunch(String url){
        Capabilities chromeCaps = new ChromeOptions();
        initGridDriver(convertGridUrl(url),chromeCaps);
    }

    private void chromeHeadlessLaunch(String url){
        Capabilities chromeCaps = new ChromeOptions().setHeadless(true);
        initGridDriver(convertGridUrl(url),chromeCaps);
    }

    private void fireFoxLaunch(String url){
        Capabilities ffCaps = new FirefoxOptions();
        initGridDriver(convertGridUrl(url),ffCaps);
    }

    private void fireFoxHeadlessLaunch(String url){
        Capabilities ffCaps = new FirefoxOptions().setHeadless(true);
        initGridDriver(convertGridUrl(url),ffCaps);
    }


    //**************************** Конструкторы   ***************************************************************************
    public GridWebDriver(URL url, Capabilities capabilities){
        initGridDriver(url, capabilities);
    }

    public GridWebDriver(String url, Capabilities capabilities){
        URL gridUrl = convertGridUrl(url);
        initGridDriver(gridUrl, capabilities);
    }

    public GridWebDriver(String url, RemoteBrowserType remoteBrowserType){
        URL gridUrl = convertGridUrl(url);
        switch (remoteBrowserType){
            case CHROME                 : chromeLaunch(url);break;
            case CHROME_HEADLESS        : chromeHeadlessLaunch(url); break;
            case FIREFOX                : fireFoxLaunch(url);break;
            case FIREFOX_HEADLESS       : fireFoxHeadlessLaunch(url);break;
            default                     : throw new RuntimeException("Unknown remote browser type!");
        }
    }
}