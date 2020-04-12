package com.data_access;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates and executes the queries that are common to all the tables in the database.
 * @param <T> The generic class.
 */
public class AbstractDAO<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Creates a SELECT SQL query that searches the table for a given id.
     * @return The SELECT by id query.
     */
    private String createSelectIDQuery() {
        return "SELECT " +
                " * " +
                " FROM " +
                type.getSimpleName() +
                " WHERE " + type.getDeclaredFields()[0].getName() + " =?";
    }

    /**
     * Creates a SELECT ALL SQL query.
     * @return The SELECT all query.
     */
    private String createSelectAllQuery() {
         return "SELECT * FROM " + type.getSimpleName();
    }

    /**
     * Finds all the data that exists inside a table.
     * @return A list objects of type T, objects that were found inside the searched table. If the table is empty returns null.
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     *
     * @param id The id of the object that is being searched.
     * @return The object found in the table. If it's not found returns null.
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectIDQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            List<T> list = createObjects(resultSet);
            if(list.size() == 0)
                return null;
            else return list.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Creates a list of objects of type T. These objects were previously found in the database.
     * @param resultSet The result of the query execution.
     * @return A list of objects if type T.
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();

        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | InvocationTargetException | IllegalArgumentException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Creates an INSERT SQL query.
     * @param t The object that will be inserted in the database.
     * @return The SQL INSERT query.
     */
    private String createInsertQuery(T t) {
        StringBuilder query = new StringBuilder("INSERT INTO " + type.getSimpleName() + "(");
        for (int i = 1; i < type.getDeclaredFields().length; i++) {
            query.append("`").append(type.getDeclaredFields()[i].getName()).append("`");
            if (i == type.getDeclaredFields().length - 1) {
                query.append(")");
            } else
                query.append(", ");
        }
        query.append(" VALUES (");
        try {
            for (int i = 1; i < type.getDeclaredFields().length; i++) {
                Class value = type.getDeclaredFields()[i].getType();
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[i].getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                boolean isString = false;
                if (value.equals(String.class)) {
                    query.append("'");
                    isString = true;
                }
                query.append(method.invoke(t));
                if (isString)
                    query.append("'");
                if (i == type.getDeclaredFields().length - 1)
                    query.append(")");
                else
                    query.append(", ");
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ignored) {
        }
            return query.toString();
    }

    /**
     * Performs the insertion or deletion of a given object from the table.
     * @param t The object that is inserted or deleted.
     * @param insertMode If true insertion is performed otherwise deletion.
     */
    public void insertOrDelete(T t, boolean insertMode) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        String query;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();
            if(insertMode) {
                query = createInsertQuery(t);
                statement.executeUpdate(query);
            }
            else {
                query = createDeleteQuery(t);
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
        }
    }

    /**
     * Creates a SQL DELETE query.
     * @param t The object that will be deleted.
     * @return SQL DELETE query.
     */
    private String createDeleteQuery(T t) {
        StringBuilder query = new StringBuilder("DELETE FROM " + type.getSimpleName() + " WHERE " + type.getDeclaredFields()[0].getName() + " = ");
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[0].getName(), type);
            Method method = propertyDescriptor.getReadMethod();
            query.append(method.invoke(t));
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return query.toString();
    }

    /**
     * Creates a SQL UPDATE query.
     * @param t The object that will be updated.
     * @return SQL UPDATE query.
     */
    private String createUpdateQuery(T t) {
        StringBuilder query = new StringBuilder("UPDATE " + type.getSimpleName() + " SET ");
        try {
            for (int i = 1; i < type.getDeclaredFields().length; i++) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[i].getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                query.append(type.getDeclaredFields()[i].getName()).append(" = ");
                boolean isString = false;
                if (type.getDeclaredFields()[i].getType().equals(String.class)) {
                    query.append("'");
                    isString = true;
                }
                query.append(method.invoke(t));
                if (isString) {
                    query.append("'");
                }
                if (i != type.getDeclaredFields().length - 1) {
                    query.append(", ");
                }
            }
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        }
        query.append(" WHERE ").append(type.getDeclaredFields()[0].getName()).append(" = ");
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[0].getName(), type);
            Method method = propertyDescriptor.getReadMethod();
            query.append(method.invoke(t));
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return query.toString();
    }

    /**
     * Updates a given object inside the corresponding table.
     * @param t The object that is being updated.
     */
    public void update(T t) {
        String query = createUpdateQuery(t);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
        }
    }
}
