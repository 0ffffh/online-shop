package com.k0s.dao.jdbc;

import com.k0s.dao.ProductDao;
import com.k0s.dao.jdbc.mapper.ProductRowMapper;
import com.k0s.entity.Product;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Slf4j
public class JdbcProductDao implements ProductDao {

    private static final String GET_ALL_QUERY = "SELECT id, name, price, creation_date, description FROM products";
    private static final String GET_PRODUCT_BY_ID_QUERY = "SELECT id, name, price, creation_date, description FROM products WHERE id = ?";
    private static final String ADD_PRODUCT_QUERY = "INSERT INTO products (name, price, creation_date, description) values (?, ?, ?, ?);";
    private static final String REMOVE_PRODUCT_QUERY = "DELETE FROM products WHERE id = ?;";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE products SET name = ?, price = ?, creation_date = ?, description = ?  WHERE id = ?;";
    private static final String SEARCH_PRODUCT_QUERY = "SELECT id, name, price, creation_date, description FROM products WHERE LOWER(name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?)";

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }


    @Override
    public List<Product> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new ProductRowMapper());
    }


    @Override
    public Product get(long id) {
        return jdbcTemplate.queryForObject(GET_PRODUCT_BY_ID_QUERY, new ProductRowMapper(), id);
    }



    @Override
    public void add(@NonNull Product product) {
        jdbcTemplate.update(ADD_PRODUCT_QUERY, product.getName(), product.getPrice(),
                Timestamp.valueOf(product.getCreationDate()), product.getDescription());
    }


    @Override
    public void remove(long id) {
        jdbcTemplate.update(REMOVE_PRODUCT_QUERY, id);
    }


    @Override
    public void update(@NonNull Product product) {
        jdbcTemplate.update(UPDATE_PRODUCT_QUERY, product.getName(), product.getPrice(),
                Timestamp.valueOf(product.getCreationDate()),  product.getDescription(), product.getId());
    }


    @Override
    public List<Product> search(String value) {
        String search = "%" + value + "%";
        return jdbcTemplate.query(SEARCH_PRODUCT_QUERY, new ProductRowMapper(), search, search);
    }

}
