package org.example.model;

public class Order {
    private String orderId;
    private String buyerName;
    private String product;
    private double price;
    private String date;
    private String status;

    public Order() {}

    public Order(String orderId, String buyerName, String product, double price, String date, String status) {
        this.orderId = orderId;
        this.buyerName = buyerName;
        this.product = product;
        this.price = price;
        this.date = date;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
