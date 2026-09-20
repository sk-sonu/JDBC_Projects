package com.ecommerce.dao;

import com.ecommerce.DatabaseConnection;
import com.ecommerce.model.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemDao {


    public CartItem getCartItemByCartIdAndProductId(int cartId, int productId)
    {
        CartItem cartItem = null;

        try
        {
            String query = "SELECT * FROM cart_items WHERE cart_id = ? AND product_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, cartId);
            preparedStatement.setInt(2, productId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                int cart_item_id = resultSet.getInt("cart_item_id");
                int quantity = resultSet.getInt("quantity");

                cartItem = new CartItem(cartId, productId, quantity);
                cartItem.setCart_item_id(cart_item_id);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return cartItem;
    }

    public CartItem getCartItemById(int cartItemId)
    {
        CartItem cartItem = null;

        try
        {
            String query = "SELECT * FROM cart_items WHERE cart_item_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, cartItemId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                int cart_id = resultSet.getInt("cart_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                cartItem = new CartItem(cart_id, product_id, quantity);
                cartItem.setCart_item_id(cartItemId);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return cartItem;
    }
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

    public List<CartItem> getCartItemsByCartId(int cartId)
    {
        List<CartItem> cartItems = new ArrayList<>();

        try
        {
            String query = "SELECT * FROM cart_items WHERE cart_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, cartId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                int cart_item_id = resultSet.getInt("cart_item_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                CartItem cartItem = new CartItem(cartId, product_id, quantity);
                cartItem.setCart_item_id(cart_item_id);

                cartItems.add(cartItem);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return cartItems;
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
