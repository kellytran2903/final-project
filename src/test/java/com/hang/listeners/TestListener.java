package com.hang.listeners;

import com.core.helpers.CaptureHelper;
import com.core.helpers.PropertiesHelper;
import com.core.reports.AllureManager;
import com.core.utils.LogUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static int test_total;
    private static int test_passed_total;
    private static int test_failed_total;
    private static int test_skipped_total;

    public String getTestName(ITestResult result) {
        return result.getTestName() != null ? result.getTestName() : result.getMethod().getConstructorOrMethod().getName();
    }

    public String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
    }


    @Override
    public void onStart(ITestContext result) {
        LogUtils.info("Setup môi trường onStart: " + result.getStartDate());
        PropertiesHelper.loadAllFiles();
    }

    @Override
    public void onFinish(ITestContext result) {
        LogUtils.info("Kết thúc bộ test: " + result.getEndDate());
        LogUtils.info("Test total: " + test_total);
        LogUtils.info("Test PASSED total: " + test_passed_total);
        LogUtils.info("Test FAILED total: " + test_failed_total);
        LogUtils.info("Test SKIPPED total: " + test_skipped_total);
    }

    @Override
    public void onTestStart(ITestResult result) {
        CaptureHelper.startRecord(result.getName());
        LogUtils.info("Bắt đầu chạy test case: " + result.getName());
        test_total++;
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.info("Đây là test case PASSED: " + result.getName());
        test_passed_total++;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtils.error("Đây là test case FAILED: " + result.getName());
        LogUtils.error("==> Status: " + result.getStatus());
        LogUtils.error(result.getThrowable());
        //Allure Report
        AllureManager.saveScreenshotPNG();
        test_failed_total++;
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.warn("Test case " + result.getName() + " is skipped.");
        LogUtils.warn(result.getThrowable());
        test_skipped_total++;
    }
}