package com.hang.dataproviders;

import com.hang.models.ProductModel;
import org.testng.annotations.DataProvider;

public class DataProvidersAddProducts {
    //Cho case Search and select product
    @DataProvider(name = "searchAndSelectProduct")
    public Object[][] searchAndSelectProduct() {
        return new Object[][]{
                // Search | Name
                {"Dell", "Dell"},
                {"Giỏ quà Tết", "Giỏ quà Tết THKIo"}
        };
    }

    //Cho case Add new product
    @DataProvider(name = "dataAddProducts")
    public Object[][] dataAddProducts() {
        return new Object[][]{
                {new String[]{"Giỏ quà Tết QbAIn", "Dell"}}
        };
    }

    //Data Provider cho việc tăng quantity và checkout 5 steps
    @DataProvider(name = "dataAddProductsAndQuantity")
    public Object[][] dataAddProductsAndQuantity() {
        ProductModel[] productList = new ProductModel[]{
                new ProductModel("Giỏ quà Tết QbAIn", 1),
                new ProductModel("Dell", 2)
        };

        return new Object[][]{
                {productList}
        };
    }
}
