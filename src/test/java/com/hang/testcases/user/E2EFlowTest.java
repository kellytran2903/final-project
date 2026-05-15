package com.hang.testcases.user;

import com.core.helpers.PropertiesHelper;
import com.core.keywords.WebUI;
import com.hang.common.BaseTest;
import com.hang.dataproviders.DataProvidersAddProducts;
import com.hang.flows.CommonFlow;
import com.hang.models.ProductModel;
import com.hang.pages.admin.DashboardPageAdmin;
import com.hang.pages.admin.LoginPageAdmin;
import com.hang.pages.admin.OrderDetailPage;
import com.hang.pages.user.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class E2EFlowTest extends BaseTest {
    //USER
    LoginPage loginPage;
    DashboardPage dashboardPage;
    CartPage cartPage;
    ProductPage productPage;

    LoginPageAdmin loginPageAdmin;
    DashboardPageAdmin dashboardPageAdmin;
    OrderDetailPage orderDetailPage;

    @BeforeMethod
    public void setupPrecondition() {
        loginPage = new LoginPage();
        dashboardPage = loginPage.loginCMS();
        cartPage = new CartPage();
        productPage = new ProductPage();
        loginPageAdmin = new LoginPageAdmin();
        dashboardPageAdmin = new DashboardPageAdmin();
        orderDetailPage = new OrderDetailPage();
    }

    // Case: Compare 1 product
    @Epic("Include User and Admin")
    @Feature("E2E Flow: Compare data between User and Admin")
    @Test(priority = 2, groups = "E2E")
    public void compareDataBetweenUserAndAdmin() {
        //Add product to cart
        cartPage.navigateToHomePage();
        productPage.openProductModal("Giỏ quà Tết QbAIn");
        productPage.clickAddToCartButton();
        productPage.closeSuccessPopup();

        //Checkout
        cartPage.openCartList();
        cartPage.clickViewCartButtonInCart();
        cartPage.clickContinueToShippingButton();
        cartPage.checkoutProcess();
        cartPage.verifyCheckoutSuccess();

        //Lấy data từ User
        String orderCodeUser = cartPage.getOrderCode();
        String productNameUser = cartPage.getProductNameUser();
        String quantityUser = cartPage.getQuantityUser();
        String productPriceUser = cartPage.getProductPriceUser();
        String productTotalPriceUser = cartPage.getTotalPriceUser();

        //Lưu vào file Properties
//        PropertiesHelper.saveData("ORDER_CODE", orderCodeUser, "src/test/resources/configs/savedData.properties");
//        PropertiesHelper.saveData("PRODUCT_NAME_USER", productNameUser, "src/test/resources/configs/savedData.properties");
//        PropertiesHelper.saveData("QUANTITY_USER", quantityUser, "src/test/resources/configs/savedData.properties");
//        PropertiesHelper.saveData("PRODUCT_PRICE_USER", productPriceUser, "src/test/resources/configs/savedData.properties");
//        PropertiesHelper.saveData("PRODUCT_TOTAL_PRICE_USER", productTotalPriceUser, "src/test/resources/configs/savedData.properties");

        dashboardPage.logoutCMS();

        // Lấy data từ file Properties
//        String expectedOrderCode = PropertiesHelper.getData("ORDER_CODE", "src/test/resources/configs/savedData.properties");
//        System.out.println("Expected Order Code: " + expectedOrderCode);
//        String expectedProductNameUser = PropertiesHelper.getData("PRODUCT_NAME_USER", "src/test/resources/configs/savedData.properties");
//        String expectedQuantityUser = PropertiesHelper.getData("QUANTITY_USER", "src/test/resources/configs/savedData.properties");
//        String expectedProductPriceUser = PropertiesHelper.getData("PRODUCT_PRICE_USER", "src/test/resources/configs/savedData.properties");
//        String expectedProductTotalPriceUser = PropertiesHelper.getData("PRODUCT_TOTAL_PRICE_USER", "src/test/resources/configs/savedData.properties");

        loginPageAdmin.loginAdmin();
        dashboardPageAdmin.openAllOrders();
        dashboardPageAdmin.searchOrderCode(orderCodeUser);
        orderDetailPage.openOrderDetailPage();

        // So sánh
        Assert.assertEquals(orderDetailPage.getProductNameAdmin(), productNameUser, "❌Tên sản phẩm không khớp!");
        Assert.assertEquals(orderDetailPage.getProductQuantityAdmin(), Integer.parseInt(quantityUser), "❌Số lượng sản phẩm không khớp!");
        Assert.assertEquals(orderDetailPage.getProductPriceAdmin(), cartPage.parsePrice(productPriceUser), "❌Giá sản phẩm không khớp!");
        Assert.assertEquals(orderDetailPage.getProductTotalPriceAdmin(), cartPage.parsePrice(productTotalPriceUser), "❌Tổng giá sản phẩm không khớp!");
    }

    //----------------------------------------------------------------------------//
    //Checkout process 5 steps (from View Cart button in cart) + tăng quantity
    @Epic("Only User site")
    @Feature("E2E Checkout Flow (5 steps): Include adding new products, increase quantity, and checkout")
    @Test(priority = 1, groups = "E2E", dataProvider = "dataAddProductsAndQuantity", dataProviderClass = DataProvidersAddProducts.class)
    public void testCheckoutFlow(ProductModel[] productList) {
        CommonFlow commonFlow = new CommonFlow();
        commonFlow.prepareCartData(productList);

        cartPage.openCartList();
        cartPage.clickViewCartButtonInCart();

        double subTotal = 0;

        for (ProductModel product : productList) {
            double price = product.getRecordedPrice();
            double eachProductTotal = cartPage.verifyCartProductInfo(product.getProductName(), price, product.getQuantity());

            subTotal += eachProductTotal;
        }

        cartPage.verifySubTotalCorrectly(subTotal);
        cartPage.clickContinueToShippingButton();
        cartPage.checkoutProcess();
        cartPage.verifyCheckoutSuccess();
    }
}
