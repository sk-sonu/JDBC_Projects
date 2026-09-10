package com.ecommerce.model;

import java.sql.Timestamp;

public class Payment {
private int payment_id;
private  int order_id;
private  String payment_method;
private  String payment_status;
private Timestamp payment_date;


    Payment(String payment_method, int order_id)
    {
        this.payment_method = payment_method;
        this.order_id = order_id;
    }
    public Timestamp getPayment_date()
    {
        return  payment_date;
    }
    public void setPayment_date(Timestamp payment_date)
    {
        this.payment_date = payment_date;
    }
    public String getPayment_status()
    {
        return  payment_status;
    }
    public void setPayment_status(String payment_status)
    {
        this.payment_status = payment_status;
    }
    public String getPayment_method()
    {
        return  payment_method;
    }
    public void setPayment_method(String payment_method)
    {
        this.payment_method = payment_method;
    }
    public int getOrder_id()
    {
        return order_id;
    }
    public void setOrder_id(int orderId)
    {
        this.order_id = orderId;
    }
    public int getPayment_id()
    {
        return  payment_id;
    }
    public void setPayment_id(int payment_id)
    {
        this.payment_id = payment_id;
    }
}
