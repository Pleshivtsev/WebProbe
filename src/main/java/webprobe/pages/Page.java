package webprobe.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import webprobe.WebProbe;
import webprobe.utils.Assert;

import java.util.ArrayList;
import java.util.List;

public class Page {

    protected WebProbe webProbe;

    protected Integer pageTimeOut;                      //Время ожидания появления элементов на странице
    protected String pageUrl;                           // относительный урл страницы
//------------------------------------------------------------------------------------------------------------------
    private List<PageBlock> pageBlocks;                 // Блоки страницы
    private List<String> errorStrings;         // Массив строк-триггеров, которые не должны отображаться на странице
//------------------------------------------------------------------------------------------------------------------
    private PageElement lastLoadElement;                // Элемент страницы, который предположительно загружается последним
    private PageElement invisibleElement;               // Элемент страницы, который должен быть невидимым перед началом проверок
//------------------------------------------------------------------------------------------------------------------

    public Page(WebProbe webProbe){
        setPageTimeOut(30);
        pageBlocks = new ArrayList<>();
        errorStrings = new ArrayList<>();

        this.webProbe = webProbe;
    }

//**************************** Геттеры   *******************************************************************************
    public Integer getPageTimeOut() {
        return pageTimeOut;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public PageElement getLastLoadElement() {
        return lastLoadElement;
    }

    public PageElement getInvisibleElement() {
        return invisibleElement;
    }

    public String getName() { return this.getClass().getName(); }

//**************************** Cеттеры   *******************************************************************************

    public void setPageTimeOut(Integer pageTimeOut) {
        this.pageTimeOut = pageTimeOut;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public void setLastLoadElement(PageElement lastLoadElement) {
        this.lastLoadElement = lastLoadElement;
    }

    public void setInvisibleElement(PageElement invisibleElement) {
        this.invisibleElement = invisibleElement;
    }

//**************************** Моделирование страницы   *******************************************************************************

    public void addPageBlock(PageBlock pageBlock){
        pageBlocks.add(pageBlock);
    }

    public void addErrorString(String errorString){
        errorStrings.add(errorString);
    }

    public void clearErrorStrings(){
        errorStrings.clear();
    }

//**************************** Проверки   *******************************************************************************

    private void checkForErrorStrings(){
        String bodyText;        // Текст страницы
        String errorMessage;    // Генерируемая строка с текстом ошибки для Assert
        List<String> existingErrorStrings = new ArrayList<String>();       // Массив строк-триггеров которые найдены на странице
        By bodyLocator = By.cssSelector("body");

        waitForElementToBeVisible(bodyLocator);
        bodyText = webProbe.getText(bodyLocator);

        for (String errStr : errorStrings){
            if (bodyText.contains(errStr)) {
                existingErrorStrings.add(errStr);
            }
        }

        if (existingErrorStrings.size() > 0){
            errorMessage = getName() + ": Error strings found: ";
            for (String exErrStr : existingErrorStrings){
                errorMessage = errorMessage + "\"" + exErrStr + "\"; ";
            }
            Assert.pageAssert(errorMessage);
        }
    }

    private void waitForLastLoadElementAppears(){
        if (! (lastLoadElement == null)){
            waitForElementToBeVisible(lastLoadElement);
            /*webProbe.waitForElementIsVisible(lastLoadElement, pageTimeOut);*/
        }
    }

    private void waitForInvisibleElementDisappears(){
        if (! (invisibleElement == null)){
            waitForElementToBeNotVisible(invisibleElement);
           /* webProbe.waitForElementIsNotVisible(invisibleElement, pageTimeOut);*/
        }
    }


    public void verify(){

        String resultMessage =  getName() + " page check result:" + "\r\n";
        Boolean verifyResult = true;

        checkForErrorStrings();
        waitForInvisibleElementDisappears();
        waitForLastLoadElementAppears();

        webProbe.setImplicitlyWait(0);
        WebDriver driver = webProbe.getDriver();

        for (PageBlock pageBlock: pageBlocks){
            pageBlock.fillWebElements(driver);
            pageBlock.verify();
            if (pageBlock.getVerifyResult() == false){
                verifyResult = false;
                resultMessage += pageBlock.getResultMessage();
            }
        }

        if (verifyResult == false){
            Assert.pageAssert(resultMessage);
        }

        webProbe.setImplicitlyWait(pageTimeOut);

    }

    private By assertElementLocator(PageElement pageElement){
        Assert.pageAssertTrue(pageElement != null, "Page element is null");
        Assert.pageAssertTrue(pageElement.getLocator() != null, "Locator of this page element is null");
        return pageElement.getLocator();
    }


    public void verifyElement(PageElement pageElement){
        assertElementLocator(pageElement);
        webProbe.fillWebElement(pageElement);
        pageElement.assertElement();
    }

//**************************** Клики   *******************************************************************************

    public void click(By locator){
        WebElement webElement = webProbe.waitForElementToBeClickableBy(locator, pageTimeOut);
        webElement.click();
    }

    public void click(PageElement pageElement){
        By locator = assertElementLocator(pageElement);
        click(locator);
    }

    public void clickAfterVerify(PageElement pageElement){
        By locator = assertElementLocator(pageElement);
        WebElement webElement = webProbe.waitForElementToBeClickableBy(locator, pageTimeOut);
        verifyElement(pageElement);
        pageElement.click();
    }

//**************************** Ожидания  элементов ************************************************************
    private WebDriverWait getWait(){
        WebDriverWait wait = new WebDriverWait(webProbe.getDriver(), pageTimeOut);
        return wait;
    }

    public void waitForElementToBeVisible(By locator){
        getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToBeVisible(PageElement pageElement){
        By locator = assertElementLocator(pageElement);
        waitForElementToBeVisible(locator);
    }

    public void waitForElementToBeNotVisible(By locator){
        getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForElementToBeNotVisible(PageElement pageElement){
        By locator = assertElementLocator(pageElement);
        waitForElementToBeNotVisible(locator);
    }

    public void waitForElementToBeClickable(By locator){
        getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForElementToBeClickable(PageElement pageElement){
        By locator = assertElementLocator(pageElement);
        getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForStalenessOfElement(PageElement pageElement){
        Assert.pageAssertTrue(pageElement != null, "Page element is null");
        WebElement webElement = pageElement.getWebElement();
        Assert.pageAssertTrue(webElement != null, "Web element of page element is null");
        getWait().until(ExpectedConditions.stalenessOf(webElement));
    }

}