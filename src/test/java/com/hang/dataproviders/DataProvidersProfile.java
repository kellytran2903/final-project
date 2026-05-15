package com.hang.dataproviders;

import com.core.helpers.ExcelHelper;
import com.core.helpers.SystemHelper;
import com.core.utils.LogUtils;
import org.testng.annotations.DataProvider;

public class DataProvidersProfile {
    @DataProvider(name = "data_provider_profile_excel")
    public Object[][] dataProfileFromExcel() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] data = excelHelper.getExcelDataProvider(SystemHelper.getCurrentDir() + "src/test/resources/testdata/auto-tc.xlsx", "Edit Profile");
        LogUtils.info("Loaded profile data from Excel. Rows=" + (data == null ? 0 : data.length));
        return data;
    }
}
