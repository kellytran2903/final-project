package com.hang.pages.user;

import com.core.keywords.WebUI;
import com.hang.common.BasePage;
import com.hang.common.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;

public class DashboardPage extends BasePage {
    private By dashboardPageTitle = By.xpath("//h1[normalize-space()='Dashboard']");
    private By logoutButton = By.xpath("(//a[normalize-space()='Logout'])/parent::li[@class='list-inline-item']");

    public void verifyDashboardPageDisplay() {
        Assert.assertTrue(WebUI.isElementDisplayed(dashboardPageTitle), "Dashboard page is not displayed");
    }

    public void logoutCMS() {
        WebUI.clickElement(logoutButton);
    }
}
