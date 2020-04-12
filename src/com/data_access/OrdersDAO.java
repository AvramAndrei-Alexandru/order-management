package com.data_access;


import com.model.Orders;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates and executes queries that are specific to orders.
 */
public class OrdersDAO extends AbstractDAO<Orders> {
    private static final Logger LOGGER = Logger.getLogger(Orders.class.getName());
    @Override
    public List<Orders> findAll() {
        return super.findAll();
    }

    @Override
    public Orders findById(int id) {
        return super.findById(id);
    }

    @Override
    public void insertOrDelete(Orders orders, boolean insertMode) {
        super.insertOrDelete(orders, insertMode);
    }

    @Override
    public void update(Orders orders) {
        super.update(orders);
    }
    /**
     * Erases all the data that is inside the orders table.
     */
    public void deleteAllData() {
        List<Orders> allItemsList = findAll();
        if(allItemsList.size() != 0) {
            for(Orders orders : allItemsList) {
                insertOrDelete(orders, false);
            }
        }
    }

    /**
     * Creates a SQL SELECT on client id and product name query.
     * @param clientID The client id.
     * @param productName The product name
     * @return A SQL SELECT on client id and product name query.
     */
    private String createQuery(int clientID, String productName) {
        return "SELECT * FROM orders WHERE clientID = " + clientID + " AND productName = '" + productName + "'";
    }

    /**
     * Searches the table for an order that has the client id and product name the same as the parameters.
     * @param clientID The client id.
     * @param productName The product name.
     * @return The order having the client id and product name the same as the parameters.
     */
    public Orders findByClientIDAndProductName (int clientID, String productName){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createQuery(clientID, productName);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if(!resultSet.next()) {
                return null;
            }
           return new Orders(resultSet.getInt("id"), resultSet.getInt("clientID"), resultSet.getDouble("totalPrice"), resultSet.getString("productName"), resultSet.getInt("ok"));
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,   "Orders:findByClientID " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}
