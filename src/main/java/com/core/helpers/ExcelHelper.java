package com.core.helpers;

import com.core.utils.LogUtils;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook wb;
    private Sheet sh;
    private Cell cell;
    private Row row;
    private CellStyle cellstyle;
    private Color mycolor;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();

    public void setExcelFile(String ExcelPath, String SheetName){
        try {
            File f = new File(ExcelPath);

            if (!f.exists()) {
                LogUtils.warn("Excel file does not exist: " + ExcelPath);
            }

            fis = new FileInputStream(ExcelPath);
            wb = WorkbookFactory.create(fis);
            sh = wb.getSheet(SheetName);

            if (sh == null) {
                throw new Exception("Sheet name doesn't exist.");
            }

            this.excelFilePath = ExcelPath;

            //adding all the column header names to the map 'columns'
            sh.getRow(0).forEach(cell ->{
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });

        } catch (Exception e) {
            LogUtils.error("Failed to open Excel file. Path=" + ExcelPath + " | sheet=" + SheetName + " | error=" + e.getMessage());
        }
    }

    public String getCellData(int columnIndex, int rowIndex) {
        try {
            cell = sh.getRow(rowIndex).getCell(columnIndex);
            String CellData = null;
            switch (cell.getCellType()) {
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        //Dùng DataFormatter để lấy đúng định dạng hiển thị trên Excel (không ép kiểu về long)
//                        CellData = String.valueOf((long) cell.getNumericCellValue());
                        DataFormatter formatter = new DataFormatter();
                        CellData = formatter.formatCellValue(cell);
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    public String getCellData(String columnName, int rowIndex) {
        return getCellData(columns.get(columnName), rowIndex);
    }

    //set by column index
    public void setCellData(String text, int columnIndex, int rowIndex) {
        try {
            row = sh.getRow(rowIndex);
            if (row == null) {
                row = sh.createRow(rowIndex);
            }
            cell = row.getCell(columnIndex);

            if (cell == null) {
                cell = row.createCell(columnIndex);
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            style.setFillPattern(FillPatternType.NO_FILL);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //set by column name
    public void setCellData(String text, String columnName, int rowIndex) {
        try {
            row = sh.getRow(rowIndex);
            if (row == null) {
                row = sh.createRow(rowIndex);
            }
            cell = row.getCell(columns.get(columnName));

            if (cell == null) {
                cell = row.createCell(columns.get(columnName));
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            style.setFillPattern(FillPatternType.NO_FILL);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //Đọc data từ Excel file với DataProvider
    public Object[][] getExcelDataProvider(String filePath, String sheetName) {
        Object[][] data = null;
        Workbook workbook = null;
        try {
            // load the file
            FileInputStream fis = new FileInputStream(filePath);

            // load the workbook
            workbook = new XSSFWorkbook(fis);

            // load the sheet
            Sheet sh = workbook.getSheet(sheetName);

            // load the row
            Row row = sh.getRow(0);

            //
            int noOfRows = sh.getPhysicalNumberOfRows();
            int noOfCols = row.getLastCellNum();
            LogUtils.info("Excel data provider. rows=" + noOfRows + " | cols=" + noOfCols);

            Cell cell;
            data = new Object[noOfRows - 1][noOfCols];

            //
            for (int i = 1; i < noOfRows; i++) {
                for (int j = 0; j < noOfCols; j++) {
                    row = sh.getRow(i);
                    cell = row.getCell(j);

                    switch (cell.getCellType()) {
                        case STRING:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            data[i - 1][j] = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BLANK:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;
                        default:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.error("Excel data provider failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return data;
    }

//    public  void saveRowData(String[] dataRow){
//     try{
//         //1. Kiểm tra sheet đã được khởi tạo chưa (phải gọi setExcelFile trước)
//         if (sh == null) {
//             System.out.println("Sheet chưa được khởi tạo, hãy gọi setExcelFile trước!");
//             return;
//         }
//
//         //2. Xác định index của dòng mới (Dòng cuối cùng +1)
//         int lastRowNum = sh.getLastRowNum();
//         //Note: getLastRow trả về 0 nếu chỉ có Header hoặc sheet trống
//         if (sh.getPhysicalNumberOfRows() > 0){
//             lastRowNum++;
//         }
//
//         //3. Tạo dòng mới
//         Row row = sh.createRow(lastRowNum);
//
//         //4. Duyệt mảng dât và ghi vào từng cột
//         for (int i=0; i<dataRow.length; i++){
//             Cell cell = row.createCell(i);
//             cell.setCellValue(dataRow[i]);
//
//             // Style căn giữa (Tùy chọn)
//             XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
//             style.setAlignment(HorizontalAlignment.LEFT); // Căn trái cho text
//             style.setVerticalAlignment(VerticalAlignment.CENTER);
//             cell.setCellStyle(style);
//         }
//
//         // 5. Lưu file
//         fileOut = new FileOutputStream(excelFilePath);
//         wb.write(fileOut);
//         fileOut.flush();
//         fileOut.close();
//
//         System.out.println("Đã ghi thêm dòng dữ liệu tại row: " + lastRowNum);
//     } catch (Exception e) {
//         System.out.println("Lỗi ghi file: " + e.getMessage());
//     }
//    }

    // Hàm ghi nhiều cột trên 1 dòng mới (Dùng cho ProductPage)
    public void saveRowData(String[] dataRow) {
        try {
            if (sh == null) {
                LogUtils.warn("Sheet is not initialized. Call setExcelFile() first.");
                return;
            }

            // 1. Tạo Style chung (Tạo 1 lần dùng cho cả dòng)
            CellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.LEFT);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setWrapText(true); // Tự động xuống dòng nếu text dài

            // 2. Xác định dòng cuối cùng để ghi tiếp
            int rowCount = sh.getLastRowNum();
            // Nếu sheet có data thì ghi vào dòng tiếp theo, nếu chưa có thì ghi vào dòng 0 (hoặc 1 tùy logic)
            // Logic an toàn: Luôn +1 so với dòng cuối cùng tìm thấy
            int newRowIndex = rowCount + 1;

            // 3. Tạo dòng mới
            Row row = sh.createRow(newRowIndex);

            // 4. Duyệt mảng data và ghi vào từng cột
            for (int i = 0; i < dataRow.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(dataRow[i]);
                cell.setCellStyle(style); // Áp dụng style đã tạo
            }

            // 5. Auto resize cột cho đẹp (Optional - có thể làm chậm nếu file lớn)
            for (int i = 0; i < dataRow.length; i++) {
                sh.autoSizeColumn(i);
            }

            // 6. Lưu file
            fileOut = new FileOutputStream(excelFilePath);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();

            LogUtils.info("Wrote Excel row. RowIndex=" + newRowIndex);

        } catch (Exception e) {
            LogUtils.error("Failed to write Excel row: " + e.getMessage());
        }
    }

    // Để Test Case biết vòng lặp chạy bao nhiêu lần
    public int getRowCount() {
        if (sh == null) {
            LogUtils.warn("Sheet is not initialized. Call setExcelFile() first.");
            return 0;
        }
        return sh.getLastRowNum();
    }
}