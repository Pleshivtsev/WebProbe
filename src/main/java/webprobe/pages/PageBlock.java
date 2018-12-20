package webprobe.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import webprobe.utils.Assert;

import java.util.ArrayList;
import java.util.List;

public class PageBlock {

    protected List<PageElement> elements;
    private boolean doVerify;

    public PageBlock(){
        elements = new ArrayList<>();
        doVerify = true;
    }

    public PageBlock(WebDriver driver, By locator){

        if (driver.equals(null)){
            Assert.pageAssert("Selenium driver was not initialized");
            return;
        }

        renew(driver, locator);
    }


    public void addWebElement(WebElement webElement){
        PageElement pageElement = new PageElement(webElement);
        elements.add(pageElement);
    }

    public void addPageElement(PageElement pageElement){
        elements.add(pageElement);
    }


    public void renew(WebDriver driver, By locator){
        List<WebElement> webElements = new ArrayList<>();

        elements.clear();
        webElements = driver.findElements(locator);
        if (webElements.size() == 0){
            Assert.pageAssert("Zero elements found");
            return;
        }

        webElements.forEach(webElement -> addWebElement(webElement));
    }

    public void enableVerify(){
        doVerify = true;
    }

    public void disableVerify(){
        doVerify = false;
    }

    public void verify(){
        if (doVerify && elements.size() > 0){
            elements.forEach(element -> element.verify());
        }
    }

    public PageElement clickNthElement(Integer index){
        if(elements.size()<1){
            Assert.pageAssert("Page block is empty");
            return null;
        }

        PageElement element = elements.get(index - 1);
        element.click();
        return element;
    }
}