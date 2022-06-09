package com.k0s.dao;

import com.k0s.entity.Product;

import java.util.List;

//@Repository
public interface UserCartDao {
    void addToCart(String name, Long productId);

    List<Product> getProductCart(String name);

    void removeProduct(String name, Long productId);

    void clearCart(String username);

}
