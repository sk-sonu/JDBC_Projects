package com.ecommerce.dao;

import com.ecommerce.DatabaseConnection;
import com.ecommerce.model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemDao {


    public void getAllOrderItems()
    {
        try
        {
            // Retrieve all order items from the orders_items table
            String query = "SELECT * FROM orders_items";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process each order item one by one
            while(resultSet.next())
            {
                int order_item_id = resultSet.getInt("order_item_id");
                int order_id = resultSet.getInt("order_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                System.out.println("Order Item ID: " + order_item_id);
                System.out.println("Order ID: " + order_id);
                System.out.println("Product ID: " + product_id);
                System.out.println("Quantity: " + quantity);
                System.out.println("Price: " + price);
                System.out.println("----------------------------");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public void deleteOrderItem(int orderItemId)
    {
        try
        {
            // Delete an existing order item using its ID
            String query = "DELETE FROM orders_items WHERE order_item_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the ID of the order item that should be deleted
            preparedStatement.setInt(1, orderItemId);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Order item deleted successfully.");
            }
            else
            {
                System.out.println("Order item not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void updateOrderItem(OrderItem orderItem)
    {
        try
        {
            // Update the quantity and price of an existing order item
            String query = "UPDATE orders_items SET quantity = ?, price = ? WHERE order_item_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the new quantity
            preparedStatement.setInt(1, orderItem.getQuantity());

            // Set the updated price
            preparedStatement.setDouble(2, orderItem.getPrice());

            // Use order_item_id to identify which item should be updated
            preparedStatement.setInt(3, orderItem.getOrder_item_id());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Order item updated successfully.");
            }
            else
            {
                System.out.println("Order item not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public OrderItem getOrderItemById(int orderItemId)
    {
        try
        {
            // Find one order item using its order_item_id
            String query = "SELECT * FROM orders_items WHERE order_item_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the ID of the order item we want to find
            preparedStatement.setInt(1, orderItemId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                int id = resultSet.getInt("order_item_id");
                int order_id = resultSet.getInt("order_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                // Create Java object using database values
                OrderItem orderItem = new OrderItem(order_id, product_id, quantity, price);

                // Set the database-generated ID into the Java object
                orderItem.setOrder_item_id(id);

                return orderItem;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void createOrderItem(OrderItem orderItem)
    {
        try
        {
            // Insert a new item into an order
            String query = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the order in which this product belongs
            preparedStatement.setInt(1, orderItem.getOrder_id());

            // Set the product being ordered
            preparedStatement.setInt(2, orderItem.getProduct_id());

            // Set the quantity of the product
            preparedStatement.setInt(3, orderItem.getQuantity());

            // Store the product price at the time of purchase
            preparedStatement.setDouble(4, orderItem.getPrice());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Order item created successfully.");
            }
            else
            {
                System.out.println("Order item not created.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}