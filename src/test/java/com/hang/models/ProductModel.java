package com.hang.models;

public class ProductModel {
    private String productName;
    private int quantity; //Input: Số lượng cần mua
    private double recordedPrice; //Output: Giá thực tế lấy từ Web

    public ProductModel(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getRecordedPrice() {
        return recordedPrice;
    }

    //Tự động điền giá vào sau khi lấy từ Web
    public void setRecordedPrice(double recordedPrice) {
        this.recordedPrice = recordedPrice;
    }
}
