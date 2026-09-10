package com.ecommerce.model;

public class OrderItem {
 private int order_item_id;
 private  int order_id;
 private int product_id;
 private  int quantity;
 private  double price;


 OrderItem(int order_id, int product_id, int quantity, double price)
 {
     this.order_id = order_id;
     this.product_id = product_id ;
     this.quantity = quantity ;
     this.price = price;
 }
 public double getPrice()
 {
     return price;
 }
 public void setPrice(double price)
 {
     this.price = price;
 }
 public int getQuantity()
 {
     return  quantity;
 }
 public void setQuantity(int quantity)
 {
     this.quantity = quantity;
 }
 public int getProduct_id()
 {
     return  product_id;
 }
 public void setProduct_id(int product_id)
 {
     this.product_id = product_id;
 }
 public int getOrder_id()
 {
     return order_id;
 }
 public void setOrder_id(int orderId)
 {
     this.order_id = orderId;
 }
 public int getOrder_item_id()
 {
     return  order_item_id;
 }
 public void setOrder_item_id(int order_item_id)
 {
     this.order_item_id = order_item_id;
 }

}
