package com.hang.pages.admin;

import com.core.keywords.WebUI;
import com.hang.common.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class OrderDetailPage extends BasePage {
    private By firstViewButton = By.xpath("(//a[@title='View'])[1]");

    //Data compare
    private By productNameAdmin = By.xpath("//tbody//tr//td[3]//strong");
    private By productQuantityAdmin = By.xpath("//tbody//tr//td[5]");
    private By productPriceAdmin = By.xpath("//tbody//tr//td[6]");
    private By productTotalPriceAdmin = By.xpath("//td[normalize-space()='Total :']//following-sibling::td");

    public void openOderDetailPage(){
        WebUI.waitForPageLoaded();
        WebUI.clickElement(firstViewButton);
        WebUI.waitForPageLoaded();
    }

    private double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replace("$", "").replace(",", "").trim());
    }

    @Step("Get product name at Admin page")
    public String getProductNameAdmin(){
        WebUI.waitForElementVisible(productNameAdmin);
        WebUI.scrollToElement(productNameAdmin);
        return WebUI.getElementText(productNameAdmin);
    }

    @Step("Get product quantity at Admin page")
    public int getProductQuantityAdmin(){
        WebUI.waitForElementVisible(productQuantityAdmin);
        return Integer.parseInt(WebUI.getElementText(productQuantityAdmin).trim());
    }

    @Step("Get product price at Admin page")
    public double getProductPriceAdmin(){
        WebUI.waitForElementVisible(productPriceAdmin);
        return parsePrice(WebUI.getElementText(productPriceAdmin));
    }

    @Step("Get product total price at Admin page")
    public double getProductTotalPriceAdmin(){
        WebUI.waitForElementVisible(productTotalPriceAdmin);
        return parsePrice(WebUI.getElementText(productTotalPriceAdmin));
    }
}