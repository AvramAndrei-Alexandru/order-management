package com.data_access;

import com.model.Products;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Creates and executes queries that are specific to products.
 */
public class ProductsDAO extends AbstractDAO<Products> {
    private static final Logger LOGGER = Logger.getLogger(Products.class.getName());
    @Override
    public List<Products> findAll() {
        return super.findAll();
    }

    @Override
    public Products findById(int id) {
        return super.findById(id);
    }

    @Override
    public void insertOrDelete(Products products, boolean insertMode) {
        super.insertOrDelete(products, insertMode);
    }

    @Override
    public void update(Products products) {
        super.update(products);
    }
    /**
     * Erases all the data that is inside the products table.
     */
    public void deleteAllData() {
        List<Products> allItemsList = findAll();
        if(allItemsList.size() != 0) {
            for(Products products : allItemsList) {
                insertOrDelete(products, false);
            }
        }
    }

    /**
     * Creates a SQL SELECT on product name query.
     * @param productName The product name.
     * @return A SQL SELECT on product name query.
     */
    private String createSearchNameQuery(String productName) {
        return "SELECT * FROM products WHERE productName = '" + productName + "'";
    }

    /**
     * Searches the table for a product having the name the same as the productName parameter.
     * @param productName The product name.
     * @return The product having the name the same as the productName parameter.
     */
    public Products findByName(String productName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSearchNameQuery(productName);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if(!resultSet.next()) {
                return null;
            }
            return new Products(resultSet.getInt("id"), resultSet.getString("productName"), resultSet.getDouble("price"), resultSet.getDouble("quantity"));
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,   "Clients:findByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Deletes a given product from the database.
     * @param productName The product name.
     */
    public void deleteByName(String productName) {
        Products product = findByName(productName);
        if(product != null) {
            insertOrDelete(product, false);
        }
    }

}
