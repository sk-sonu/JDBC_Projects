package com.ecommerce.dao;

import com.ecommerce.DatabaseConnection;
import com.ecommerce.model.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartItemDao {


    public void getAllCartItems()
    {
        try
        {
            // Retrieve all cart items from the cart_items table
            String query = "SELECT * FROM cart_items";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Process each cart item one by one
            while(resultSet.next())
            {
                int cart_item_id = resultSet.getInt("cart_item_id");
                int cart_id = resultSet.getInt("cart_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                // Display cart item information
                System.out.println("Cart Item ID: " + cart_item_id);
                System.out.println("Cart ID: " + cart_id);
                System.out.println("Product ID: " + product_id);
                System.out.println("Quantity: " + quantity);
                System.out.println("----------------------------");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCartItem(int cartItemId)
    {
        try
        {
            // Delete a cart item using its cart_item_id
            String query = "DELETE FROM cart_items WHERE cart_item_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the ID of the cart item that should be deleted
            preparedStatement.setInt(1, cartItemId);

            int rowsAffected = preparedStatement.executeUpdate();

            // Check whether the cart item was actually deleted
            if(rowsAffected > 0)
            {
                System.out.println("Cart item deleted successfully.");
            }
            else
            {
                System.out.println("Cart item not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public void updateCartItem(CartItem cartItem)
    {
        try
        {
            // Update the quantity of an existing cart item
            String query = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the new quantity
            preparedStatement.setInt(1, cartItem.getQuantity());

            // Use cart_item_id to identify which cart item should be updated
            preparedStatement.setInt(2, cartItem.getCart_item_id());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check whether the cart item was actually updated
            if(rowsAffected > 0)
            {
                System.out.println("Cart item updated successfully.");
            }
            else
            {
                System.out.println("Cart item not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public CartItem getCartItemById(int cartItemId)
    {
        try
        {
            // Find one cart item using its cart_item_id
            String query = "SELECT * FROM cart_items WHERE cart_item_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the ID of the cart item we want to find
            preparedStatement.setInt(1, cartItemId);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Check whether the cart item exists
            if(resultSet.next())
            {
                int id = resultSet.getInt("cart_item_id");
                int cart_id = resultSet.getInt("cart_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                // Create a CartItem object using the retrieved data
                CartItem cartItem = new CartItem(cart_id, product_id, quantity);

                // Set the database-generated ID into the Java object
                cartItem.setCart_item_id(id);

                return cartItem;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void createCartItem(CartItem cartItem)
    {
        try
        {
            // Insert a product into a cart with the required quantity
            String query = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the cart where the product will be added
            preparedStatement.setInt(1, cartItem.getCart_id());

            // Set the product that will be added to the cart
            preparedStatement.setInt(2, cartItem.getProduct_id());

            // Set how many units of the product are being added
            preparedStatement.setInt(3, cartItem.getQuantity());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check whether the cart item was successfully inserted
            if(rowsAffected > 0)
            {
                System.out.println("Product added to cart successfully.");
            }
            else
            {
                System.out.println("Product not added to cart.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
