package com.hang.testcases.user;

import com.core.helpers.SystemHelper;
import com.core.keywords.WebUI;
import com.hang.common.BaseTest;
import com.hang.dataproviders.DataProvidersProfile;
import com.hang.pages.user.DashboardPage;
import com.hang.pages.user.LoginPage;
import com.hang.pages.user.ProfilePage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.*;
import java.io.IOException;

@Feature("Manage Profile Functionality")
public class ProfileTest extends BaseTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;
    ProfilePage profilePage;

    @Feature("Test Profile")
    @BeforeMethod
    public void setupPrecondition() {
        loginPage = new LoginPage();
        dashboardPage = loginPage.loginCMS();
        profilePage = dashboardPage.clickManageProfile();
    }

    @Story("Check elements display")
    @Test(priority = 1, description = "Verify elements are visible")
    public void checkElementsDisplay() {
        profilePage.verifyElementsDisplay();
    }

    @Story("Upload photo")
    @Test(priority = 2)
    public void testUploadFile_macOS() throws IOException, AWTException {
        WebUI.waitForPageLoaded();

        profilePage.updatePhotoProfile();
        String filePath = SystemHelper.getCurrentDir() + "src/test/resources/datatest/boss1.jpg";

        // Gọi hàm upload
        WebUI.uploadFileWithRobot_macOS(filePath);
        profilePage.selectAndAddPhoto();

        By fileNameAfterUpload = By.xpath("//span[@class='text-truncate title'][1]");
        Assert.assertTrue(WebUI.checkElementExist(fileNameAfterUpload), "❌ Cannot upload file");
        Assert.assertEquals(WebUI.getElementText(fileNameAfterUpload), "boss1", "Tên file không khớp sau upload");
    }

    //Create new Address area trong Profile
    @Story("Add new address form")
    @Test(priority = 3, description = "Add new address form and verify data mapping")
    public void addNewAddressForm() {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String UNIQUE_ADDRESS = "Home " + timeStamp;
        String COUNTRY = "Vietnam";
        String STATE = "Hà Nội";
        String CITY = "Hà Nội";
        String POSTAL_CODE = "10000";
        String PHONE = "088888889";

        profilePage.fillAddressForm(UNIQUE_ADDRESS, COUNTRY, STATE, CITY, POSTAL_CODE, PHONE);
        profilePage.scrollToNewAddress(UNIQUE_ADDRESS);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(profilePage.getAddressValue(UNIQUE_ADDRESS), UNIQUE_ADDRESS, "❌Address does not match");
        softAssert.assertEquals(profilePage.getCountryOfAddress(UNIQUE_ADDRESS), COUNTRY, "❌Country does not match");
        softAssert.assertEquals(profilePage.getStateOfAddress(UNIQUE_ADDRESS), STATE, "❌State does not match");
        softAssert.assertEquals(profilePage.getCityOfAddress(UNIQUE_ADDRESS), CITY, "❌City does not match");
        softAssert.assertEquals(profilePage.getPostalCodeOfAddress(UNIQUE_ADDRESS), POSTAL_CODE, "❌Postal code does not match");
        softAssert.assertEquals(profilePage.getPhoneOfAddress(UNIQUE_ADDRESS), PHONE, "❌Phone does not match");

        softAssert.assertAll();
    }

    //Edit Address area trong Profile
    @Test(priority = 4)
    @Story("Edit existing address form")
    public void editNewlyCreatedAddress() {

        // --- Dữ liệu để tạo mới ---
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String oldAddress = "Old Address " + timeStamp;
        String country = "Vietnam";
        String state = "Hà Nội";
        String city = "Hà Nội";
        String postalCode = "10000";
        String oldPhone = "033333333";

        // --- Dữ liệu để cập nhật ---
        String newAddress = "New Address " + timeStamp;
        String newPhone = "044444444";

        // 1. Thêm mới địa chỉ
        profilePage.fillAddressForm(oldAddress, country, state, city, postalCode, oldPhone);
        profilePage.scrollToNewAddress(oldAddress);

        // 2. Chỉnh sửa địa chỉ vừa tạo
        profilePage.editAddress(oldAddress, newAddress, newPhone);
        profilePage.scrollToNewAddress(newAddress);

        // 3. Kiểm tra lại thông tin đã được cập nhật
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(profilePage.getSuccessMessage().contains("Address info updated successfully"), "❌Flash message success không đúng");
        softAssert.assertEquals(profilePage.getPhoneOfAddress(newAddress), newPhone, "❌Phone chưa được cập nhật");

        softAssert.assertAll();
    }

    // Update Manage profile
    @Story("Update Basic Info by Data Provider")
    @Test(priority = 5, dataProvider = "data_provider_profile_excel", dataProviderClass = DataProvidersProfile.class)
    public void updateDataByDataProvider(String name, String phoneNumber) {
        profilePage.updateBasicInfo(name, phoneNumber);

        Assert.assertTrue(profilePage.getSuccessMessage().contains("Your Profile has been updated successfully!"), "❌Flash message success không đúng");
        WebUI.waitForPageLoaded();

        Assert.assertEquals(profilePage.getNameValue(), name, "❌Name chưa được update");
        Assert.assertEquals(profilePage.getPhoneValue(), phoneNumber, "❌Phone chưa được update");
    }
}
