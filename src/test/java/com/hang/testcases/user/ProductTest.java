package com.hang.testcases.user;

import com.core.keywords.WebUI;
import com.hang.common.BaseTest;
import com.hang.pages.user.DashboardPage;
import com.hang.pages.user.LoginPage;
import com.hang.pages.user.ProductPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {
    LoginPage loginPage;
    ProductPage productPage;

    @Feature("Test Product")
    @BeforeMethod
    public void setupPrecondition() {
        loginPage = new LoginPage();
        loginPage.loginCMS();
        productPage = new ProductPage();
    }

    @Story("Search and select product")
    @Test(priority = 1)
    public void searchAndSelectProduct(String searchKey, String expectedName){
        productPage.searchAndSelectProduct(searchKey);
        String[] actualData = productPage.getProductDetailInfo();

        String actualName = actualData[0];
        WebUI.assertEquals(actualName, expectedName, "Product title is not correct.");
    }

    // Cào data, lưu vào file excel
    @Story("Save product detail into Excel")
    @Test(priority = 2)
    public void crawlProductDetailIntoExcel(){
        String myProduct = "Dell";
        WebUI.waitForPageLoaded();
        productPage.searchAndSelectProduct(myProduct);

        //Lấy thông tin từ Web
        String[] actualProductData = productPage.getProductDetailInfo();

        String actualName = actualProductData[0];
        String actualAvailable = actualProductData[1];
        String actualOriginalPrice = actualProductData[2];
        String actualSolbBy = actualProductData[3];
        String actualDesc = actualProductData[4];

        WebUI.assertNotNull(actualProductData[0], "Kiểm tra mảng dữ liệu sản phẩm trả về không bị Null");

        boolean saved = productPage.setDataProductIntoExcel(actualName, actualAvailable, actualOriginalPrice, actualSolbBy, actualDesc, 1);
        WebUI.assertTrue(saved, "Kiểm tra dữ liệu đã lưu thành công vào Excel");
    }
}
