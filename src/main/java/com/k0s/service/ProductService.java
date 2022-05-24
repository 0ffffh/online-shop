package com.k0s.service;

import com.k0s.dao.ProductDao;
import com.k0s.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService  {

    private final ProductDao productDao;

    public ProductService(@Autowired ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAll() {
        return productDao.getAll();
    }

    public Product get(long id) {
        return productDao.get(id);
    }

    public void add(Product product) {
        productDao.add(product);
    }

    public void remove(long id) {
        productDao.remove(id);
    }

    public void update(Product product) {
        productDao.update(product);
    }

    public List<Product> search(String value) {
        return productDao.search(value);
    }

    public void addToCart(List<Product> cart, long productId) {
        cart.add(productDao.get(productId));
    }
    public void removeFromCart(List<Product> cart, int index) {
        cart.remove(index);
    }

    public void clearCart(List<Product> cart) {
        cart.clear();
    }
}
