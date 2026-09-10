package com.ecommerce.model;

import java.sql.Timestamp;

public class User {
    private String name;
    private String email;
    private String password;
    private String role;
    private Timestamp created_at;
    private int user_id;
   public User(String name, String email , String password, String role)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getRole()
    {
        return role;
    }
    public void setRole(String role)
    {
        this.role = role;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
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
    public  void setCreated_at(Timestamp createdAt)
    {
        this.created_at = createdAt;
    }
}
