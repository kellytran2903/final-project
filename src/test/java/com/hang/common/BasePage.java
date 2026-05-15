package com.hang.common;

import com.core.keywords.WebUI;
import com.hang.pages.user.DashboardPage;
import com.hang.pages.user.ProfilePage;
import org.openqa.selenium.By;

public class BasePage {
    //Locators cho Header (User page)
    private By menuDashboard = By.xpath("//div[@class='d-flex align-items-start']//span[normalize-space()='Dashboard']");
    private By menuManageProfile = By.xpath("//div[@class='d-flex align-items-start']//span[@class='aiz-side-nav-text'][normalize-space()='Manage Profile']");
    private By headerHome = By.xpath("//a[contains(text(),'Home')]");
    private By headerMyCart = By.xpath("//div[@id=\"cart_items\"]/a");

    public DashboardPage clickDashboard() {
        WebUI.waitForElementClickable(menuDashboard);
        WebUI.getWebElement(menuDashboard).click();
        return new DashboardPage();
    }

    public ProfilePage clickManageProfile() {
        WebUI.waitForElementClickable(menuManageProfile);
        WebUI.clickElement(menuManageProfile);
        return new ProfilePage();
    }

//    public ProfilePage clickManageProfile() {
//        System.out.println("DEBUG: Bắt đầu clickManageProfile..."); // Dòng 1
//
//        WebUI.waitForElementClickable(menuManageProfile);
//        WebUI.clickElement(menuManageProfile);
//
//        System.out.println("DEBUG: Click xong, chuẩn bị return ProfilePage..."); // Dòng 2
//
//        ProfilePage page = new ProfilePage();
//
//        if (page == null) {
//            System.out.println("DEBUG: Ôi trời ơi, page bị null!"); // Dòng 3 (Không thể xảy ra)
//        } else {
//            System.out.println("DEBUG: Page đã được khởi tạo thành công!");
//        }
//
//        return page;
//    }
}
