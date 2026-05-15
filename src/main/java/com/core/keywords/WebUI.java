package com.core.keywords;

import com.aventstack.extentreports.Status;
import com.core.drivers.DriverManager;
import com.core.helpers.PropertiesHelper;
import com.core.reports.AllureManager;
import com.core.utils.LogUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class WebUI {

    private static int TIMEOUT = Integer.parseInt(PropertiesHelper.getValue("EXPLICIT_WAIT"));
    private static int STEP_TIME = Integer.parseInt(PropertiesHelper.getValue("STEP_TIME"));

    // * Chờ cho đến khi element hiển thị trên giao diện và trả về element đó.
    public static WebElement waitForElementVisible(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            LogUtils.info("Timeout waiting for the element Visible: " + by.toString());
            Assert.fail("Timeout waiting for the element Visible: " + by.toString());
            return null;
        }
    }

    public static boolean verifyElementVisible(By by, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            // Không tìm thấy hoặc timeout -> Trả về false ngay lập tức
            return false;
        }
    }

    public static boolean isElementPresent(By by) {
        List<WebElement> webElementList = getWebElements(by);
        boolean present = !webElementList.isEmpty();
        LogUtils.info("Element present=" + present + " | locator=" + by);
        return present;
    }

    @Deprecated
    public static boolean checkElementExist(By by) {
        return isElementPresent(by);
    }

    public static void waitForElementClickable(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Throwable error) {
            LogUtils.info("Timeout waiting for the element Clickable" + by.toString());
            Assert.fail("Timeout waiting for the element Clickable" + by.toString());
        }
    }

    public static void waitForPageLoaded() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(30), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

        ExpectedCondition<Boolean> jsLoad = driver ->
                js.executeScript("return document.readyState").toString().equals("complete");

        try {
            wait.until(jsLoad);
        }
        // 2. Bắt lỗi cụ thể hơn
        catch (TimeoutException e) {
            LogUtils.info("Timeout waiting for page to load.");
            Assert.fail("FAILED. Timeout waiting for page load.");
        } catch (Exception e) {
            LogUtils.info("An unexpected error occurred while waiting for page load: " + e.getMessage());
            Assert.fail("FAILED. An unexpected error occurred while waiting for page load.");
        }
    }

    public static void sleep(double second) {
        try {
            Thread.sleep((long) (1000 * second));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //* Tìm kiếm một WebElement một cách an toàn (đã bao gồm 'wait' bên trong).
    public static WebElement getWebElement(By by) {
        return waitForElementVisible(by); // Ủy thác toàn bộ công việc chờ và tìm kiếm cho hàm waitForElementVisible
    }

    public static List<WebElement> getWebElements(By by) {
        return DriverManager.getDriver().findElements(by);
    }

    public static boolean isElementDisplayed(By by) {
        try {
            WebElement element = DriverManager.getDriver().findElement(by);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static void openUrl(String url) {
        DriverManager.getDriver().get(url);
        sleep(STEP_TIME);
        LogUtils.info("Open URL: " + url);
    }

    @Deprecated
    public static void openURL(String url) {
        openUrl(url);
    }

    public static void clearText(By by) {
        LogUtils.info("Clear text on element" + by);
        getWebElement(by).clear();
    }

    public static void setText(By by, String value) {
        waitForElementVisible(by);
        sleep(STEP_TIME);
        getWebElement(by).sendKeys(value);
        LogUtils.info("Set text " + value + " on element " + by);
    }

    public static String getElementText(By by) {
//        waitForElementVisible(by);
        LogUtils.info("Get text of element " + by);
        String text = getWebElement(by).getText();
        LogUtils.info("==> Text: " + text);
        AllureManager.saveTextLog("==> Text: " + text);
        return text; //Trả về một giá trị kiểu String
    }

    public static String getElementAttribute(By by, String attributeName) {
        waitForElementVisible(by);
        LogUtils.info("Get attribute of element " + by);
        String value = getWebElement(by).getAttribute(attributeName);
        LogUtils.info("==> Attribute vlaue: " + value);
        AllureManager.saveTextLog("==> Attribute vlaue: " + value);
        return value;
    }

    public static void clickElement(By by) {
        waitForElementClickable(by);
        sleep(STEP_TIME);
        getWebElement(by).click();
        LogUtils.info("Click on element " + by);
    }

    public static String getBorderColor(By by) {
        return Color.fromString(DriverManager.getDriver().findElement(by).getCssValue("border-color")).asHex();
    }

    public static void assertNotContain(String actual, String expected, String message) {
        waitForPageLoaded();
        LogUtils.info("Assert NOT contain: " + expected + " and Actual: " + actual);
        boolean check = actual.contains(expected);
        Assert.assertFalse(check, message);
    }

    public static void assertContain(String actual, String expected, String message) {
        waitForPageLoaded();
        LogUtils.info("Assert contain: " + expected + " and Actual: " + actual);
        boolean check = actual.contains(expected);
        Assert.assertTrue(check, message);
    }

    @Step("Assert equals")
    public static void assertEquals(String actual, String expected, String message) {
        LogUtils.info("Assert equals | actual=" + actual + " | expected=" + expected);
        Assert.assertEquals(actual, expected, message);
    }

    @Deprecated
    public static void assertEqual(String actual, String expected, String message) {
        assertEquals(actual, expected, message);
    }

//    @Step("Assert equals: ")
//    public static void assertEqual(int actual, int expected, String message){
//        waitForPageLoaded();
//        LogUtils.info("Assert equals: " + actual + " \uD83D\uDFF0 " + expected);
//        Assert.assertEquals(actual, expected, message);
//    }

    public static void assertEquals(int actual, int expected, String message) {
        LogUtils.info("Assert equals | " + message);
        try {
            Assert.assertEquals(actual, expected, message);
            LogUtils.info("PASSED: " + message);
            Allure.step("Verify Equals: " + message + " | Actual: " + actual + " | Expected: " + expected);
        } catch (AssertionError e) {
            LogUtils.error("FAILED: " + message);
            AllureManager.saveFailureLog(message, String.valueOf(actual), String.valueOf(expected));
            throw e;
        }
    }

    @Deprecated
    public static void assertEqual(int actual, int expected, String message) {
        assertEquals(actual, expected, message);
    }

    @Step("Assert NOT equals: ")
    public static void assertNotEqual(String actual, String expected, String message) {
//        waitForPageLoaded();
        LogUtils.info("Assert not equals: " + actual + " #\uFE0F⃣ " + expected);
        Assert.assertNotEquals(actual, expected, message);
    }

    //    @Step("Check True: {1}")
    public static void assertTrue(boolean condition, String message) {
        LogUtils.info("Assert TRUE | " + message);

        try {
            Assert.assertTrue(condition, message);
            LogUtils.info("PASSED: " + message);
        } catch (AssertionError e) {
            LogUtils.error("FAILED: " + message);
            AllureManager.saveFailureLog(message, "FALSE", "TRUE");
            throw e; // Ném lỗi để TestNG đánh dấu Fail
        }
    }

    public static void assertFalse(boolean condition, String message) {
        LogUtils.info("Assert FALSE | " + message);

        try {
            Assert.assertFalse(condition, message);
            LogUtils.info("PASSED: " + message);
        } catch (AssertionError e) {
            LogUtils.error("FAILED: " + message);
            // Expected là FALSE, nhưng Actual lại ra TRUE nên mới lỗi
            AllureManager.saveFailureLog(message, "TRUE", "FALSE");
            throw e;
        }
    }

    public static void assertNotNull(Object object, String message) {
        LogUtils.info("Assert NOT NULL | " + message);

        try {
            Assert.assertNotNull(object, message);
            LogUtils.info("PASSED: " + message);
        } catch (AssertionError e) {
            LogUtils.error("FAILED: " + message);
            AllureManager.saveFailureLog(message, "NULL", "NOT NULL");
            throw e;
        }
    }

    public static String getCurrentUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }

    public static boolean verifyUrlContains(String keyword) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.urlContains(keyword));
        } catch (TimeoutException e) {
            LogUtils.error("URL does not contain keyword: " + keyword);
            return false;
        }
    }

    public static void scrollToElement(By by) {
        // Lấy element từ By
        WebElement element = DriverManager.getDriver().findElement(by);

        // Dùng Javascript để scroll
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", element);

        // (Tùy chọn) Thêm sleep 1 xíu để trang ổn định sau khi scroll
        sleep(1);
    }

    @Step("Upload file successfully: {1}")
    public static void uploadFileWithRobot_macOS(String filePath) throws AWTException, IOException {
        try {
            LogUtils.info("Upload file (macOS). Path: " + filePath);

            // --- Click để mở hộp thoại Upload ---
//            WebUI.clickElement(uploadButton);
            LogUtils.info("Clicked upload button. Waiting for Finder...");
            WebUI.sleep(2);

            // --- Copy file path vào clipboard ---
            StringSelection selection = new StringSelection(filePath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            LogUtils.info("Copied file path to clipboard.");

            // --- Khởi tạo Robot ---
            Robot rb = new Robot();
            rb.setAutoDelay(300);

            rb.keyPress(KeyEvent.VK_META);
            rb.keyPress(KeyEvent.VK_TAB);
            rb.keyRelease(KeyEvent.VK_META);
            rb.keyRelease(KeyEvent.VK_TAB);
            rb.delay(500);


            // --- Mở hộp thoại 'Go to Folder' ---
            rb.keyPress(KeyEvent.VK_META);
            rb.keyPress(KeyEvent.VK_SHIFT);
            rb.keyPress(KeyEvent.VK_G);
            rb.delay(500);
            rb.keyRelease(KeyEvent.VK_G);
            rb.keyRelease(KeyEvent.VK_SHIFT);
            rb.keyRelease(KeyEvent.VK_META);
            rb.delay(500);
            LogUtils.info("Opened 'Go to Folder' (Cmd+Shift+G).");

            // --- Dán đường dẫn ---
            rb.delay(500);
            rb.keyPress(KeyEvent.VK_META);
            rb.keyPress(KeyEvent.VK_V);
            rb.keyRelease(KeyEvent.VK_V);
            rb.keyRelease(KeyEvent.VK_META);
            rb.delay(500);
            LogUtils.info("Pasted path to dialog.");

            // --- Nhấn Enter để truy cập đường dẫn ---
            rb.keyPress(KeyEvent.VK_ENTER);
            rb.keyRelease(KeyEvent.VK_ENTER);
            rb.delay(500);
            LogUtils.info("Confirmed path.");

            // --- Nhấn Enter lần 2 để chọn file ---
            rb.keyPress(KeyEvent.VK_ENTER);
            rb.keyRelease(KeyEvent.VK_ENTER);
            rb.delay(500);
            LogUtils.info("Selected file for upload.");

            rb.keyPress(KeyEvent.VK_ENTER);
            rb.keyRelease(KeyEvent.VK_ENTER);
            rb.delay(500);

        } catch (Exception e) {
            LogUtils.error("Upload file (macOS) failed: " + e.getMessage());
        }
        AllureManager.saveTextLog("==> File path: " + filePath);
    }
}
