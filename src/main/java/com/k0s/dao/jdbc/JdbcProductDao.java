package com.k0s.dao.jdbc;

import com.k0s.dao.ProductDao;
import com.k0s.dao.jdbc.mapper.ProductRowMapper;
import com.k0s.entity.Product;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcProductDao implements ProductDao {

    private static final String GET_ALL_QUERY = "SELECT id, name, price, creation_date, description FROM products";
    private static final String GET_PRODUCT_BY_ID_QUERY = "SELECT id, name, price, creation_date, description FROM products WHERE id = ?";
    private static final String GET_PRODUCT_BY_NAME_QUERY = "SELECT id, name, price, creation_date, description FROM products WHERE name = ?";
    private static final String ADD_PRODUCT_QUERY = "INSERT INTO products (name, price, creation_date, description) values (?, ?, ?, ?);";
    private static final String REMOVE_PRODUCT_QUERY = "DELETE FROM products WHERE id = ?;";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE products SET name = ?, price = ?, creation_date = ?, description = ?  WHERE id = ?;";
    private static final String SEARCH_PRODUCT_QUERY = "SELECT id, name, price, creation_date, description FROM products WHERE LOWER(name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?)";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRowMapper productRowMapper;
    @Autowired
    public JdbcProductDao( DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Product> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, productRowMapper);
    }

    @Override
    public Optional<Product> get(long id) {
        return Optional.of(jdbcTemplate.queryForObject(GET_PRODUCT_BY_ID_QUERY, productRowMapper, id));
    }

    @Override
    public Product get(String name) {
        List<Product> productList =  jdbcTemplate.query(GET_PRODUCT_BY_NAME_QUERY, productRowMapper, name);
        if(productList.isEmpty()){
            return null;
        }
        return productList.stream().findFirst().get();
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
        return jdbcTemplate.query(SEARCH_PRODUCT_QUERY, productRowMapper, search, search);
    }

}
