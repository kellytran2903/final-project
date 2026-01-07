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
    @BeforeMethod
    public void createDriver(@Optional("chrome") String browser) {
        WebDriver driver;

        // Đọc cấu hình BROWSER từ file properties (nếu có)
        if (!PropertiesHelper.getValue("BROWSER").isEmpty()) {
            browser = PropertiesHelper.getValue("BROWSER");
        }

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
                options.addArguments("--start-maximized");
                options.addArguments("--disable-infobars");

                driver = new ChromeDriver(options);
                LogUtils.info("✅ Khởi chạy trình duyệt Chrome (đã tắt password popup)");
                break;

            case "safari":
                driver = new SafariDriver();
                LogUtils.info("Khởi chạy trình duyệt Safari");
                break;

            default:
                driver = new ChromeDriver();
                LogUtils.info("Khởi chạy trình duyệt Chrome (default)");
                break;
        }

        // Lưu WebDriver vào ThreadLocal
        DriverManager.setDriver(driver);
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
    }

    @AfterMethod
    @Step("Close driver")
    public void closeDriver() {
        try {
            CaptureHelper.stopRecord();
        } catch (Exception e) {
            LogUtils.warn("⚠️ Không thể stop record: " + e.getMessage());
        }

        if (DriverManager.getDriver() != null) {
            DriverManager.quit();
            LogUtils.info("🔻 Đã đóng trình duyệt và cleanup driver");
        }
    }
}
