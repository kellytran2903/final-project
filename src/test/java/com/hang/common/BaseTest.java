package com.hang.common;

import com.core.drivers.DriverManager;
import com.core.helpers.CaptureHelper;
import com.core.helpers.PropertiesHelper;
import com.core.utils.LogUtils;
import com.hang.listeners.TestListener;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Listeners(TestListener.class)
public class BaseTest {
    @BeforeSuite
    public void setupEnvironment() {
        PropertiesHelper.loadAllFiles();
    }

    @Step("Khởi chạy trình duyệt {0}")
    @BeforeMethod(alwaysRun = true)
    public void createDriver(@Optional("chrome") String browser) {
        WebDriver driver;

        // Đọc cấu hình BROWSER từ file properties (nếu có)
        if (!PropertiesHelper.getValue("BROWSER").isEmpty()) {
            browser = PropertiesHelper.getValue("BROWSER");
        }

        // Mặc định là false nếu không có trong file config
//        boolean headless = Boolean.parseBoolean(PropertiesHelper.getValue("HEADLESS"));
        // Logic: Ưu tiên lấy từ câu lệnh Maven (-DHEADLESS=true), nếu không có mới lấy từ file config
        String headlessConfig = System.getProperty("HEADLESS");
        if (headlessConfig == null) {
            headlessConfig = PropertiesHelper.getValue("HEADLESS");
        }
        boolean headless = Boolean.parseBoolean(headlessConfig);

        //CÁCH 1:
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions options = new ChromeOptions();

                // ⚙️ Cấu hình để tắt các popup password, autofill, notification
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false); // Tắt Chrome Password Manager
                prefs.put("profile.password_manager_enabled", false); // Tắt Save password prompt
                prefs.put("autofill.profile_enabled", false); // Tắt autofill form
                prefs.put("autofill.credit_card_enabled", false);
                options.setExperimentalOption("prefs", prefs);

                options.addArguments("--disable-save-password-bubble");
                options.addArguments("--disable-notifications");
                options.addArguments("--disable-popup-blocking");
                options.addArguments("--disable-extensions");
//                options.addArguments("--start-maximized");
                options.addArguments("--disable-infobars");
                options.addArguments("--remote-allow-origins=*"); // Fix lỗi connection bản Chrome mới

                // ⚙️ CẤU HÌNH HEADLESS
                if (headless) {
                    options.addArguments("--headless=new"); // Chế độ Headless mới nhất (Chrome 109+)
                    options.addArguments("--window-size=1920,1080"); // ⚠️ QUAN TRỌNG: Phải set size cứng khi headless
                    LogUtils.info("🚀 Đang chạy Chrome ở chế độ HEADLESS");
                } else {
                    options.addArguments("--start-maximized"); // Chỉ maximize khi có giao diện
                }

                driver = new ChromeDriver(options);
                LogUtils.info("\uD83C\uDF10 Khởi chạy trình duyệt Chrome (đã tắt password popup)");
                break;

            case "safari":
                // Safari ít hỗ trợ headless qua config đơn giản, thường chạy UI thực
                driver = new SafariDriver();
                if (headless) {
                    LogUtils.warn("⚠️ Safari không hỗ trợ chế độ Headless qua Selenium config thông thường.");
                }
                LogUtils.info("Khởi chạy trình duyệt Safari");
                break;

            default:
                driver = new ChromeDriver();
                LogUtils.info("Khởi chạy trình duyệt Chrome (default)");
                break;
        }

        // Lưu WebDriver vào ThreadLocal
        DriverManager.setDriver(driver);
        if (!headless) {
            DriverManager.getDriver().manage().window().maximize();
        }
        DriverManager.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
    }

    @AfterMethod
    @Step("Close driver")
//    public void closeDriver() {
//        try {
//            CaptureHelper.stopRecord();
//        } catch (Exception e) {
//            LogUtils.warn("⚠️ Không thể stop record: " + e.getMessage());
//        }
//
//        if (DriverManager.getDriver() != null) {
//            DriverManager.quit();
//            LogUtils.info("🔻 Đã đóng trình duyệt và cleanup driver");
//        }
//    }

    //CÁCH 2: CHẠY HEADLESS TRÊN GIT ACTIONS (không capture/ record)
    public void closeDriver() {
        // 👇 SỬA ĐOẠN NÀY 👇
        String headless = System.getProperty("HEADLESS");
        if (headless == null) {
            headless = PropertiesHelper.getValue("HEADLESS");
        }

        // Chỉ dừng quay nếu không phải headless
        if ("false".equalsIgnoreCase(headless)) {
            try {
                CaptureHelper.stopRecord();
            } catch (Exception e) {
                LogUtils.warn("⚠️ Không thể stop record: " + e.getMessage());
            }
        }
        // 👆 ---------------- 👇

        if (DriverManager.getDriver() != null) {
            DriverManager.quit();
            LogUtils.info("🔻 Đã đóng trình duyệt và cleanup driver");
        }
    }
}
