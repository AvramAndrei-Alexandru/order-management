package com.data_access;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Performs the application communication with the database.
 */
class ConnectionFactory {

        private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
        private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
        private static final String DBURL = "jdbc:mysql://localhost:3306/order_management";
        private static final String USER = "root";
        private static final String PASS = "root";
    /**
     * A single instance of this class.
     */
    private static ConnectionFactory singleInstance = new ConnectionFactory();

        private ConnectionFactory() {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    /**
     * Creates a database connection.
     * @return The database connection.
     */
    private Connection createConnection() {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(DBURL, USER, PASS);
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to connect to the database");
                e.printStackTrace();
            }
            return connection;
        }

        static Connection getConnection() {
            return singleInstance.createConnection();
        }

    /**
     * Closes the connection given as a parameter.
     * @param connection The connection that will be closed.
     */
    static void close(Connection connection) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "An error occurred while trying to close the connection");
                }
            }
        }

    /**
     * Closes the statement given as a parameter.
     * @param statement The statement that will be closed.
     */
        static void close(Statement statement) {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "An error occurred while trying to close the statement");
                }
            }
        }

    /**
     * Closes the resultSet given as a parameter.
     * @param resultSet The resultSet that will be closed.
     */
        static void close(ResultSet resultSet) {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "An error occurred while trying to close the ResultSet");
                }
            }
        }
    }

