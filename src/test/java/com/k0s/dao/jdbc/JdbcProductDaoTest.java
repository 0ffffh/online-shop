//package com.k0s.dao.jdbc;
//
//
//import com.k0s.entity.Product;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.flywaydb.core.Flyway;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//
//import javax.sql.DataSource;
//import java.time.LocalDateTime;
//import java.util.List;
//
//class JdbcProductDaoTest {
//    private static JdbcProductDao jdbcProductDao;
//
//    @BeforeAll
//    static void setUp() {
//
//        HikariConfig hikariConfig = new HikariConfig();
//
////        hikariConfig.setJdbcUrl("jdbc:h2:mem:test");
////        hikariConfig.setUsername("user");
////        hikariConfig.setPassword("password");
//
////        hikariConfig.setJdbcUrl("jdbc:postgresql://ec2-99-80-170-190.eu-west-1.compute.amazonaws.com:5432/defpo01vks4v8d");
////        hikariConfig.setUsername("zfcyuffrlcbpoh");
////        hikariConfig.setPassword("14b61823f2de51f82cbe17c3d327fe50c887ac93c8989bdb89284e5fef632ed4");
//
//        DataSource dataSource = new HikariDataSource(hikariConfig);
//
//        jdbcProductDao = new JdbcProductDao(dataSource);
//
//
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .load();
//        flyway.migrate();
//    }
//
//    @Test
//    void getAll() {
//        List<Product> productList = jdbcProductDao.getAll();
//        assertEquals(0, productList.size());
//        Product product = new Product();
//        product.setName("product");
//        product.setPrice(111);
//        product.setCreationDate(LocalDateTime.now());
//
//        jdbcProductDao.add(product);
//
//
//        productList = jdbcProductDao.getAll();
//        assertEquals(1, productList.size());
//
//    }
//
//    @Test
//    void get() {
//        Product expected = new Product();
//        expected.setName("product");
//        expected.setPrice(111);
//        expected.setCreationDate(LocalDateTime.now());
//
//        jdbcProductDao.add(expected);
//
//
//        Product actual = jdbcProductDao.get("product");
//        assertEquals(expected.getName(), actual.getName());
//        assertEquals(expected.getPrice(), actual.getPrice());
//    }
//
//    @Test
//    void add() {
//       int beforeSize =  jdbcProductDao.getAll().size();
//        Product expected = new Product();
//        expected.setName("product");
//        expected.setPrice(111);
//        expected.setCreationDate(LocalDateTime.now());
//
//        jdbcProductDao.add(expected);
//        int aftersize = jdbcProductDao.getAll().size();
//        assertTrue(aftersize>beforeSize);
//        assertEquals(aftersize, beforeSize + 1);
//
//    }
//
//    @Test
//    void remove() {
//        int beforeSize =  jdbcProductDao.getAll().size();
//        Product expected = new Product();
//        expected.setName("product");
//        expected.setPrice(111);
//        expected.setCreationDate(LocalDateTime.now());
//
//        jdbcProductDao.add(expected);
//        jdbcProductDao.remove(1);
//        int aftersize = jdbcProductDao.getAll().size();
//
//        assertEquals(aftersize, beforeSize);
//    }
//
//    @Test
//    void update() {
//        Product expected = new Product();
//        expected.setName("productTOupdate");
//        expected.setPrice(111);
//        expected.setCreationDate(LocalDateTime.now());
//
//        jdbcProductDao.add(expected);
//
//        Product actual = jdbcProductDao.get("productTOupdate");
//        actual.setName("updated");
//        actual.setPrice(222);
//
//        jdbcProductDao.update(actual);
//
//        Product updated = jdbcProductDao.get("updated");
//
//
//
//        assertEquals(actual.getId(), updated.getId());
//        assertEquals(actual.getDescription(), updated.getDescription());
//
//
//    }
//
//    @Test
//    void search() {
//        Product expected = new Product();
//        expected.setName("search");
//        expected.setPrice(111);
//        expected.setCreationDate(LocalDateTime.now());
//
//        jdbcProductDao.add(expected);
//
//        assertEquals(1, jdbcProductDao.search("search").size());
//
//    }
//}