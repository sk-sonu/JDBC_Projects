package com.ecommerce.dao;

import com.ecommerce.DatabaseConnection;
import com.ecommerce.model.Cart;

import java.sql.*;

public class CartDao {

    public void getAllCarts()
    {
        try
        {
            // Retrieve all carts from the cart table
            String query = "SELECT * FROM cart";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Process each cart row one by one
            while(resultSet.next())
            {
                int cart_id = resultSet.getInt("cart_id");
                int user_id = resultSet.getInt("user_id");
                Timestamp created_at = resultSet.getTimestamp("created_at");

                // Display cart information
                System.out.println("Cart ID: " + cart_id);
                System.out.println("User ID: " + user_id);
                System.out.println("Created At: " + created_at);
                System.out.println("----------------------------");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public void deleteCart(int cartId)
    {
        try
        {
            // Delete an existing cart using its cart_id
            String query = "DELETE FROM cart WHERE cart_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the cart_id to identify which cart should be deleted
            preparedStatement.setInt(1, cartId);

            int rowsAffected = preparedStatement.executeUpdate();

            // Check whether the cart was actually deleted
            if(rowsAffected > 0)
            {
                System.out.println("Cart deleted successfully.");
            }
            else
            {
                System.out.println("Cart not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public void updateCart(Cart cart)
    {
        try
        {
            // Update the user_id of an existing cart
            String query = "UPDATE cart SET user_id = ? WHERE cart_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the new user_id
            preparedStatement.setInt(1, cart.getUser_id());

            // Use cart_id to identify which cart should be updated
            preparedStatement.setInt(2, cart.getCart_id());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check whether any cart was actually updated
            if(rowsAffected > 0)
            {
                System.out.println("Cart updated successfully.");
            }
            else
            {
                System.out.println("Cart not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Cart getCartById(int cartId)
    {
        try
        {
            String query = "SELECT * FROM cart WHERE cart_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, cartId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                int id = resultSet.getInt("cart_id");
                int user_id = resultSet.getInt("user_id");
                Timestamp created_at = resultSet.getTimestamp("created_at");

                Cart cart = new Cart(user_id);

                cart.setCart_id(id);
                cart.setCreated_at(created_at);

                return cart;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }


    public void createCart(Cart cart)
    {
        try
        {
            String query = "INSERT INTO cart (user_id) VALUES (?)";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, cart.getUser_id());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Cart created successfully.");
            }
            else
            {
                System.out.println("Cart not created.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
