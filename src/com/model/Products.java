package com.model;
/**
 * This class is used to hold records from the products table.
 */
public class Products implements Comparable<Products>{
    /**
     * The product id.
     */
    private int id;
    /**
     * The product name.
     */
    private String productName;
    /**
     * The price of the product.
     */
    private double price;
    /**
     * The quantity of the product.
     */
    private double quantity;

    public Products(int id, String productName, double price, double quantity) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public Products() {
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Products{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    /**
     * Compares two products based on their quantity.
     * @param o The product to compare with this product.
     * @return 0, 1, -1 based on the comparison.
     */
    @Override
    public int compareTo(Products o) {
        return Double.compare(this.getQuantity(), o.getQuantity());
    }
}
