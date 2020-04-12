package com.data_access;

import com.model.Clients;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates and executes queries that are specific to clients.
 */
public class ClientsDAO extends AbstractDAO<Clients> {
    private static final Logger LOGGER = Logger.getLogger(Clients.class.getName());
    @Override
    public List<Clients> findAll() {
        return super.findAll();
    }

    @Override
    public void insertOrDelete(Clients clients, boolean insertMode) {
        super.insertOrDelete(clients, insertMode);
    }

    @Override
    public void update(Clients clients) {
        super.update(clients);
    }

    @Override
    public Clients findById(int id) {
        return super.findById(id);
    }

    /**
     * Creates an SQL SELECT query that searches for a client with a given first name and last name.
     * @param firstName The first name of the client.
     * @param lastName The last name of the client.
     * @return SQL SELECT query that searches on first name and last name.
     */

    private String createSearchNameQuery(String firstName, String lastName) {
        return "SELECT * FROM clients WHERE firstName = '" + firstName + "' AND lastName = '" + lastName + "'";
    }

    /**
     * Searches for a client that has the name the same as th parameters.
     * @param firstName The first name of the client.
     * @param lastName The last name of the client.
     * @return The client from the table that has the corresponding first name and last name.
     */
    public Clients getClientByFullName(String firstName, String lastName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSearchNameQuery(firstName, lastName);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if(!resultSet.next()) {
               return null;
            }
            return new Clients(resultSet.getInt("id"), resultSet.getString("firstName"), resultSet.getString("lastName"), resultSet.getString("address"));
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
     * Deletes the client with the corresponding name from the database.
     * @param firstName The first name of the client.
     * @param lastName The last name of the client.
     */
    public void deleteClientByFullName(String firstName, String lastName) {
        Clients foundClient = getClientByFullName(firstName, lastName);
        if(foundClient != null) {
            insertOrDelete(foundClient, false);
        }
    }

    /**
     * Erases all the data that is found inside the table.
     */
    public void deleteAllData() {
        List<Clients> allItemsList = findAll();
        if(allItemsList.size() != 0) {
            for(Clients clients : allItemsList) {
                insertOrDelete(clients, false);
            }
        }
    }
}
