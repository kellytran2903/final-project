package com.hang.pages.user;

import com.core.helpers.ExcelHelper;
import com.core.keywords.WebUI;
import com.hang.common.BasePage;
import org.openqa.selenium.By;
import org.testng.Assert;
import io.qameta.allure.Step;

import static com.core.keywords.WebUI.getElementText;

public class ProductPage extends BasePage {

    // ---1. LOCATORS
    //Search
    private By searchProductsTextbox = By.xpath("//input[@name='keyword']");
    private By buttonSearch = By.xpath("//div[@class='input-group-append d-none d-lg-block']//button[@type='submit']");

    private By productDetailTitle = By.xpath("//h1");
    private By addToCartButton = By.xpath("//button[@onclick='addToCart()']");
    private By addToCartSuccessMessage = By.xpath("//h3[normalize-space()='Item added to your cart!']");
    private By closeButton = By.xpath("//button[@aria-label='Close']");

    //    private By productName = By.xpath("//h1[normalize-space()='Dell']");
    private By productSoldBy = By.xpath("//small[contains(text(), 'Sold by:')]/parent::div");
    private By productAvailable = By.xpath("//span[@id='available-quantity']");
    private By productOriginalPrice = By.xpath("//div[normalize-space()='Price:']/following-sibling::div");
    private By productDescription = By.xpath("//div[@id='tab_default_1']");

    private By selectedProductPrice = By.xpath("(//div[contains(normalize-space(), 'Price:')]/parent::div/following-sibling::div/div/strong)[1]");
    private By increaseQuantityBtn = By.xpath("//button[contains(@data-type,'plus')]");

    // ---2. ACTIONS

    @Step("Search and select product: {0}")
    public void searchAndSelectProduct(String productName) {
        WebUI.waitForPageLoaded();
        WebUI.clearText(searchProductsTextbox);
        WebUI.setText(searchProductsTextbox, productName);
        WebUI.clickElement(buttonSearch);

        String dynamicProductXpath = String.format("(//img[@alt='%s'])[last()]", productName);
        By productElement = By.xpath(dynamicProductXpath);

        WebUI.waitForElementVisible(productElement);
        WebUI.clickElement(productElement);
        WebUI.waitForElementVisible(productDetailTitle);
    }

    //Get Product detail from Web, and return String array
    @Step("Get Product Detail Info")
    public String[] getProductDetailInfo() {
        WebUI.waitForPageLoaded();
        String name = WebUI.getElementText(productDetailTitle).trim();
        String available = WebUI.getElementText(productAvailable).trim();
        String originalPrice = WebUI.getElementText(productOriginalPrice).trim();

        String soldByText = WebUI.getElementText(productSoldBy);
        String cleanSoldBy = soldByText.replace("Sold by:", "");

        String description = WebUI.getElementText(productDescription).trim();

        return new String[]{name, available, originalPrice, cleanSoldBy, description};
    }

    @Step("Open product modal: {0}")
    public void openProductModal(String productName) {
        WebUI.waitForPageLoaded();
        String dynamicXpath = String.format("//div[@id='section_newest']//img[@alt='%s']/ancestor::div[contains(@class,'position-relative')]//a[@data-title='Add to cart']", productName);
        By productElement = By.xpath(dynamicXpath);
        WebUI.scrollToElement(productElement);
        WebUI.clickElement(productElement);
        WebUI.waitForElementVisible(increaseQuantityBtn);
    }

    @Step("Get selected product price of {0}")
    public double getSelectedProductPrice(String productName) {
        WebUI.waitForElementVisible(increaseQuantityBtn);
        String price = WebUI.getElementText(selectedProductPrice);
        String cleanPrice = price.replace("$", "").replace(",", "").trim();
        return Double.parseDouble(cleanPrice);
    }

    @Step("Increase quantity from Modal to: {0}")
    public void increaseQuantityFromModal(int targetQuatity) {
        if (targetQuatity > 1) {
            WebUI.waitForElementVisible(increaseQuantityBtn);
            for (int i = 1; i < targetQuatity; i++) {
                WebUI.clickElement(increaseQuantityBtn);
            }
        }
    }

    @Step("Click Add to Cart Button")
    public void clickAddToCartButton() {
        WebUI.clickElement(addToCartButton);
    }

    public void verifyAddToCartSuccess() {
        WebUI.waitForElementVisible(addToCartSuccessMessage);

    }

    @Step("Close Success Popup")
    public void closeSuccessPopup() {
        WebUI.clickElement(closeButton);
    }

    //Set Product Detail into file Excel
    @Step("Save Product Detail into Excel")
    public boolean setDataProductIntoExcel(String name, String available, String originalPrice, String soldBy, String description, int row) {
        try {
            ExcelHelper excelHelper = new ExcelHelper();
            excelHelper.setExcelFile("src/test/resources/datatest/Product Detail.xlsx", "Product detail");
            excelHelper.setCellData(name, 0, row);
            excelHelper.setCellData(available, 1, row);
            excelHelper.setCellData(originalPrice, 2, row);
            excelHelper.setCellData(soldBy, 3, row);
            excelHelper.setCellData(description, 4, row);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //-----------------------------------------------------------//
    //Hàm gộp Add Product và quantity
    @Step("Add product '{0}' with quantity '{1}'")
    public void addProductToCartAndQuantity(String productName, int quantity) {
        WebUI.waitForPageLoaded();
        String dynamicXpath = String.format("//div[@id='section_newest']//img[@alt='%s']/ancestor::div[contains(@class,'position-relative')]//a[@data-title='Add to cart']", productName);
        By productElement = By.xpath(dynamicXpath);
        WebUI.scrollToElement(productElement);
        WebUI.clickElement(productElement);
        WebUI.sleep(1);

        if (quantity > 1) {
            increaseQuantityFromModal(quantity);
        }

        WebUI.clickElement(addToCartButton);
    }
}