package com.hang.testcases.user;

import com.core.helpers.PropertiesHelper;
import com.core.keywords.WebUI;
import com.core.utils.LogUtils;
import com.hang.common.BaseTest;
import com.hang.pages.user.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Feature("Test Login Functionality")
public class LoginTest extends BaseTest {
    LoginPage loginPage;

    @BeforeMethod (alwaysRun = true)
    public void setupPage() {
        loginPage = new LoginPage();
        loginPage.openLoginPageUser();
    }

    @Test(priority = 1, description = "Login successfully with valid Email and Password", groups = {"smoke", "regression"})
    @Story("Login Valid")
    @Severity(SeverityLevel.CRITICAL)
    public void loginWithValidEmailAndPassword() {
        loginPage.login(
                PropertiesHelper.getValue("user_email"),
                PropertiesHelper.getValue("user_password"));
        WebUI.assertTrue(loginPage.isDashboardPageLoaded(), "Lỗi: Không tìm thấy trang Dashboard sau khi đăng nhập.");
    }

    @Test(priority = 2, description = "Login fail when inputting invalid Password", groups = {"smoke"})
    @Story("Login Invalid")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithInvalidPassword() {
        loginPage.login(PropertiesHelper.getValue("user_email"),
                "Abcd1234sai@");
        WebUI.assertTrue(loginPage.isAtLoginPage(), "Lỗi: User đã bị chuyển trang (đáng lẽ phải ở lại trang Login).");
        WebUI.assertEquals(loginPage.getErrorMessage(), "Invalid login credentials", "Invalid password error message is incorrect.");
    }

    @Test(priority = 3, description = "Login fail when inputting invalid Email", groups = {"regression"})
    @Story("Login Invalid")
    public void loginWithInvalidEmail() {
        loginPage.login("customer123sai@example.com",
                PropertiesHelper.getValue("user_password"));
        WebUI.assertTrue(loginPage.isAtLoginPage(), "Lỗi: User đã bị chuyển trang (đáng lẽ phải ở lại trang Login).");
        WebUI.assertEquals(loginPage.getErrorMessage(), "Invalid login credentials", "Invalid email error message is incorrect.");
    }

    @Test(priority = 4, description = "Login fail when Email is null", groups = {"smoke"})
    @Story("Validation Error")
    public void loginWithEmailNull() {
        loginPage.login("",
                PropertiesHelper.getValue("user_password"));
        WebUI.assertTrue(loginPage.isAtLoginPage(), "Lỗi: User đã bị chuyển trang khi để trống Email.");
        WebUI.assertEquals(loginPage.getValidationError(), "The email field is required when phone is not present.", "Email required validation message is incorrect.");
    }

    @Test(priority = 5, description = "Login fail when Password is null", groups = {"smoke", "regression"})
    @Story("Validation Error")
    public void loginWithPasswordNull() {
        String beforeColor = loginPage.getPasswordBorderColor();
        LogUtils.info("Border color before clicking: " + beforeColor);

        loginPage.login(PropertiesHelper.getValue("user_email"),
                "");

        String afterColor = loginPage.getPasswordBorderColor();
        LogUtils.info("Border color after clicking: " + afterColor);

        WebUI.assertTrue(loginPage.isAtLoginPage(), "Lỗi: Lỗi: User đã bị chuyển trang khi để trống Password");
        WebUI.assertNotEqual(afterColor, beforeColor, "Lỗi: Viền ô Password không đổi màu báo lỗi.");
    }
}
