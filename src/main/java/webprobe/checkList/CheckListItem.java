package webprobe.checkList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckListItem {

    private WebElement webElement;
    private By locator;

    private String text;
    private String title;
    private String value;
    private boolean shouldNotBeDisplayed;

    public CheckListItem(){
        this.shouldNotBeDisplayed = false;
    }

    public boolean check(){

        return false;
    }




}