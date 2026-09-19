package com.ecommerce.dao;

import com.ecommerce.DatabaseConnection;
import com.ecommerce.model.Payment;

import java.sql.*;

public class PaymentDao {

    public void getAllPayments()
    {
        try
        {
            // Retrieve all payments from the payments table
            String query = "SELECT * FROM payments";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process each payment one by one
            while(resultSet.next())
            {
                int payment_id = resultSet.getInt("payment_id");
                int order_id = resultSet.getInt("order_id");
                String payment_method = resultSet.getString("payment_method");
                String payment_status = resultSet.getString("payment_status");
                Timestamp payment_date = resultSet.getTimestamp("payment_date");

                System.out.println("Payment ID: " + payment_id);
                System.out.println("Order ID: " + order_id);
                System.out.println("Payment Method: " + payment_method);
                System.out.println("Payment Status: " + payment_status);
                System.out.println("Payment Date: " + payment_date);
                System.out.println("----------------------------");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public void deletePayment(int paymentId)
    {
        try
        {
            // Delete an existing payment using its payment_id
            String query = "DELETE FROM payments WHERE payment_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the payment ID to identify which payment should be deleted
            preparedStatement.setInt(1, paymentId);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Payment deleted successfully.");
            }
            else
            {
                System.out.println("Payment not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void updatePayment(Payment payment)
    {
        try
        {
            // Update the payment method and status of an existing payment
            String query = "UPDATE payments SET payment_method = ?, payment_status = ? WHERE payment_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the new payment method
            preparedStatement.setString(1, payment.getPayment_method());

            // Set the new payment status
            preparedStatement.setString(2, payment.getPayment_status());

            // Use payment_id to identify which payment should be updated
            preparedStatement.setInt(3, payment.getPayment_id());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Payment updated successfully.");
            }
            else
            {
                System.out.println("Payment not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Payment getPaymentById(int paymentId)
    {
        try
        {
            // Find one payment using its payment_id
            String query = "SELECT * FROM payments WHERE payment_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the payment ID we want to find
            preparedStatement.setInt(1, paymentId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                int id = resultSet.getInt("payment_id");
                int order_id = resultSet.getInt("order_id");
                String payment_method = resultSet.getString("payment_method");
                String payment_status = resultSet.getString("payment_status");
                Timestamp payment_date = resultSet.getTimestamp("payment_date");

                // Create Payment object using database values
                Payment payment = new Payment(payment_method, order_id);

                // Set database-generated values into Java object
                payment.setPayment_id(id);
                payment.setPayment_status(payment_status);
                payment.setPayment_date(payment_date);

                return payment;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }
    public void createPayment(Payment payment)
    {
        try
        {
            // Insert a new payment for an order
            String query = "INSERT INTO payments (order_id, payment_method) VALUES (?, ?)";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the order for which payment is being made
            preparedStatement.setInt(1, payment.getOrder_id());

            // Set the payment method: UPI, CARD or C.O.D
            preparedStatement.setString(2, payment.getPayment_method());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Payment created successfully.");
            }
            else
            {
                System.out.println("Payment not created.");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
