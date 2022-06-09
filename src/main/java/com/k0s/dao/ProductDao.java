package com.k0s.dao;

import com.k0s.entity.Product;

import java.util.List;
import java.util.Optional;

//@Repository
public interface ProductDao {

    List<Product> getAll();

    Optional<Product> get(long id);

    Product get(String name);

    void add(Product product);

    void remove(long id);

    void update(Product product);

    List<Product> search(String value);

}

