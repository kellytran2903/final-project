package com.hang.pages.user;

import com.core.keywords.WebUI;
import com.hang.common.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class ProfilePage extends BasePage {
    //LOCATORS
    private By manageProfileTitle = By.xpath("//h1[normalize-space()='Manage Profile']");

    private By basicInfoText = By.xpath("//h5[normalize-space()='Basic Info']");
    private By yourNameLabel = By.xpath("//label[normalize-space()='Your name']");
    private By yourNameTextbox = By.xpath("//input[@placeholder='Your name']");
    private By yourPhoneLabel = By.xpath("//label[normalize-space()='Your Phone']");
    private By yourPhoneTextbox = By.xpath("//input[@placeholder='Your Phone']");
    private By photoLabel = By.xpath("//label[normalize-space()='Photo']");
    private By photoTextbox = By.xpath("//div[@class='form-control file-amount']");
    private By uploadNewButton = By.xpath("//a[normalize-space()='Upload New']");
    private By browserButton = By.xpath("//button[normalize-space()='Browse']");
    private By selectFileButton = By.xpath("//a[normalize-space()='Select File']");
    private By firstPhotoOption = By.xpath("//div[@class='card card-file aiz-uploader-select'][1]");
    private By addFileButton = By.xpath("//button[normalize-space()='Add Files']");
    private By yourPasswordLabel = By.xpath("//label[normalize-space()='Your Password']");
    private By yourPasswordTextbox = By.xpath("//input[@placeholder='New Password']");
    private By confirmPasswordLabel = By.xpath("//label[normalize-space()='Confirm Password']");
    private By confirmPasswordTextbox = By.xpath("//input[@placeholder='Confirm Password']");
    private By updateProfileButton = By.xpath("//button[normalize-space()='Update Profile']");

    private By addressText = By.xpath("//h5[normalize-space()='Address']");
    private By addNewAddressArea = By.xpath("//div[@class='border p-3 rounded mb-3 c-pointer text-center bg-light']");

    private By changeYourEmailText = By.xpath("//h5[normalize-space()='Change your email']");
    private By yourEmailLabel = By.xpath("//label[normalize-space()='Your Email']");
    private By yourEmailTextbox = By.xpath("//input[@placeholder='Your Email']");
    private By updateEmailButton = By.xpath("//button[normalize-space()='Update Email']");

    //*Locators trong Address modal
    private By yourAddressTextbox = By.xpath("//label[normalize-space()='Address']/following::textarea[@name='address']");
    private By yourAddressTextboxEdited = By.xpath("(//label[normalize-space()='Address']/following::textarea[@name='address'])[2]");
    private By yourPostalCodeTextbox = By.xpath("//input[@placeholder='Your Postal Code']");
    private By phoneTextbox = By.xpath("//input[@placeholder='+880']");
    private By phoneTextboxEdited = By.xpath("(//input[@placeholder='+880'])[2]");
    private By saveButton = By.xpath("//button[normalize-space()='Save']");

    //**Locators cho Dropdown
    private By dropdownCountry = By.xpath("//button[@title='Select your country']");
    private By dropdownState = By.xpath("//select[@name='state_id']/following-sibling::button");
    private By dropdownCity = By.xpath("//select[@name='city_id']/following-sibling::button");

    //**Locators cho ô Search
    private By dropdownSearch = By.xpath("//div[@class='dropdown-menu show']//input[@aria-label='Search']");
    private By optionCountry = By.xpath("//span[normalize-space()='Vietnam']");
    private By optionState = By.xpath("//span[normalize-space()='Hà Nội']");
    private By optionCity = By.xpath("//a[@id='bs-select-3-1']//span[@class='text'][contains(text(),'Hà Nội')]");

    private By editButton = By.xpath("//div[@class='dropdown-menu dropdown-menu-right show']//a[@class='dropdown-item'][normalize-space()='Edit']");
    private By saveChangeButton = By.xpath("//div[@id='edit_modal_body']//button[@type='submit'][normalize-space()='Save']");
    private By updateSuccessMessage = By.xpath("//div[@role='alert']");

    // ---ACTIONS
    @Step("Navigate to Manage Profile Page")
    public void navigateToManageProfilePage() {
        WebUI.waitForElementVisible(manageProfileTitle);
        Assert.assertTrue(WebUI.isElementDisplayed(manageProfileTitle), "Manage Profile page is not displayed");
    }

    @Step("Verify elements display")
    public void verifyElementsDisplay() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(WebUI.isElementDisplayed(basicInfoText), "'Basic info' text is not displayed");
        softAssert.assertTrue(WebUI.isElementDisplayed(yourNameTextbox), "'Your name' textbox is not displayed");
        softAssert.assertTrue(WebUI.isElementDisplayed(yourPhoneTextbox), "'Your Phone' textbox is not displayed");
        softAssert.assertTrue(WebUI.isElementDisplayed(photoTextbox), "'Photo' textbox is not displayed");
        softAssert.assertTrue(WebUI.isElementDisplayed(yourPasswordTextbox), "'Your Password' textbox is not displayed");
        softAssert.assertTrue(WebUI.isElementDisplayed(confirmPasswordTextbox), "'Confirm Password' textbox is not displayed");

        softAssert.assertTrue(WebUI.isElementDisplayed(updateProfileButton), "'Update Profile' button is not displayed");

        softAssert.assertTrue(WebUI.isElementDisplayed(addressText), "'Address' text is not displayed");
        softAssert.assertTrue(WebUI.isElementDisplayed(addNewAddressArea), "'Add New Address' area is not displayed");

        softAssert.assertTrue(WebUI.isElementDisplayed(changeYourEmailText), "'Change your email' text is not displayed");
        softAssert.assertTrue(WebUI.isElementDisplayed(yourEmailTextbox), "'Your Email' textbox is not displayed");
        softAssert.assertTrue(WebUI.isElementDisplayed(updateEmailButton), "'Update Email' button is not displayed");

        softAssert.assertAll();
    }

    @Step("Scroll to Address: {0}")
    public void scrollToNewAddress(String addressName) {
        String dynamicXpath = String.format("//div[@class='card-body']//span[normalize-space()='%s']", addressName);
        By addressElement = By.xpath(dynamicXpath);
        WebUI.scrollToElement(addressElement);
    }

    @Step("Open Add New Address Modal")
    public void openAddNewAddressModal() {
        WebUI.scrollToElement(addNewAddressArea);
        WebUI.clickElement(addNewAddressArea);
        WebUI.waitForElementVisible(yourAddressTextbox);
    }

    @Step("Select from dropdown: {0}")
    public void selectFromDropdown(By dropdownTrigger, String value) {
        WebUI.clickElement(dropdownTrigger);
        WebUI.waitForElementVisible(dropdownSearch);
        WebUI.setText(dropdownSearch, value);

        //Chọn phần tử trong list kết quả
        String itemXpath = String.format("//div[contains(@class,'dropdown-menu show')]//span[contains(text(),'%s')]", value);
        By optionDropdown = By.xpath(itemXpath);
        WebUI.clickElement(optionDropdown);
    }

    @Step("Fill Address Form")
    public void fillAddressForm(String address, String country, String state, String city, String postalCode, String phone) {
        openAddNewAddressModal();

        WebUI.setText(yourAddressTextbox, address);
        selectFromDropdown(dropdownCountry, country);
        selectFromDropdown(dropdownState, state);
        selectFromDropdown(dropdownCity, city);
        WebUI.setText(yourPostalCodeTextbox, postalCode);
        WebUI.setText(phoneTextbox, phone);
        WebUI.clickElement(saveButton);
    }

    @Step("Edit Address: {0} -> {1}")
    public void editAddress(String oldAddress, String newAddress, String newPhone) {
        String icon3DotXpath = String.format("//span[normalize-space()='%s']/parent::div/following-sibling::div/button", oldAddress);
        By icon3Dot = By.xpath(icon3DotXpath);

        WebUI.scrollToElement(icon3Dot);
        WebUI.clickElement(icon3Dot);

        WebUI.waitForElementVisible(editButton);
        WebUI.clickElement(editButton);
        WebUI.waitForElementVisible(yourAddressTextboxEdited);

        WebUI.clearText(yourAddressTextboxEdited);
        WebUI.setText(yourAddressTextboxEdited, newAddress);
        WebUI.clearText(phoneTextboxEdited);
        WebUI.setText(phoneTextboxEdited, newPhone);

        WebUI.clickElement(saveChangeButton);
    }

    @Step("Update Address form: {0}, {1}")
    public void updateAddressForm(String name, String phone) {
        WebUI.waitForPageLoaded();

        WebUI.scrollToElement(yourNameTextbox);
        WebUI.clearText(yourNameTextbox);
        WebUI.setText(yourNameTextbox, name);
        WebUI.sleep(1);
        WebUI.clearText(yourPhoneTextbox);
        WebUI.setText(yourPhoneTextbox, phone);
        WebUI.clickElement(updateProfileButton);
    }

    @Step("Scroll to Address card: {0}")
    public void scrollToAddressCard(String addressName) {
        String dynamicXpath = String.format("//div[@class='card-body']//span[normalize-space()='%s']", addressName);
        WebUI.scrollToElement(By.xpath(dynamicXpath));
    }

    @Step("Update Basic Info")
    public void updateBasicInfo(String name, String phone) {
        WebUI.scrollToElement(yourNameTextbox);
        WebUI.clearText(yourNameTextbox);
        WebUI.setText(yourNameTextbox, name);
        WebUI.clearText(yourPhoneTextbox);
        WebUI.setText(yourPhoneTextbox, phone);
        WebUI.clickElement(updateProfileButton);
    }

    private String getAddressInfo(String addressName, String labelName) {
        String dynamicXpath = String.format("//span[normalize-space()='%s']/ancestor::div[contains(@class,'border')]//span[normalize-space()='%s']/following-sibling::span",
                addressName, labelName);
        By elementBy = By.xpath(dynamicXpath);
        WebUI.waitForElementVisible(elementBy);
        return WebUI.getElementText(elementBy);
    }

    public String getAddressValue(String addressName) {
        return getAddressInfo(addressName, "Address:");
    }

    public String getCityOfAddress(String addressName) {
        return getAddressInfo(addressName, "City:");
    }

    public String getStateOfAddress(String addressName) {
        return getAddressInfo(addressName, "State:");
    }

    public String getCountryOfAddress(String addressName) {
        return getAddressInfo(addressName, "Country:");
    }

    public String getPhoneOfAddress(String addressName) {
        return getAddressInfo(addressName, "Phone:");
    }

    public String getPostalCodeOfAddress(String addressName) {
        return getAddressInfo(addressName, "Postal code:");
    }

    public String getSuccessMessage() {
        WebUI.waitForElementVisible(updateSuccessMessage);
        return WebUI.getElementText(updateSuccessMessage);
    }

    //Lấy giá trị thực tế
    public String getNameValue() {
        WebUI.waitForElementVisible(yourNameTextbox);
        return WebUI.getElementAttribute(yourNameTextbox, "value");
    }

    public String getPhoneValue() {
        WebUI.waitForElementVisible(yourPhoneTextbox);
        return WebUI.getElementAttribute(yourPhoneTextbox, "value");
    }

    @Step("Update Photo Profile")
    public void updatePhotoProfile() {
        WebUI.waitForElementVisible(photoTextbox);
        WebUI.clickElement(photoTextbox);
        WebUI.clickElement(uploadNewButton);
        WebUI.clickElement(browserButton);
    }

    public void selectAndAddPhoto() {
        WebUI.clickElement(selectFileButton);
        WebUI.waitForElementVisible(firstPhotoOption);
        WebUI.clickElement(firstPhotoOption);
        WebUI.clickElement(addFileButton);
    }
}
