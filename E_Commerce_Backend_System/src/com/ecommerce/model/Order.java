package com.ecommerce.model;

import java.sql.Timestamp;

public class Order {
    private int order_id;
    private int user_id;
    private double total_amount;
    private String order_status;
    private Timestamp order_date;

    Order(int user_id, double total_amount)
    {
        this.user_id = user_id;
        this.total_amount = total_amount;
    }
    public Timestamp getOrder_date()
    {
        return order_date;
    }
    public void setOrder_date(Timestamp orderDate)
    {
        this.order_date = orderDate;
    }
    public String getOrder_status()
    {
        return  order_status;
    }
    public void setOrder_status(String  order_status)
    {
        this.order_status = order_status;
    }
    public double getTotal_amount()
    {
        return  total_amount;
    }
    public void setTotal_amount(double total_amount)
    {
        this.total_amount = total_amount;
    }
    public int getUser_id()
    {
        return user_id;
    }
    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }
    public int getOrder_id()
    {
        return order_id;
    }
    public void setOrder_id(int orderId)
    {
        this.order_id = orderId;
    }
}
