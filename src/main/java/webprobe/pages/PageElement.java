package webprobe.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import webprobe.utils.Assert;

/**
 * Abstract page element
 */
public class PageElement {

    protected boolean doVerify;               // Этот элемент необходимо проверить?
    protected boolean displayed;              // Отображается ли элемент на экране?
    protected boolean verifyResult;           // Результаты проверки элемента
    private  String resultMessage;            // Строка с сообщением об ошибке
    //------------------------------------------------------------------------------------------------------------------
    private By locator;                        // Локатор элемента, по которому можно найти WebElement
    private String name;                       // Понятное имя элемента
    private WebElement webElement;              // Web элемент для Selenium драйвера
    //------------------------------------------------------------------------------------------------------------------
    private String expectedText;            // Ожидаемый текст элемента
    private String expectedValue;            // Ожидаемое value элемента
    private String expectedTitle;            // Ожидаемый Title элемента
    private String expectedAlt;            // Ожидаемый Alt элемента
    private String expectedTag;             // Ожидаемый HTML тэг элемента
    //------------------------------------------------------------------------------------------------------------------
    private String actualText;              // Фактический текст элемента
    private String actualValue;              // Фактическое value элемента
    private String actualTitle;              // Фактический текст элемента
    private String actualAlt;              // Фактический текст элемента
    private String actualTag;               // Фактический HTML тэг элемента


    public PageElement(By locator) {
        this.locator = locator;
        doVerify = true;
    }

    public PageElement(WebElement webElement){
        doVerify = true;
        this.webElement = webElement;
    }


    //**************************** Геттеры   *******************************************************************************
    public By getLocator() { return locator;   }

    public String getName() {
        if (name != null)
            return name;
        else return "";
    }

    public WebElement getWebElement() { return webElement;  }

    public String getExpectedText() {  return expectedText;   }

    public String getExpectedValue() {  return expectedValue;    }

    public String getExpectedTitle() {        return expectedTitle;    }

    public String getExpectedAlt() {        return expectedAlt;    }

    public String getExpectedTag() { return expectedTag;   }

    public String getActualText() {
        if (webElement != null)
            return webElement.getText();
        else {
            Assert.pageAssert("webElement:" + getName() + " ("+ locator +") is null");
            return null;
        }
    }

    public String getActualValue() {    return actualValue;    }

    public String getActualTitle() {       return actualTitle;    }

    public String getActualAlt() {        return actualAlt;    }

    public String getActualTag() { return actualTag;  }

    public boolean getVerifyResult() { return verifyResult;   }

    public boolean isDisplayed() {  return displayed;   }

    public String getResultMessage() {        return resultMessage;    }

    public boolean isVerify() {     return doVerify;    }

    public String getIntelligibleName(){
        String clearName = "";

        if (name != null) clearName += name;
        if (locator !=null) clearName += locator;
        clearName += " ";

        return clearName;
    }

    //**************************** Сеттеры   *******************************************************************************
    public PageElement setLocator(By locator) {
        this.locator = locator;
        return this;
    }

    public PageElement setName(String name) {
        this.name = name;
        return this;
    }

    public PageElement setExpectedText(String expectedText) {
        this.expectedText = expectedText;
        return this;
    }

    public PageElement setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
        return this;
    }

    public PageElement setExpectedTitle(String expectedTitle) {
        this.expectedTitle = expectedTitle;
        return this;
    }

    public PageElement setExpectedAlt(String expectedAlt) {
        this.expectedAlt = expectedAlt;
        return this;
    }

    public PageElement setExpectedTag(String expectedTag) {
        this.expectedTag = expectedTag;
        return this;
    }

    public PageElement setWebElement(WebElement webElement) {
        this.webElement = webElement;
        return this;
    }

    public PageElement clearWebElement(){
        this.webElement = null;
        return this;
    }

    public PageElement verifyThisElement(){
        this.doVerify = true;
        return this;
    }

    public PageElement dontVerifyThisElement(){
        this.doVerify = false;
        return this;
    }

    //**************************** Декораторы над WebElement****************************************************************
    //Набор текста в элементе
    public void type(String text) {
        if (webElement == null)
            Assert.pageAssert("Can't type on element " + name +"(" + locator +"): WebElement is null");

        webElement.clear();
        webElement.sendKeys(text);

    }//public void type(String text)

    //Клик по элементу
    public void click() {
        if (webElement == null)
            Assert.pageAssert("Can't click on element " + name +"(" + locator +"): WebElement is null");
        webElement.click();
    }//public void click() {

    //Enter на элементе
    public void sendEnter(){
        if (webElement == null)
            Assert.pageAssert("Can't send Enter to element " + name +"(" + locator +"): WebElement is null");
        webElement.sendKeys(Keys.ENTER);
    }

    //Сабмит элемента
    public void submit() {
        if (webElement == null)
            Assert.pageAssert("Can't submit on element " + name +"(" + locator +"): WebElement is null");
        webElement.submit();
    }// public void submit()

    //Выбор из выпадающего списка
    public void selectByValue(String string){
        if (webElement == null)
            Assert.pageAssert("Can't select from element " + name +"(" + locator +"): WebElement is null");
        new Select(webElement).selectByValue(string);
    }// public void select

    //Выбор из выпадающего списка
    public void selectByText(String string){
        if (webElement == null)
            Assert.pageAssert("Can't select from element " + name +"(" + locator +"): WebElement is null");
        new Select(webElement).selectByVisibleText(string);
    }// public void select

    //**************************** Очистка элемента   **********************************************************************
    public PageElement reset(){
        actualText = null;
        actualTag = null;
        actualValue = null;
        actualAlt = null;
        actualTitle = null;
        webElement = null;
        resultMessage = "";

        return this;
    }

    //**************************** Вспомогательные утилиты проверки элемента   *********************************************************************
    private void verifyDisplayed(){
        if (!displayed) {
            verifyResult = false;
            resultMessage = "does not displayed!; ";
        }
    }

    private void verifyText(){
        if (expectedText != null && !expectedText.equals(actualText)){
            verifyResult = false;
            resultMessage = resultMessage +
                    "Expected text: " +"\"" + expectedText + "\"" +"; " +
                    "Actual text: " + "\"" +actualText + "\"" +"; ";
        }
    }

    private void verifyValue(){
        if (expectedValue != null && !expectedValue.equals(actualValue)){
            verifyResult = false;
            resultMessage = resultMessage +
                    "Expected value: " +"\"" + expectedValue + "\"" +"; " +
                    "Actual value: " + "\"" +actualValue + "\"" +"; ";
        }
    }

    private void verifyTitle(){
        if (expectedTitle != null && !expectedTitle.equals(actualTitle)){
            verifyResult = false;
            resultMessage = resultMessage +
                    "Expected title: " +"\"" + expectedTitle + "\"" +"; " +
                    "Actual title: " + "\"" +actualTitle + "\"" +"; ";
        }
    }

    private void verifyAlt(){
        if (expectedAlt != null && !expectedAlt.equals(actualAlt)){
            verifyResult = false;
            resultMessage = resultMessage +
                    "Expected alt: " +"\"" + expectedAlt + "\"" +"; " +
                    "Actual alt: " + "\"" + expectedAlt + "\"" +"; ";
        }
    }

    private void verifyTag(){
        if (expectedTag != null && !expectedTag.equals(actualTag)){
            verifyResult = false;
            resultMessage = resultMessage +
                    "Expected tag: " + "\"" + expectedTag + "\"" +"; " +
                    "Actual tag: " + "\"" + actualTag + "\"" + "; ";
        }
    }//private void verifyTag


//**************************** Заполнение и сброс элемента   ******************************************************
    public WebElement fillWebElement(WebDriver driver){
        Assert.pageAssertTrue(locator != null, "Locator of this page element is null");
        WebElement webElement1 = driver.findElement(locator);
        this.webElement = webElement1;
        return webElement1;
    }

//**************************** Обработка ожиданий   ******************************************************
    public void waitForElementIsVisible(WebDriver driver, Integer timeout){
        Assert.pageAssertTrue(locator != null, "Locator of this page element is null");
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementIsNotVisible(WebDriver driver, Integer timeout){
        Assert.pageAssertTrue(locator != null, "Locator of this page element is null");
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(WebDriver driver, Integer timeout){
        Assert.pageAssertTrue(locator != null, "Locator of this page element is null");
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForTextOfElementChanged(WebDriver driver, String oldText, Integer timeout){
        Assert.pageAssertTrue(webElement != null, "webElement of this page element is null");
        WebDriverWait wait = new WebDriverWait(driver,timeout);
        wait.until((RavenDarkholme) -> !(webElement.getText().equals(oldText)));
    }

    public void waitForStalenessOfElement(WebDriver driver, Integer timeout){
        Assert.pageAssertTrue(webElement != null, "webElement of this page element is null");
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.stalenessOf(webElement));
    }


//**************************** Другие методы элемента   ******************************************************
    public void removeReadOnly(WebDriver driver){
        Assert.pageAssertTrue(this != null, "Page element is null");
        ((JavascriptExecutor) driver).executeScript
                ("arguments[0].removeAttribute('readonly','readonly')",webElement);
    }

//**************************** Основной метод проверки элемента   ******************************************************
    public boolean verify() {
        // По умолчанию выставляем результат проверки в true;
        verifyResult = true;

        String errorMessage;
        if (name != null) errorMessage = "Element " + name + ":";
        else errorMessage = "Element (" + locator + "): ";

        resultMessage = "";

        // Если элемент не нуждается в проверке, то записываем положительный результат и возвращаем true
        // Сделано для обработки коллекций элементов
        if (!doVerify){
            verifyResult = true;
            resultMessage = "verifying is disabled for this element";
            return true;
        }
        // Если webElement по какой-то причине null, то возвращаем false
        if (webElement == null) {
            verifyResult = false;
            resultMessage = errorMessage + " has no attached WebElement!";
            return false;
        }

        // Устанавливаем фактические значения элемента
        displayed = webElement.isDisplayed();

        actualText = webElement.getText();
        if (actualText != null) actualText = actualText.trim();

        actualTag = webElement.getTagName();
        actualValue = webElement.getAttribute("value");
        if (actualValue != null ) actualValue = actualValue.trim();

        actualTitle = webElement.getAttribute("title");
        actualAlt = webElement.getAttribute("alt");

        // Выполняем проверку параметров
        verifyDisplayed();
        verifyText();
        verifyValue();
        verifyTitle();
        verifyAlt();
        verifyTag();

        if (!verifyResult) resultMessage = errorMessage + resultMessage;
        else resultMessage = "result Ok";

        return verifyResult;
    }//public void verify()

    public void assertElement(){
        verify();
        if (!verifyResult){
            Assert.pageAssert(resultMessage);
        }
    }

}
