package com.hang.dataproviders;

import com.core.helpers.ExcelHelper;
import com.core.helpers.SystemHelper;
import lombok.experimental.Helper;
import org.testng.annotations.DataProvider;

public class DataProvidersProfile {
    @DataProvider(name = "data_provider_profile_excel")
    public Object[][] dataProfileFromExcel() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] data = excelHelper.getExcelDataProvider(SystemHelper.getCurrentDir() + "src/test/resources/datatest/Auto TC.xlsx", "Edit Profile");
        System.out.println("Edit data from Excel: " + data);
        return data;
    }
}
