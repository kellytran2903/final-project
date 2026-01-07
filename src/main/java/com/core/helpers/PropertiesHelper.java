package com.core.helpers;

import io.qameta.allure.Step;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

public class PropertiesHelper {

    private static Properties properties;
    private static String linkFile;
    private static FileInputStream file;
    private static FileOutputStream out;
    private static String relPropertiesFilePathDefault = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "configs" + File.separator + "config.properties"; //File.separator → Tự động thêm ký tự phân tách thư mục phù hợp với hệ điều hành (/ trên Linux/macOS, \ trên Windows).

    // [loadAllFiles] dùng để đọc files
    public static Properties loadAllFiles() {
        LinkedList<String> files = new LinkedList<>();
        // Add tất cả file Properties vào đây theo mẫu
        files.add("src/test/resources/configs/config.properties");

        try {
            properties = new Properties();

            for (String f : files) {
                Properties tempProp = new Properties();
                linkFile = SystemHelper.getCurrentDir() + f;
                file = new FileInputStream(linkFile);
                tempProp.load(file);
                properties.putAll(tempProp);
            }
            return properties;
        } catch (IOException ioe) {
            return new Properties();
        }
    }

    public static void setDefaultFile() {
        properties = new Properties();
        try {
            linkFile = SystemHelper.getCurrentDir() + relPropertiesFilePathDefault;
            file = new FileInputStream(linkFile);
            properties.load(file);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        String value = null;
        try {
            if (file == null) {
                setDefaultFile();
            }
            // Lấy giá trị từ file đã Set
            value = properties.getProperty(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return value;
    }

    public static void setValue(String key, String keyValue) {
        try {
            if (file == null) {
                setDefaultFile();
                out = new FileOutputStream(SystemHelper.getCurrentDir() + relPropertiesFilePathDefault);
            }
            //Ghi vào cùng file Prop với file lấy ra
            out = new FileOutputStream(linkFile);
            System.out.println(linkFile);
            properties.setProperty(key, keyValue);
            properties.store(out, null);
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setValue(String key, String keyValue, String filePath) {
        try {
            if (file == null) {
                setDefaultFile();
                out = new FileOutputStream(SystemHelper.getCurrentDir() +  filePath);
            }
            //Ghi vào cùng file Prop với file lấy ra
            out = new FileOutputStream(SystemHelper.getCurrentDir() +  filePath);
            System.out.println(SystemHelper.getCurrentDir() +  filePath);
            properties.setProperty(key, keyValue);
            properties.store(out, null);
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //HÀM DÙNG RIÊNG CHO VIỆC LƯU DATA TEST
    //Hàm đọc giá trị từ một file cụ thể
    @Step("Get data from Properties file:")
    public static String getData(String key, String relativeFilePath){
        Properties tempProp = new Properties();
        String value = null;
        String fullPath = SystemHelper.getCurrentDir() + relativeFilePath;

        try (FileInputStream fileIn = new FileInputStream(fullPath)){
            tempProp.load(fileIn);
            value = tempProp.getProperty(key);
        } catch (Exception e) {
            System.out.println("Không đọc được file data: " + fullPath);
        }
        return value;
    }

    //Hàm ghi giá trị vào file
    @Step("Save data into Properties file with: key '{0}' - value '{1}'")
    public static void saveData(String key, String value, String relativeFilePath){
        Properties tempProp = new Properties();
        String fullPath = SystemHelper.getCurrentDir() + relativeFilePath;
        File f = new File(fullPath);

        // 1. Nếu file đã tồn tại, load dữ liệu cũ lên trước
        if (f.exists()){
            try (FileInputStream fileIn = new FileInputStream(fullPath)){
                tempProp.load(fileIn);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        // 2. Put dữ liệu mới vào
        tempProp.setProperty(key, value);

        // 3. Lưu xuống file (Nếu file chưa có sẽ tự tạo)
        try (FileOutputStream fileOut = new FileOutputStream(fullPath)){
            tempProp.store(fileOut, "Temp Data for Automation Test");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

