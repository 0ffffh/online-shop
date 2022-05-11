package com.k0s.dao;

import com.k0s.entity.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getAll();

    Product get(long id);

    Product get(String name);

    void add(Product product);

    void remove(long id);

    void update(Product product);

    List<Product> search(String value);

}

