package com.ecommerce.model;

public class Category {
    private int category_id;
    private String category_name;

    Category(String category_name)
    {
        this.category_name = category_name;
    }

    public int getCategory_id()
    {
        return category_id;
    }
    public void setCategory_id(int categoryId)
    {
        this.category_id = categoryId;
    }
    public String getCategory_name()
    {
        return  category_name;
    }
    public void setCategory_name(String category_name)
    {
        this.category_name = category_name;
    }
}
