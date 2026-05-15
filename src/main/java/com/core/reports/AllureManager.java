package com.core.reports;

import com.core.drivers.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class AllureManager {
    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    //Screenshot attachments for Allure
    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] saveScreenshotPNG() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    public static void saveFailureLog(String message, String actual, String expected) {
        // 1. Tạo một Step ảo đánh dấu là FAILED để làm nổi bật trong Report
        Allure.step("ASSERT FAILED: " + message, Status.FAILED);

        // 2. Đính kèm text Actual và Expected (Nếu có)
        if (actual != null) {
            Allure.addAttachment("Actual Value", actual);
        }
        if (expected != null) {
            Allure.addAttachment("Expected Value", expected);
        }
    }
}
