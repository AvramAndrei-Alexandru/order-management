package com.business;

import com.data_access.ClientsDAO;
import com.data_access.OrderItemDAO;
import com.data_access.OrdersDAO;
import com.data_access.ProductsDAO;
import com.model.Clients;
import com.model.OrderItem;
import com.model.Orders;
import com.model.Products;

/**
 * Provides the means for place order and erase orders and order item operations to be made
 */
public class OrderOperations {
    /**
     * Used for accessing the database to make queries related to clients
     */
    private ClientsDAO clientsDAO = new ClientsDAO();
    /**
     * Used for accessing the database to make queries related to products
     */
    private ProductsDAO productsDAO = new ProductsDAO();
    /**
     * Used for accessing the database to make queries related to order items
     */
    private OrderItemDAO orderItemDAO = new OrderItemDAO();
    /**
     * Used for accessing the database to make queries related to orders
     */
    private OrdersDAO ordersDAO = new OrdersDAO();

    /**
     * Checks if the client that places the order and the product being ordered exists in the database.
     * Also it checks if an order for that client exists already in the database.
     * @param firstName the first name of the client that is placing the order
     * @param lastName the last name of the client that is placing the order
     * @param productName the product that is being ordered
     * @param productQuantity the quantity of the product ordered
     */
    void placeOrder(String firstName, String lastName, String productName, double productQuantity) {
        Clients foundClient = clientsDAO.getClientByFullName(firstName, lastName);
        Products foundProduct = productsDAO.findByName(productName);
        if(foundClient == null) {
            System.out.println("No client with that name was found in the database. The order will not be placed");
            return;
        }
        if(foundProduct == null) {
            System.out.println("No product with that name was found in the database. The order will not be placed");
            return;
        }
        Orders foundOrder = ordersDAO.findByClientIDAndProductName(foundClient.getId(), foundProduct.getProductName());
        OrderItem foundOrderItem = orderItemDAO.findByProductID(foundProduct.getId());
        if(foundOrder == null)
            orderNotFound(foundOrderItem, foundClient, foundProduct, productQuantity);
        else
            orderFound(foundOrder, foundOrderItem, foundProduct, productQuantity);
    }

    /**
     * Adjusts the order and product stock.
     * @param foundOrder The order that already exists in the database for that client.
     * @param foundOrderItem The product that may have been ordered previously.
     * @param foundProduct The product that is ordered.
     * @param productQuantity The quantity of the product.
     */

    private void orderFound(Orders foundOrder, OrderItem foundOrderItem, Products foundProduct, double productQuantity) {
            if(foundOrderItem.getProductQuantity() + productQuantity > foundProduct.getQuantity()) {
                foundOrder.setOk(0);
                ordersDAO.update(foundOrder);
            }
            else {
                foundOrderItem.setProductQuantity(foundOrderItem.getProductQuantity() + productQuantity);
                foundOrder.setTotalPrice(foundOrder.getTotalPrice() + foundProduct.getPrice() * productQuantity);
                ordersDAO.update(foundOrder);
                orderItemDAO.update(foundOrderItem);
            }
    }

    /**
     * Places the first order for this client. If the product has been ordered before its orderItem gets updated otherwise gets created.
     * @param foundOrderItem The product that may have been ordered previously.
     * @param foundClient Tha client that is placing the order.
     * @param foundProduct The product that is ordered.
     * @param productQuantity The quantity of the product.
     */
    private void orderNotFound(OrderItem foundOrderItem, Clients foundClient, Products foundProduct, double productQuantity) {
        if(foundOrderItem == null) {
            if(foundProduct.getQuantity() < productQuantity) { // The order will not be done
                ordersDAO.insertOrDelete(new Orders(1, foundClient.getId(), 0, foundProduct.getProductName(), 0), true);
            }
            else {
                ordersDAO.insertOrDelete(new Orders(1, foundClient.getId(), productQuantity * foundProduct.getPrice(), foundProduct.getProductName(), 1), true);
                orderItemDAO.insertOrDelete(new OrderItem(1, foundProduct.getId(), productQuantity), true);
            }
        }
        else {
            if(foundOrderItem.getProductQuantity() + productQuantity > foundProduct.getQuantity()) {
                ordersDAO.insertOrDelete(new Orders(1, foundClient.getId(), 0, foundProduct.getProductName(), 0), true);
            }
            else {
                ordersDAO.insertOrDelete(new Orders(1, foundClient.getId(), productQuantity * foundProduct.getPrice(), foundProduct.getProductName(), 1), true);
                foundOrderItem.setProductQuantity(foundOrderItem.getProductQuantity() + productQuantity);
                orderItemDAO.update(foundOrderItem);
            }
        }
    }

    /**
     * Used to delete all the data that is inside the Orders and OrderItem tables
     */
    void eraseData() {
        ordersDAO.deleteAllData();
        orderItemDAO.deleteAllData();
    }
}
