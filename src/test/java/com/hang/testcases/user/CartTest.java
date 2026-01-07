package com.hang.testcases.user;

import com.core.keywords.WebUI;
import com.hang.common.BaseTest;
import com.hang.dataproviders.DataProvidersAddProducts;
import com.hang.flows.CommonFlow;
import com.hang.models.ProductModel;
import com.hang.pages.user.DashboardPage;
import com.hang.pages.user.CartPage;
import com.hang.pages.user.LoginPage;
import com.hang.pages.user.ProductPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;
    CartPage cartPage;
    ProductPage productPage;

    @Feature("Test Cart")
    @BeforeMethod
    public void setupPrecondition() {
        loginPage = new LoginPage();
        dashboardPage = loginPage.loginCMS();
        cartPage = new CartPage();
        productPage = new ProductPage();
    }

    //Cách 1
    //Add 2 products khác nhau vào giỏ hàng bằng cách thêm lần lượt, không dùng Data Provider
//    @Test(priority = 1)
//    public void addNewProductToCart(){
//        String product1 = "Giỏ quà Tết QbAIn";
//        String product2 = "Dell";
//
//        cartPage.navigateToHomePage();
//        int beforeTotal = cartPage.getCartTotalBadge();
//        cartPage.addProductToCart(product1);
//        cartPage.verifyAddToCartSuccess();
//        cartPage.closeSuccessPopup();
//
//        int afterTotal1 = cartPage.getCartTotalBadge();
//        WebUI.sleep(1);
//        Assert.assertEquals(afterTotal1, beforeTotal + 1, "❌Total badge of the first selection is NOT correct");
//
//        WebUI.sleep(1);
//        cartPage.addProductToCart(product2);
//        cartPage.verifyAddToCartSuccess();
//        cartPage.closeSuccessPopup();
//
//        int afterTotal2 = cartPage.getCartTotalBadge();
//        WebUI.sleep(1);
//        Assert.assertEquals(afterTotal2, afterTotal1 + 1, "❌Total badge of the second selection is NOT correct");
//        cartPage.openCartList();
//        WebUI.sleep(1);
//
//        Assert.assertTrue(cartPage.checkProductInCart(product1),"❌Sản phẩm '" + product1 + "' không tìm thấy trong giỏ hàng");
//        Assert.assertTrue(cartPage.checkProductInCart(product2),"❌Sản phẩm '" + product2 + "' không tìm thấy trong giỏ hàng");
//    }

    // Checkout 4 steps (from Checkout button in Cart) + Không tăng quantity
    // Cách 2: Add new Products từ Data Provider(Different products)
    @Story("Add new product")
    @Test(priority = 1, dataProvider = "dataAddProducts", dataProviderClass = DataProvidersAddProducts.class)
    public void addNewProductFromDataProvider(String[] productList) {
        cartPage.navigateToHomePage();
        cartPage.clearCurrentCart();

        //Với mỗi tên sản phẩm nằm trong productList
        for (String productName : productList) {
            int beforeTotal = cartPage.getCartTotalBadge();
            productPage.openProductModal(productName);
            productPage.clickAddToCartButton();
            productPage.verifyAddToCartSuccess();
            productPage.closeSuccessPopup();
            WebUI.sleep(1);

            int afterTotal = cartPage.getCartTotalBadge();
            Assert.assertEquals(afterTotal, beforeTotal + 1, "❌Total badge is NOT correct after adding: " + productName);
        }
        cartPage.openCartList();
        WebUI.sleep(1);

        //Kiểm tra lại xem đủ sản phẩm chưa
        for (String productName : productList) {
            Assert.assertTrue(cartPage.checkProductInCart(productName), "❌Sản phẩm '" + productName + "' không tìm thấy trong giỏ hàng");
        }
    }

    //Checkout 4 steps, đi từ Checkout button của Cart, không add new product
    @Story("Checkout from Checkout button")
    @Test(priority = 2)
    public void checkoutProcess() {
        cartPage.openCartList();
        cartPage.clickCheckoutButtonInCart();
        cartPage.checkoutProcess();
        cartPage.verifyCheckoutSuccess();
    }

    //----------------------------------------------------------------------------//
    @Story("Add 2 products to Cart, and increase quantiy")
    @Test(priority = 3, dataProvider = "dataAddProductsAndQuantity", dataProviderClass = DataProvidersAddProducts.class)
    public void testAddToCart(ProductModel[] productList) {
        cartPage.navigateToHomePage();
        cartPage.openCartList();
        cartPage.clearCurrentCart();

        cartPage.navigateToHomePage();
        int beforeBadge = cartPage.getCartTotalBadge();

        for (ProductModel product : productList) {
            productPage.addProductToCartAndQuantity(product.getProductName(), product.getQuantity());

            productPage.verifyAddToCartSuccess();
            productPage.closeSuccessPopup();
        }

        int afterBadge = cartPage.getCartTotalBadge();
        int expectedBadge = beforeBadge + productList.length;
        Assert.assertEquals(afterBadge, expectedBadge, "❌ Badge is displayed incorrectly");
    }
}