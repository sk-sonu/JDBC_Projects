package com.ecommerce.model;

import java.sql.Timestamp;

public class Cart {
    private  int cart_id;
    private int user_id;
    private Timestamp created_at;

    Cart(int user_id)
    {
        this.user_id = user_id;
    }

    public int getCart_id()
    {
        return  cart_id;
    }
    public void setCart_id(int cartId)
    {
        this.cart_id = cartId;
    }
    public int getUser_id()
    {
        return  user_id;
    }
    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }
    public Timestamp getCreated_at()
    {
        return  created_at;
    }
    public void setCreated_at(Timestamp createdAt)
    {
        this.created_at = createdAt;
    }
}
