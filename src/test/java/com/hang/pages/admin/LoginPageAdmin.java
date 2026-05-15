package com.hang.pages.admin;

import com.core.helpers.PropertiesHelper;
import com.core.keywords.WebUI;
import com.hang.pages.user.DashboardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

public class LoginPageAdmin{
    //Elements at Login page
    private By inputEmail = By.xpath("//input[@id='email']");
    private By inputPassword = By.xpath("//input[@id='password']");
    private By buttonLogin = By.xpath("//button[normalize-space()='Login']");
    private By errorMessage = By.xpath("//div[@class='alert alert-danger']");

    @Step("Login Admin")
    public DashboardPage loginAdmin(){
        WebUI.openUrl(PropertiesHelper.getValue("admin_url"));
        WebUI.waitForPageLoaded();
        WebUI.setText(inputEmail, PropertiesHelper.getValue("admin_email"));
        WebUI.setText(inputPassword, PropertiesHelper.getValue("admin_password"));
        WebUI.clickElement(buttonLogin);
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }

    public void verifyLoginFail(){
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.isElementPresent(errorMessage), "Error message is not displayed");
        Assert.assertEquals(WebUI.getElementText(errorMessage), "Invalid login credentials", "Content of error message is not match.");
    }
}
