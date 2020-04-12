package com.data_access;

import com.model.OrderItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates and executes queries that are specific to orderItems.
 */
public class OrderItemDAO extends AbstractDAO<OrderItem> {
    private static final Logger LOGGER = Logger.getLogger(OrderItem.class.getName());
    @Override
    public List<OrderItem> findAll() {
        return super.findAll();
    }

    @Override
    public OrderItem findById(int id) {
        return super.findById(id);
    }

    @Override
    public void insertOrDelete(OrderItem orderItem, boolean insertMode) {
        super.insertOrDelete(orderItem, insertMode);
    }

    @Override
    public void update(OrderItem orderItem) {
        super.update(orderItem);
    }

    /**
     * Erases all the data that is inside the orderItem table.
     */
    public void deleteAllData() {
        List<OrderItem> allItemsList = findAll();
        if(allItemsList.size() != 0) {
            for(OrderItem orderItem : allItemsList) {
                insertOrDelete(orderItem, false);
            }
        }
    }

    /**
     * Creates a SQL SELECT on id query.
     * @param id The id of the product.
     * @return A SELECT SQL query.
     */
    private String createFindByID(int id) {
        return "SELECT * FROM orderItem WHERE productID = " + id;
    }

    /**
     * Searches for an orderItem that has the product id equal with the parameter.
     * @param productID The product id.
     * @return Returns the orderItem object that has the corresponding product id.
     */
    public OrderItem findByProductID (int productID){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createFindByID(productID);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if(!resultSet.next()) {
                return null;
            }
            return new OrderItem(resultSet.getInt("id"), resultSet.getInt("productID"), resultSet.getDouble("productQuantity"));
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,   "OrderItem:findByProductID " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

}
