package com.business;

import com.data_access.ClientsDAO;
import com.data_access.OrdersDAO;
import com.data_access.ProductsDAO;
import com.model.Clients;
import com.model.Orders;
import com.model.Products;
import com.presentation.ClientReportGenerator;
import com.presentation.OrderReportGenerator;
import com.presentation.ProductReportGenerator;
import java.util.List;

/**
 * Used to validate the operations that can be done using this application.
 */
public class Validator {
    /**
     * Used for accessing the database to make queries related to clients
     */
    private ClientsDAO clientsDAO = new ClientsDAO();
    /**
     * Used for accessing the database to make queries related to products
     */
    private ProductsDAO productsDAO = new ProductsDAO();
    /**
     * Used for accessing the database to make queries related to orders
     */
    private OrdersDAO ordersDAO = new OrdersDAO();

    /**
     * Validates the report client operation. The report will be generated only if at least one client is found in the database.
     */
    public void validateClientReport() {
        List<Clients> foundClients = clientsDAO.findAll();
        if(foundClients == null) {
            System.out.println("Error at getting the clients");
            return;
        }
        if(foundClients.size() != 0) {
            ClientReportGenerator clientReportGenerator = new ClientReportGenerator(foundClients);
            clientReportGenerator.generateReport();
        }
    }
    /**
     * Validates the report product operation. The report will be generated only if at least one product is found in the database.
     */
    public void validateProductReport() {
        List<Products> foundProducts = productsDAO.findAll();
        if(foundProducts == null) {
            System.out.println("Error at getting the products");
            return;
        }
        if(foundProducts.size() != 0) {
            ProductReportGenerator productReportGenerator = new ProductReportGenerator(foundProducts);
            productReportGenerator.generateReport();
        }
    }
    /**
     * Validates the report orders operation. The report will be generated only if at least one client and one order is found.
     */
    public void validateOrderReport() {
        List<Orders> foundOrders = ordersDAO.findAll();
        List<Clients> foundClients = clientsDAO.findAll();
        if(foundOrders == null || foundClients == null) {
            System.out.println("Error at getting the orders");
            return;
        }
        if(foundOrders.size() != 0) {
            new ProductOperations().updateStock();
            new OrderOperations().eraseData();
            OrderReportGenerator orderReportGenerator = new OrderReportGenerator(foundOrders, foundClients);
            orderReportGenerator.generateReport();
        }
    }

    /**
     * Validates the delete or insert client operations. If the command string corresponds to the valid format, the method calls the insert or delete operations found in
     * the ClientOperations class.
     * @param command The command to insert or delete a client.
     * @param insert If true insert will be performed, if false delete will be performed.
     */
    public void validateInsertOrDeleteClient(String command, boolean insert) {
        command = command.replace(",", "");
        String[] split = command.split(" ");
        if(split.length != 5 || !validateString(split[2]) || !validateString(split[3]) || !validateString(split[4])) {
            if(insert)
                System.out.println("Invalid insert client command");
            else
                System.out.println("Invalid delete client command");
            return;
        }
        ClientOperations clientOperations = new ClientOperations();
        if(insert)
            clientOperations.insertClient(split[2], split[3], split[4]);
        else
            clientOperations.deleteClient(split[2], split[3], split[4]);
    }

    /**
     * Validates the insert product operation. If the command string corresponds to the valid format, the method calls the insert operation found in
     * the ProductOperations class.
     * @param command The command to insert a product.
     */
    public void validateInsertProduct(String command) {
        command = command.replace(",", "");
        String[] split = command.split(" ");
        if(split.length != 5 || !validateString(split[2]) || !validateDouble(split[3]) || !validateDouble(split[4])) {
            System.out.println("Invalid insert product command");
            return;
        }
        ProductOperations productOperations = new ProductOperations();
        productOperations.insertProduct(split[2], Double.parseDouble(split[4]), Double.parseDouble(split[3]));
    }
    /**
     * Validates the delete product operation. If the command string corresponds to the valid format, the method calls the delete operation found in
     * the ProductOperations class.
     * @param command The command to delete a product.
     */
    public void validateDeleteProduct(String command) {
        String[] split = command.split(" ");
        if(split.length != 3 || !validateString(split[2])) {
            System.out.println("Invalid delete product command");
            return;
        }
        ProductOperations productOperations = new ProductOperations();
        productOperations.deleteProduct(split[2]);
    }
    /**
     * Validates the place order operation. If the command string corresponds to the valid format, the method calls the place order operation found in
     * the OrderOperations class.
     * @param command The command to place an order.
     */
    public void validateOrder(String command) {
        command = command.replace(",", "");
        String[] split = command.split(" ");
        if(split.length != 5 || !validateString(split[1]) || !validateString(split[2]) || !validateString(split[3]) || !validateDouble(split[4])) {
            System.out.println("Invalid order command");
            return;
        }
        OrderOperations orderOperations = new OrderOperations();
        orderOperations.placeOrder(split[1], split[2], split[3], Double.parseDouble(split[4]));
    }

    /**
     * Checks if a string is correct. To be correct it must have less than 45 characters and to contain only letters.
     * @param validate The string that must be validated.
     * @return Returns true if the string format is correct, otherwise false.
     */
    private boolean validateString(String validate) {
        if(validate.length() > 45)
            return false;
        for(char c : validate.toCharArray()) {
            if(Character.isDigit(c))
                return false;
        }
        return true;
    }

    /**
     * Checks if a string contains a double.
     * @param validate The string that must be validated.
     * @return Returns true if the string contains a double, otherwise false
     */
    private boolean validateDouble(String validate) {
        try {
            Double.parseDouble(validate);
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
