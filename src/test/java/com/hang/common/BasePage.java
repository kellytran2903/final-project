package com.hang.common;

import com.core.keywords.WebUI;
import com.hang.pages.user.DashboardPage;
import com.hang.pages.user.ProfilePage;
import org.openqa.selenium.By;

public class BasePage {
    //Locators cho Header (User page)
    private By menuDashboard = By.xpath("//div[@class='d-flex align-items-start']//span[normalize-space()='Dashboard']");
    private By menuManageProfile = By.xpath("//div[@class='d-flex align-items-start']//span[@class='aiz-side-nav-text'][normalize-space()='Manage Profile']");
    private By headerHome = By.xpath("//a[contains(text(),'Home')]");
    private By headerMyCart = By.xpath("//div[@id=\"cart_items\"]/a");

    public DashboardPage clickDashboard() {
        WebUI.waitForElementClickable(menuDashboard);
        WebUI.getWebElement(menuDashboard).click();
        return new DashboardPage();
    }

    public ProfilePage clickManageProfile() {
        WebUI.waitForElementClickable(menuManageProfile);
        WebUI.clickElement(menuManageProfile);
        return new ProfilePage();
    }
}
