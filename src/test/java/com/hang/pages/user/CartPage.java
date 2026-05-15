package com.hang.pages.user;

import com.core.keywords.WebUI;
import com.core.utils.LogUtils;
import com.hang.common.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.core.keywords.WebUI.getElementAttribute;
import static com.core.keywords.WebUI.getElementText;

public class CartPage extends BasePage {
    private By homePage = By.xpath("//div[@class='container']//a[@href='https://cms.anhtester.com'][normalize-space()='Home']");
    private By cakeCatogory = By.xpath("//img[@alt='Cake']");
    private By firstProduct = By.xpath("(//div[@class='position-relative']/div//a[@data-title='Add to cart'])[1]");
    private By secondProduct = By.xpath("(//div[@class='position-relative']/div//a[@data-title='Add to cart'])[2]");
    private By countCartBadge = By.xpath("//span[@class='badge badge-primary badge-inline badge-pill cart-count']");

    private By cartListDropdown = By.xpath("//div[normalize-space()='Cart Items']//parent::div[contains(@class, 'dropdown-menu')]");
    private By viewCartButton = By.xpath("//a[normalize-space()='View cart']");
    private By checkoutButton = By.xpath("//a[normalize-space()='Checkout']");
    private By shippingInfoCard = By.xpath("(//form[@data-toggle='validator']//span[normalize-space()='Address:'])[last()]");
    private By continueToDeliveryButton = By.xpath("//button[normalize-space()='Continue to Delivery Info']");
    private By continueToPaymentButton = By.xpath("//button[normalize-space()='Continue to Payment']");
    private By cashPayment = By.xpath("//span[contains(text(),'Cash on Delivery')]");
    private By aggrementCheckbox = By.xpath("//span[@class='aiz-square-check']");
    private By completeOrderButton = By.xpath("//button[normalize-space()='Complete Order']");
    private By flashSuccessMessage = By.xpath("//span[normalize-space()='Your order has been placed successfully']");
    private By orderSuccessMessage = By.xpath("//h1[normalize-space()='Thank You for Your Order!']");

    //Checkout 5 steps
    private By removeButton = By.xpath("//button[contains(@onclick,'removeFromCart')]");
    private By subTotal = By.xpath("(//span[normalize-space()='Subtotal']//following-sibling::span)[last()]");
    private By emptyText = By.xpath("//h3[normalize-space()='Your Cart is empty']");
    private By continueToShippingButton = By.xpath("//a[normalize-space()='Continue to Shipping']");

    // Element compare
    private By orderCode = By.xpath("//h2[normalize-space(text()) = 'Order Code:']//span");
    private By productNameUser = By.xpath("//tbody//tr//td//a");
    private By quantityUser = By.xpath("//tbody//tr//td[4]");
    private By deliveryTypeUser = By.xpath("//tbody//tr//td[5]");
    private By priceUser = By.xpath("//tbody//tr//td[6]");
    private By totalPriceUser = By.xpath("(//th[normalize-space()='Total'])//following-sibling::td[1]");

    @Step("Navigate to Home Page")
    public void navigateToHomePage() {
        WebUI.clickElement(homePage);
        WebUI.waitForPageLoaded();
    }

    @Step("Get total product badge")
    public int getCartTotalBadge() {
        WebUI.scrollToElement(countCartBadge);
        String totalString = getElementText(countCartBadge);
        LogUtils.info("Cart badge total: " + totalString);
        return Integer.parseInt(totalString);
    }

    @Step("Open cart list")
    public void openCartList() {
        WebUI.clickElement(countCartBadge);
    }

    @Step("Clear products in the cart list")
    public void clearCurrentCart() {
        WebUI.waitForPageLoaded();

        int totalItems = getCartTotalBadge();

        if (totalItems == 0) return;
        openCartList();
        while (WebUI.isElementPresent(removeButton)) {
            try {
                WebUI.clickElement(removeButton);
                WebUI.waitForPageLoaded();
                openCartList();
            } catch (Exception e) {
                openCartList();
            }
        }
    }

    @Step("Check product in cart: {0}")
    public boolean checkProductInCart(String product) {
        String dynamicXpath = String.format("//div[contains(@class,'dropdown-menu')]//*[normalize-space()='%s']", product);
        By productElement = By.xpath(dynamicXpath);
        return WebUI.isElementPresent(productElement);
    }

    @Step("Open Shipping info (from Checkout button")
    public void clickCheckoutButtonInCart(){
        WebUI.clickElement(checkoutButton);
    }

    @Step("Open My Cart Tab (from View Cart button)")
    public void clickViewCartButtonInCart() {
        WebUI.clickElement(viewCartButton);
    }

    @Step("Click on Continue to Shipping button in My Cart tab")
    public void clickContinueToShippingButton() {
        WebUI.clickElement(continueToShippingButton);
    }

    //Checkout from Checkout button (4 steps)
    @Step("Checkout process 4 steps (from Shipping info tab)")
    public void checkoutProcess() {
        WebUI.waitForElementVisible(continueToDeliveryButton);
        WebUI.scrollToElement(shippingInfoCard);
        WebUI.clickElement(shippingInfoCard);

        WebUI.scrollToElement(continueToDeliveryButton);
        WebUI.clickElement(continueToDeliveryButton);

        WebUI.waitForElementVisible(continueToPaymentButton);
        WebUI.scrollToElement(continueToPaymentButton);
        WebUI.clickElement(continueToPaymentButton);

        WebUI.waitForElementVisible(completeOrderButton);
        WebUI.scrollToElement(cashPayment);
        WebUI.clickElement(cashPayment);
        WebUI.scrollToElement(aggrementCheckbox);
        WebUI.clickElement(aggrementCheckbox);
        WebUI.sleep(1);

        WebUI.clickElement(completeOrderButton);
        WebUI.waitForElementVisible(flashSuccessMessage);
    }

    @Step("Verify checkout success")
    public void verifyCheckoutSuccess() {
        Assert.assertTrue(WebUI.isElementDisplayed(flashSuccessMessage), "Flash message is not displayed");
        Assert.assertEquals(WebUI.getElementText(flashSuccessMessage), "Your order has been placed successfully", "Flash message is not displayed");
    }

    @Step("Get order code at User page")
    public String getOrderCode() {
        WebUI.waitForPageLoaded();
        return getElementText(orderCode);
    }

    @Step("Get product name at User page")
    public String getProductNameUser() {
        WebUI.waitForPageLoaded();
        return getElementText(productNameUser);
    }

    @Step("Get quantity at User page")
    public String getQuantityUser() {
        WebUI.waitForPageLoaded();
        return getElementText(quantityUser);
    }

    @Step("Get Product Price at User page")
    public String getProductPriceUser() {
        WebUI.waitForPageLoaded();
        return getElementText(priceUser);
    }

    @Step("Get Total Price at User page")
    public String getTotalPriceUser() {
        WebUI.waitForPageLoaded();
        return getElementText(totalPriceUser);
    }

    //-----------------------------------------------------------//

    public double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replace("$", "").replace(",", "").trim());
    }

    @Step("Verify cart product info: {0}")
    public double verifyCartProductInfo(String productName, double expectedProductPriceFromModal, int expectedQuantity) {
        String dynamicXpathNameProduct = String.format("//span[@class='fs-14 opacity-60'][contains(normalize-space(),'%s')]", productName);
        String dynamicXpathPrice = String.format("(//span[@class='fs-14 opacity-60'][contains(normalize-space(),'%s')]//parent::div//following-sibling::div/span[@class='fw-600 fs-16'])[1]", productName);
        String dynamicXpathTax = String.format("(//span[@class='fs-14 opacity-60'][contains(normalize-space(),'%s')]//parent::div//following-sibling::div/span[@class='fw-600 fs-16'])[2]", productName);
        String dynamicXpathQuantity = String.format("//span[@class='fs-14 opacity-60'][contains(normalize-space(),'%s')]//parent::div//following-sibling::div/div/input", productName);
        String dynamicXpathTotalPriceEachProduct = String.format("(//span[@class='fs-14 opacity-60'][contains(normalize-space(),'%s')]//parent::div//following-sibling::div/span)[last()]", productName);

        By nameProduct = By.xpath(dynamicXpathNameProduct);
        By priceProduct = By.xpath(dynamicXpathPrice);
        By taxProduct = By.xpath(dynamicXpathTax);
        By quantityProduct = By.xpath(dynamicXpathQuantity);
        By totalPriceEachProduct = By.xpath(dynamicXpathTotalPriceEachProduct);

        Assert.assertTrue(WebUI.isElementPresent(nameProduct), "Product was not found in cart: " + productName);

        double uiNetPriceEachProduct = parsePrice(WebUI.getElementText(priceProduct));
        double uiTaxEachProduct = parsePrice(WebUI.getElementText(taxProduct));
        double uiTotalPriceEachProduct = parsePrice(WebUI.getElementText(totalPriceEachProduct));
        int uiQuantityEachProduct = Integer.parseInt(WebUI.getElementAttribute(quantityProduct, "value"));

        double uiGrossPriceEachProduct = uiNetPriceEachProduct + uiTaxEachProduct;

        LogUtils.info("Checking price for product: " + productName);
        LogUtils.info("Modal price (expected): " + expectedProductPriceFromModal);
        LogUtils.info("UI net price: " + uiNetPriceEachProduct);
        LogUtils.info("UI tax: " + uiTaxEachProduct);
        LogUtils.info("UI gross price (net+tax): " + uiGrossPriceEachProduct);

        Assert.assertEquals(uiQuantityEachProduct, expectedQuantity, "❌ Số lượng sản phẩm sai!");
        Assert.assertEquals(uiGrossPriceEachProduct, expectedProductPriceFromModal, "❌ Giá đơn vị (bao gồm thuế) không khớp!");
        Assert.assertEquals(uiTotalPriceEachProduct, uiGrossPriceEachProduct * expectedQuantity, "❌ Tổng giá sản phẩm (đã bao gồm thuế) sai!");

        return uiTotalPriceEachProduct;
    }

    public void verifySubTotalCorrectly(double expectedSubTotal) {
        double uiSubTotal = parsePrice(WebUI.getElementText(subTotal));
        Assert.assertEquals(uiSubTotal, expectedSubTotal, "❌ Tổng giá sản phẩm sai!");
    }
}