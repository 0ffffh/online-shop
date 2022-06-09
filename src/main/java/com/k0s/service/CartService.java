package com.k0s.service;

import com.k0s.dao.UserCartDao;
import com.k0s.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final UserCartDao userCartDao;

    @Autowired
    public CartService(UserCartDao userCartDao) {
        this.userCartDao = userCartDao;
    }


    public void addToCart(String name, Long productId) {
        userCartDao.addToCart(name, productId);
    }

    public List<Product> getUserCart(String name) {
        return userCartDao.getProductCart(name);
    }

    public void removeProduct(String name, Long productId) {
        userCartDao.removeProduct(name, productId);
    }

    public void clearCart(String username) {
        userCartDao.clearCart(username);
    }
}
