package com.ecommerce.service;

import com.ecommerce.DatabaseConnection;
import com.ecommerce.dao.CartDao;
import com.ecommerce.dao.CartItemDao;
import com.ecommerce.dao.ProductDao;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartService {

    /*
    DAO vs Service

    Dao:
-----CartDao-----
 INSERT cart
 SELECT cart
 UPDATE cart
 DELETE cart

 Service:
 ----CartService----
 Add product to cart
 Check product exists
 Check stock
 Create/update cart item
 Calculate cart total

 So we can say that the Service layer contains the business logic, while DAO handles the database communication.
     */

    private CartDao cartDao;
    private CartItemDao cartItemDao;
    private ProductDao productDao;

    public CartService()
    {
        cartDao = new CartDao();
        cartItemDao = new CartItemDao();
        productDao = new ProductDao();
    }


    public void updateCartItemQuantity(int cartItemId, int quantity)
    {
        if(cartItemDao.getCartItemById(cartItemId) == null)
        {
            System.out.println("Cart item not found.");
            return;
        }

        if(quantity <= 0)
        {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        CartItem cartItem = cartItemDao.getCartItemById(cartItemId);

        cartItem.setQuantity(quantity);

        cartItemDao.updateCartItem(cartItem);

        System.out.println("Cart item quantity updated.");
    }

    public void removeProductFromCart(int cartItemId)
    {
        if(cartItemDao.getCartItemById(cartItemId) == null)
        {
            System.out.println("Cart item not found.");
            return;
        }

        cartItemDao.deleteCartItem(cartItemId);

        System.out.println("Product removed from cart.");
    }

    public void viewCart(int cartId)
    {
        if(cartDao.getCartById(cartId) == null)
        {
            System.out.println("Cart not found.");
            return;
        }

        List<CartItem> cartItems = cartItemDao.getCartItemsByCartId(cartId);

        if(cartItems.isEmpty())
        {
            System.out.println("Cart is empty.");
            return;
        }

        for(CartItem cartItem : cartItems)
        {
            Product product = productDao.getProductById(cartItem.getProduct_id());

            if(product != null)
            {
                double subtotal = product.getPrice() * cartItem.getQuantity();

                System.out.println("Product: " + product.getProduct_name());
                System.out.println("Price: " + product.getPrice());
                System.out.println("Quantity: " + cartItem.getQuantity());
                System.out.println("Subtotal: " + subtotal);
                System.out.println("---------------------------");
            }
        }

        System.out.println("Total: " + calculateCartTotal(cartId));
    }


    public double calculateCartTotal(int cartId)
    {
        if(cartDao.getCartById(cartId) == null)
        {
            System.out.println("Cart not found...");
            return 0;
        }

        List<CartItem> cartItems = cartItemDao.getCartItemsByCartId(cartId);

        double total = 0;

        for(CartItem cartItem : cartItems)
        {
            Product product = productDao.getProductById(cartItem.getProduct_id());

            if(product != null)
            {
                total = total + (product.getPrice() * cartItem.getQuantity());
            }
        }

        return total;
    }

    public List<CartItem> getCartItemsByCartId(int cartId)
    {
        List<CartItem> cartItems = new ArrayList<>();

        try
        {
            String query = "SELECT * FROM cart_items WHERE cart_id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, cartId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                int cart_item_id = resultSet.getInt("cart_item_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                CartItem cartItem = new CartItem(cartId, product_id, quantity);
                cartItem.setCart_item_id(cart_item_id);

                cartItems.add(cartItem);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return cartItems;
    }

    public void addProductToCart(int cartId, int productId, int quantity)
    {
        if(cartDao.getCartById(cartId) == null)
        {
            System.out.println("Cart not found.");
            return;
        }

        Product product = productDao.getProductById(productId);

        if(product == null)
        {
            System.out.println("Product not found.");
            return;
        }

        if(quantity <= 0)
        {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        CartItem existingItem = cartItemDao.getCartItemByCartIdAndProductId(cartId, productId);

        int finalQuantity = quantity;

        if(existingItem != null)
        {
            finalQuantity = existingItem.getQuantity() + quantity;
        }

        if(finalQuantity > product.getStock_quantity())
        {
            System.out.println("Not enough stock available.");
            return;
        }

        if(existingItem != null)
        {
            existingItem.setQuantity(finalQuantity);
            cartItemDao.updateCartItem(existingItem);

            System.out.println("Cart quantity updated.");
        }
        else
        {
            CartItem cartItem = new CartItem(cartId, productId, quantity);

            cartItemDao.createCartItem(cartItem);

            System.out.println("Product added to cart.");
        }
    }
}
