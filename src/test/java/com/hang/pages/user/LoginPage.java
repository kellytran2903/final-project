package com.hang.pages.user;

import com.core.helpers.PropertiesHelper;
import com.core.keywords.WebUI;
import com.core.utils.LogUtils;
import com.hang.common.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    // ---1. LOCATORS
    private By inputEmail = By.xpath("//input[@id='email']");
    private By inputPassword = By.xpath("//input[@id='password']");
    private By checkboxRememberMe = By.xpath("//span[normalize-space()='Remember Me']");
    private By buttonLogin = By.xpath("//button[normalize-space()='Login']");

    private By errorMessage1 = By.xpath("//strong[contains(text(),'The email field is required when phone is not pres')]");
    private By errorMessage2 = By.xpath("//div[@role='alert']");
    private By buttonRemovePopup = By.xpath("//div[@class='modal-content position-relative border-0 rounded-0']/button[@data-value='removed']");

    // ---2. ACTIONS (Hạnh động nhỏ)

    @Step("Open Login Page User")
    public void openLoginPageUser() {
        WebUI.openURL(PropertiesHelper.getValue("url_user"));
        WebUI.waitForPageLoaded();
        if (WebUI.checkElementExist(buttonRemovePopup)) {
            WebUI.clickElement(buttonRemovePopup);
        }
    }

    @Step("Set Email: {0}")
    private void setEmail(String email) {
        WebUI.clearText(inputEmail);
        WebUI.setText(inputEmail, email);
    }

    @Step("Set Password: {0}")
    private void setPassword(String password) {
        WebUI.clearText(inputPassword);
        WebUI.setText(inputPassword, password);
    }

    @Step("Click Login Button")
    public void clickLoginButton() {
        WebUI.clickElement(buttonLogin);
    }

    // --- 3. BUSINESS FLOW (Gộp các hành động nhỏ)

    @Step("Login with Email: {0} and Password: {1}")
    public void login(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    // --- 4. VERIFICATION METHODS

    @Step("Get Validation error message")
    public String getValidationError() {
        WebUI.waitForElementVisible(errorMessage1);
        return WebUI.getElementText(errorMessage1);
    }

    @Step("Get Allert error message")
    public String getErrorMessage() {
        WebUI.waitForElementVisible(errorMessage2);
        return WebUI.getElementText(errorMessage2);
    }

    @Step("Verify Allert error message correctly")
    public boolean isAllertErrorDisplayed() {
        return WebUI.isElementDisplayed(errorMessage2);
    }

    @Step("Verify URL contains: {0}")
    public boolean verifyUrlContains(String keyword) {
        return WebUI.getCurrentUrl().contains(keyword);
    }

    @Step("Get Border color of Password input")
    public String getPasswordBoderColor() {
        return WebUI.getBorderColor(inputPassword);
    }

    public boolean isDashboardPageLoaded() {
        return WebUI.getCurrentUrl().contains("dashboard");
    }

    public boolean isAtLoginPage() {
        return WebUI.getCurrentUrl().contains("login");
    }

    public DashboardPage loginCMS() {
        WebUI.openURL(PropertiesHelper.getValue("url_user"));
        WebUI.waitForPageLoaded();
        WebUI.clickElement(buttonRemovePopup);
        WebUI.clearText(inputEmail);
        WebUI.clearText(inputPassword);
        WebUI.setText(inputEmail, PropertiesHelper.getValue("user_email"));
        WebUI.setText(inputPassword, PropertiesHelper.getValue("user_password"));
        WebUI.clickElement(buttonLogin);
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }
}
