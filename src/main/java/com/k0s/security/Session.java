package com.k0s.security;

import com.k0s.entity.Product;
import com.k0s.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class Session {
    private String token;
    private User user;
    private LocalDateTime expireDate;
    private List<Product> cart;

    public Session() {
    }

    public Session(String token, User user, LocalDateTime expireDate) {
        this.token = token;
        this.user = user;
        this.expireDate = expireDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public List<Product> getCart() {
        return cart;
    }

    public void setCart(List<Product> cart) {
        this.cart = cart;
    }
}
