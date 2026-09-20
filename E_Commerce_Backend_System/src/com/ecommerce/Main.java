package com.ecommerce;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.ecommerce.dao.CategoryDao;
import com.ecommerce.dao.ProductDao;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.dao.UserDao;
public class Main {
    public static void main(String[] args) throws SQLException {


        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Connection connection = null;
        try {
             connection = DatabaseConnection.getConnection();
            System.out.println("Database Connected Successfully....");
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

    System.out.println("===================User===================");

        UserDao userDao = new UserDao();
        User user = userDao.getUserById(9);

        if(user != null)
        {
            user.setName("Rahul Updated");
            user.setEmail("rahul_updated@gmail.com");
            user.setPassword("54321");
            user.setRole("customer");
            userDao.updateUser(user);
        }
        else
        {
            System.out.println("User Not Found...");
        }

    }
}
