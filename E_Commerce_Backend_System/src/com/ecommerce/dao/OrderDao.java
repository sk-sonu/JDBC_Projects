package com.ecommerce.dao;

import com.ecommerce.DatabaseConnection;
import com.ecommerce.model.Order;

import java.sql.*;

public class OrderDao {

    public void getAllOrders()
    {
        try
        {
            // Retrieve all orders from the orders table
            String query = "SELECT * FROM orders";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Process each order one by one
            while(resultSet.next())
            {
                int order_id = resultSet.getInt("order_id");
                int user_id = resultSet.getInt("user_id");
                double total_amount = resultSet.getDouble("total_amount");
                String order_status = resultSet.getString("order_status");
                Timestamp order_date = resultSet.getTimestamp("order_date");

                // Display order information
                System.out.println("Order ID: " + order_id);
                System.out.println("User ID: " + user_id);
                System.out.println("Total Amount: " + total_amount);
                System.out.println("Order Status: " + order_status);
                System.out.println("Order Date: " + order_date);
                System.out.println("----------------------------");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void deleteOrder(int orderId)
    {
        try
        {
            // Delete an existing order using its order_id
            String query = "DELETE FROM orders WHERE order_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the order_id to identify which order should be deleted
            preparedStatement.setInt(1, orderId);

            int rowsAffected = preparedStatement.executeUpdate();

            // Check whether the order was actually deleted
            if(rowsAffected > 0)
            {
                System.out.println("Order deleted successfully.");
            }
            else
            {
                System.out.println("Order not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void updateOrder(Order order)
    {
        try
        {
            // Update the status of an existing order
            String query = "UPDATE orders SET order_status = ? WHERE order_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the new order status
            preparedStatement.setString(1, order.getOrder_status());

            // Use order_id to identify which order should be updated
            preparedStatement.setInt(2, order.getOrder_id());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check whether the order was actually updated
            if(rowsAffected > 0)
            {
                System.out.println("Order updated successfully.");
            }
            else
            {
                System.out.println("Order not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Order getOrderById(int orderId)
    {
        try
        {
            // Find one order using its order_id
            String query = "SELECT * FROM orders WHERE order_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the ID of the order we want to find
            preparedStatement.setInt(1, orderId);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Check whether the order exists
            if(resultSet.next())
            {
                int id = resultSet.getInt("order_id");
                int user_id = resultSet.getInt("user_id");
                double total_amount = resultSet.getDouble("total_amount");
                String order_status = resultSet.getString("order_status");
                Timestamp order_date = resultSet.getTimestamp("order_date");

                // Create Order object using the retrieved data
                Order order = new Order(user_id, total_amount);

                // Set database-generated values into the Java object
                order.setOrder_id(id);
                order.setOrder_status(order_status);
                order.setOrder_date(order_date);

                return order;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void createOrder(Order order)
    {
        try
        {
            // Insert a new order for a user
            String query = "INSERT INTO orders (user_id, total_amount) VALUES (?, ?)";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the user who placed the order
            preparedStatement.setInt(1, order.getUser_id());

            // Set the total amount of the order
            preparedStatement.setDouble(2, order.getTotal_amount());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check whether the order was successfully created
            if(rowsAffected > 0)
            {
                System.out.println("Order created successfully.");
            }
            else
            {
                System.out.println("Order not created.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
