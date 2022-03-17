package com.k0s.service;

import com.k0s.dao.ProductDao;
import com.k0s.entity.Product;

import java.util.List;

public class ProductService  {

    private final ProductDao<Product> productDao;

    public ProductService(ProductDao<Product> productDao) {
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
}
