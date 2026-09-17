package com.ecommerce.dao;
import com.ecommerce.model.Product;
import com.ecommerce.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDao
{
    // Retrieve all products from the products table.
    // ResultSet contains all products returned by the SELECT query.
    // while(resultSet.next()) processes each product row one by one.
    // Read the product details from each row and display them.
    public void getAllProducts()
    {
        try
        {
            String query = "SELECT * FROM products";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                int product_id = resultSet.getInt("product_id");
                String product_name = resultSet.getString("product_name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int stock_quantity = resultSet.getInt("stock_quantity");
                int category_id = resultSet.getInt("category_id");

                System.out.println("Product ID: " + product_id);
                System.out.println("Product Name: " + product_name);
                System.out.println("Description: " + description);
                System.out.println("Price: " + price);
                System.out.println("Stock Quantity: " + stock_quantity);
                System.out.println("Category ID: " + category_id);
                System.out.println("----------------------------");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


        // Take the Id number of the product... and delete that product..
    // Delete an existing product using its product_id.
// productId identifies which product should be deleted.
// PreparedStatement is used to safely pass the productId.
// executeUpdate() returns the number of rows affected.
// If rowsAffected > 0, the product was deleted successfully.
    public void deleteProduct(int productId)
    {
        try
        {
            String query = "DELETE FROM products WHERE product_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Product deleted successfully.");
            }
            else
            {
                System.out.println("Product not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    // Update an existing product using its product_id.
// Product object contains the new/updated product information.
// PreparedStatement is used to safely insert values into the SQL query.
// product_id is used in WHERE clause to identify the product.
// executeUpdate() returns the number of rows affected.
// If rowsAffected > 0, the product was updated successfully.

    public void updateProduct(Product product)
    {
        try
        {
            String query = "UPDATE products SET product_name = ?, description = ?, price = ?, stock_quantity = ?, category_id = ? WHERE product_id = ?";

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, product.getProduct_name());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getStock_quantity());
            preparedStatement.setInt(5, product.getCategoryID());
            preparedStatement.setInt(6, product.getProduct_id());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Product updated successfully.");
            }
            else
            {
                System.out.println("Product not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    // get the product
    public Product getProductById(int productId)
    {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String getQuery = "select * from products where product_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery);
            preparedStatement.setInt(1,productId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                int product_id = resultSet.getInt("product_id");

                String product_name = resultSet.getString("product_name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int stock_quantity = resultSet.getInt("stock_quantity");
                int category_id = resultSet.getInt("category_id");
                Product product = new Product(product_name,description,price,stock_quantity,category_id);
                product.setProduct_id(product_id);
                return product;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void createProduct(Product product)
    {
        try
        {
            Connection connection = DatabaseConnection.getConnection();
            String createQuery = "insert into products (product_name,description,price,stock_quantity,category_id) values(?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(createQuery);

            preparedStatement.setString(   1,product.getProduct_name());
            preparedStatement.setString(   2,product.getDescription());
            preparedStatement.setDouble(   3,product.getPrice());
            preparedStatement.setInt(      4,product.getStock_quantity());
            preparedStatement.setInt(      5,product.getCategoryID());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected>0)
            {
                System.out.println("Product Created Successfully...");
            }
            else
            {
                System.out.println("Product Not Created....");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
