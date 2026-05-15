package com.hang.flows;


import com.core.keywords.WebUI;
import com.core.utils.LogUtils;
import com.hang.models.ProductModel;
import com.hang.pages.user.CartPage;
import com.hang.pages.user.ProductPage;

public class CommonFlow {
    CartPage cartPage = new CartPage();
    ProductPage productPage = new ProductPage();

    public void prepareCartData(ProductModel[] productList) {

        cartPage.navigateToHomePage();
        cartPage.clearCurrentCart();
        cartPage.navigateToHomePage();
        WebUI.waitForPageLoaded();

        //Vòng lặp Add product
        for (ProductModel product : productList) {
            String name = product.getProductName();
            int quantity = product.getQuantity();

            LogUtils.info("Adding product to cart: " + name);

            productPage.openProductModal(name);

            //Lấy original price của mỗi Product ghi vào Model
            double originalPrice = productPage.getSelectedProductPrice(name);
            product.setRecordedPrice(originalPrice);
            LogUtils.info("Recorded product price. Name=" + name + " | price=" + originalPrice);

            productPage.increaseQuantityFromModal(quantity);

            productPage.clickAddToCartButton();
            productPage.verifyAddToCartSuccess();
            productPage.closeSuccessPopup();
        }
    }
}
