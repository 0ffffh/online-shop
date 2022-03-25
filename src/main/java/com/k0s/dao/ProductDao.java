package com.k0s.dao;


import java.util.List;
import java.util.Optional;


public interface ProductDao<T> {

    List<T> getAll();

    T get(long id);

    void add(T t);

    void remove(long id);

    void update(T t);

    List<T> search(String value);

}
