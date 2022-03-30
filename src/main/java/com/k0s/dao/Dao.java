package com.k0s.dao;


import java.util.List;


public interface Dao<T> {

    List<T> getAll();

    T get(long id);

    T get(String name);

    void add(T t);

    void remove(long id);

    void update(T t);

    List<T> search(String value);

}
