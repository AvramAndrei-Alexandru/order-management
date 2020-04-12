package com.business;

import com.data_access.ClientsDAO;
import com.model.Clients;

/**
 * Provides the means for the insert and delete client operations to be done.
 */
class ClientOperations {
    /**
     * Used for accessing the database to make queries related to clients
     */
    private ClientsDAO clientsDAO = new ClientsDAO();

    /**
     * Inserts into the database the client having tha data the same as the parameters.
     * First it tries to find a client in the database with that values given as parameter. If no client was found insert a new client with that values.
     * If a client was found, the method returns.
     * @param firstName the first name of the client that will be inserted
     * @param lastName the last name of the client that will be inserted
     * @param address the address of the client that will be inserted
     */
    void insertClient(String firstName, String lastName, String address) {
        Clients test = clientsDAO.getClientByFullName(firstName, lastName);
        if(test != null && test.getAddress().equals(address)) {
            System.out.println("The client already exists. It will not be added again.");
            return;
        }
        clientsDAO.insertOrDelete(new Clients(1, firstName, lastName, address), true);
    }

    /**
     *Removes from the database the client having tha data the same as the parameters.
     *First it tries to find a client in the database with that values given as parameter. If a client was found it will be deleted.
     *If no client was found, the method returns.
     * @param firstName the first name of the client that will be deleted
     * @param lastName the last name of the client that will be deleted
     * @param address the address of the client that will be deleted
     */
    void deleteClient(String firstName, String lastName, String address) {
        Clients test = clientsDAO.getClientByFullName(firstName, lastName);
        if(test == null || !test.getAddress().equals(address)) {
            System.out.println("The client does not exist. It can not be removed.");
            return;
        }
        clientsDAO.deleteClientByFullName(firstName, lastName);
    }
}
