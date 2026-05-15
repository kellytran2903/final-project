package com.hang.pages.admin;

import com.core.keywords.WebUI;
import io.qameta.allure.Step;
import net.bytebuddy.asm.Advice;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DashboardPageAdmin {
    private By menuDashboard = By.xpath("//span[normalize-space()='Dashboard']");
    private By menuSales = By.xpath("//span[normalize-space()='Sales']");
    private By subMenuAllOrders = By.xpath("//span[normalize-space()='All Orders']");

    private By searchOrderCode = By.xpath("//input[@id='search']");
    private By filterButton = By.xpath("//button[normalize-space()='Filter']");

    public void verifyDashboardPageLoaded(){
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.isElementDisplayed(menuDashboard), "❌Dashboard page is not displayed");
    }

    @Step("Open All Orders")
    public void openAllOrders(){
        WebUI.waitForPageLoaded();
        WebUI.clickElement(menuSales);
        WebUI.clickElement(subMenuAllOrders);
    }

    @Step("Search Order Code: {0}")
    public void searchOrderCode(String orderCode){
        WebUI.waitForPageLoaded();
        WebUI.clickElement(searchOrderCode);
        WebUI.setText(searchOrderCode, orderCode);
        WebUI.sleep(1);
        WebUI.clickElement(filterButton);
    }
}
