package com.ecommerce.dao;
import com.ecommerce.model.User;
import com.ecommerce.DatabaseConnection;

import java.sql.*;

public class UserDao {
    // DAO - Data Access Object.. It is use to communicate with the data base;

    public void updateUser(User user)
    {
        try
        {
            String query = "update users set name = ?, email = ?, password = ?, role = ? where user_id = ?";
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setString(4,user.getRole());
            preparedStatement.setInt(   5,user.getUser_id());

            System.out.println("User ID being updated: " + user.getUser_id());
            int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0)
                {
                    System.out.println("Data Updated Successfully..");
                }
                else
                {
                    System.out.println("Data not Update");
                }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public User getUserById(int userId)
    {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "select * from users where user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                int id = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
//                Timestamp createdAt = resultSet.getTimestamp("created_at");

                User user = new User(name,email,password,role);

                user.setUser_id(id);
                return user;

            }

        }
        catch (SQLException e)
        {
            System.out.println("User Not Found...");

            System.out.println(e.getMessage());
        }
        return null;
    }
    public void createUser(User user)
    {
        try{
            String query = "insert into users (name,email, password, role) values(?,?,?,?)";

            Connection connection =  DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setString(4, user.getRole());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected>0)
            {
                System.out.println("User's Data Inserted Successfully....");
            }
            else
            {
                System.out.println("Data Not Inserted...");
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
