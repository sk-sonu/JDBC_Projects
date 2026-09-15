package com.ecommerce.dao;
import com.ecommerce.DatabaseConnection;
import com.ecommerce.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDao
{

        public void deleteCategory(int categoryId)
        {
            try
            {
                Connection connection = DatabaseConnection.getConnection();

                String deleteQuery = "delete from categories where category_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setInt(1,categoryId);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0)
                {
                    System.out.println("Category Deleted Successfully...");
                }
                else
                {
                    System.out.println("Category Not Found...");
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }

        public void updateCategory(Category category)
        {
            try
            {
                Connection connection = DatabaseConnection.getConnection();
                String updateQuery = "update categories set category_name = ? where category_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1,category.getCategory_name());
                preparedStatement.setInt(2,category.getCategory_id());
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0)
                {
                    System.out.println("Category Updated Successfully.....");
                }
                else
                {
                    System.out.println("Category Not Found...");
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        public Category getCategoryById(int categoryId)
        {
            try
            {
                Connection connection = DatabaseConnection.getConnection();
                String getQuery = "select * from categories where category_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(getQuery);
                preparedStatement.setInt(1,categoryId);

                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                {
                 int id =   resultSet.getInt("category_id");
                   String categoryName  =  resultSet.getString("category_name");
                   Category category = new Category(categoryName);
                   category.setCategory_id(id);
                   return  category;
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
            return null;
        }
        public void createCategory(Category category)
        {
            try
            {
                Connection connection = DatabaseConnection.getConnection();
                String insertQuery = "insert into categories (category_name) values (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1,category.getCategory_name());
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0)
                {
                    System.out.println("Category Created  Successfully....");
                }
                else
                {
                    System.out.println("Category Not Created....");
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
}
