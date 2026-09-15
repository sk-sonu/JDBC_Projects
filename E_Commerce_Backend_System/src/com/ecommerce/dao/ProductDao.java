package com.ecommerce.dao;
import com.ecommerce.model.Product;
import com.ecommerce.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDao
{
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
