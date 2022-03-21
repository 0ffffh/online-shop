package com.k0s.dao.jdbc;

import com.k0s.dao.ProductDao;
import com.k0s.entity.Product;
import com.k0s.util.PropertiesReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProductDaoTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAll() {
    }

    @Test
    void get() {
    }

    @Test
    void remove() {
        PropertiesReader propertiesReader = new PropertiesReader("db.properties");
        propertiesReader.readProperties();
        ConnectionFactory connectionFactory = new ConnectionFactory(propertiesReader.getProperties());
        ProductDao<Product> productDao = new JdbcProductDao(connectionFactory);

        productDao.remove(100500);
    }

    @Test
    void update() {
    }

    @Test
    void search() {
    }
}