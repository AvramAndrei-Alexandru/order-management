package com.business;

import com.data_access.OrderItemDAO;
import com.data_access.ProductsDAO;
import com.model.OrderItem;
import com.model.Products;
import java.util.List;

/**
 * Provides the means for insert, delete and update stock operations to be performed on products.
 */

class ProductOperations {
    /**
     * Used for accessing the database to make queries related to products
     */
    private ProductsDAO productsDAO = new ProductsDAO();

    /**
     * Inserts into the database a product having the data the same as th parameters.
     * If no product with the name equal with productName is found in the database it will add a new product with the corresponding data given as parameters.
     * If a product is found, its price and quantity gets updated with the new price and added quantity.
     * @param productName The name of the product that will be inserted
     * @param price The price of the product that will be inserted
     * @param quantity The quantity of the product that will be inserted
     */
    void insertProduct(String productName, double price, double quantity) {
        Products foundProducts = productsDAO.findByName(productName);
        if(foundProducts == null)
            productsDAO.insertOrDelete(new Products(1, productName, price, quantity), true);
        else {
            quantity += foundProducts.getQuantity();
            foundProducts.setQuantity(quantity);
            foundProducts.setPrice(price);
            productsDAO.update(foundProducts);
        }
    }

    /**
     * Deletes from the database the product having the name equal with productName.
     * If no product with that name is found the method returns.
     * @param productName The name of the product to be deleted.
     */
    void deleteProduct(String productName) {
        Products foundProducts = productsDAO.findByName(productName);
        if(foundProducts == null) {
            System.out.println("No product with that name was found.");
            return;
        }
        productsDAO.deleteByName(productName);
    }

    /**
     * Updates the stock for the items ordered by the customers.
     * For every product that is found in the orderItem table the corresponding product that exists in the product table gets its quantity decreased
     * with the amount found in the orderItem table.
     */
    void updateStock() {
        List<OrderItem> items = new OrderItemDAO().findAll();
        Products currentProduct;
        for(OrderItem currentItem : items) {
             currentProduct = productsDAO.findById(currentItem.getProductID());
             currentProduct.setQuantity(currentProduct.getQuantity() - currentItem.getProductQuantity());
             productsDAO.update(currentProduct);
        }
    }
}
