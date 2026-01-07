package com.hang.testcases.user;

import com.core.helpers.PropertiesHelper;
import com.hang.common.BaseTest;
import com.hang.pages.user.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Feature("Test Login Functionality")
public class LoginTest extends BaseTest {
    LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        loginPage = new LoginPage();
        loginPage.openLoginPageUser();
    }

    @Test(priority = 1, description = "Login successfully with valid Email and Password")
    @Story("Login Valid")
    @Severity(SeverityLevel.CRITICAL)
    public void loginWithValidEmailAndPassword() {
        loginPage.login(
                PropertiesHelper.getValue("user_email"),
                PropertiesHelper.getValue("user_password"));
        Assert.assertTrue(loginPage.isDashboardPageLoaded(), "Login fail - Vẫn ở trang Login");
    }

    @Test(priority = 2, description = "Login fail when inputting invalid Password")
    @Story("Login Invalid")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithInvalidPassword() {
        loginPage.login(PropertiesHelper.getValue("user_email"),
                "123456abc");
        Assert.assertTrue(loginPage.isAtLoginPage(), "Login fail - Vẫn ở trang Login");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid login credentials", "Content of Error message does not match");
    }

    @Test(priority = 3, description = "Login fail when inputting invalid Email")
    @Story("Login Invalid")
    public void loginWithInvalidEmail() {
        loginPage.login("customer123@example.com",
                PropertiesHelper.getValue("user_password"));
        Assert.assertTrue(loginPage.isAtLoginPage(), "Login fail - Vẫn ở trang Login");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid login credentials", "Content of Error message does not match");
    }

    @Test(priority = 4, description = "Login fail when Eamil is null")
    @Story("Validation Error")
    public void loginWithEmailNull() {
        loginPage.login("",
                PropertiesHelper.getValue("user_password"));
        Assert.assertTrue(loginPage.isAtLoginPage(), "Login fail - Vẫn ở trang Login");
        Assert.assertEquals(loginPage.getValidationError(), "The email field is required when phone is not present.", "Email is empty - Please input the valid Email");
    }

    @Test(priority = 5, description = "Login fail when Password is null")
    @Story("Validation Error")
    public void loginWithPasswordNull() {
        String beforeColor = loginPage.getPasswordBoderColor();
        System.out.println("Border color before clicking on: " + beforeColor);

        loginPage.login(PropertiesHelper.getValue("user_email"),
                "");

        String afterColor = loginPage.getPasswordBoderColor();
        System.out.println("Border color after clicking on: " + afterColor);

        Assert.assertTrue(loginPage.isAtLoginPage(), "Login fail - Vẫn ở trang Login");
        Assert.assertNotEquals(afterColor, beforeColor, "Màu border password phải thay đổi khi để trống password");
    }
}
