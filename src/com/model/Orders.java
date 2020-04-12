package com.model;
/**
 * This class is used to hold records from the orders table.
 */
public class Orders {
    /**
     * The order id.
     */
    private int id;
    /**
     * The client id.
     */
    private int clientID;
    /**
     * Total price of the order.
     */
    private double totalPrice;
    /**
     * The product name.
     */
    private String productName;
    /**
     * A flag that signals if the order can be completed or not.
     */
    private int ok;

    public Orders(int id, int clientID, double totalPrice, String productName, int ok) {
        this.id = id;
        this.clientID = clientID;
        this.totalPrice = totalPrice;
        this.productName = productName;
        this.ok = ok;
    }

    public Orders() {

    }

    public int getId() {
        return id;
    }

    public int getClientID() {
        return clientID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getOk() {
        return ok;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", clientID=" + clientID +
                ", totalPrice=" + totalPrice +
                ", productName='" + productName + '\'' +
                ", ok=" + ok +
                '}';
    }
}
