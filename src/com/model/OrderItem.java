package com.model;
/**
 * This class is used to hold records from the orderItem table.
 */
public class OrderItem {
    /**
     * The orderItem id.
     */
    private int id;
    /**
     * The product id.
     */
    private int productID;
    /**
     * The product quantity.
     */
    private double productQuantity;

    public OrderItem(int id, int productID, double productQuantity) {
        this.id = id;
        this.productID = productID;
        this.productQuantity = productQuantity;
    }

    public OrderItem() {
    }

    public int getId() {
        return id;
    }

    public int getProductID() {
        return productID;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductQuantity(double productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productID=" + productID +
                ", productQuantity=" + productQuantity +
                '}';
    }

}
