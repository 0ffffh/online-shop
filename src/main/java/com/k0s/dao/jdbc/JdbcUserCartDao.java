package com.k0s.dao.jdbc;

import com.k0s.dao.UserCartDao;
import com.k0s.dao.jdbc.mapper.ProductRowMapper;
import com.k0s.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcUserCartDao implements UserCartDao {

    private static final String ADD_PRODUCT_TO_CART = "INSERT INTO product_cart (username, product_id) VALUES (?, ?);";
    private static final String GET_PRODUCT_CART = "select p.id, p.name, p.price, p.creation_date, p.description from products p join product_cart pc on p.id=pc.product_id where pc.username = ?;";
    private static final String REMOVE_PRODUCT_FROM_CART = "DELETE FROM product_cart WHERE ctid = (SELECT ctid FROM product_cart WHERE username = ? AND product_id = ? LIMIT 1);";
    private static final String CLEAR_CART = "DELETE FROM product_cart WHERE username = ?;";



    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRowMapper productRowMapper;
    @Autowired
    public JdbcUserCartDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void addToCart(String name, Long productId) {
        jdbcTemplate.update(ADD_PRODUCT_TO_CART, name, productId);
    }

    @Override
    public List<Product> getProductCart(String name) {
        return jdbcTemplate.query(GET_PRODUCT_CART, productRowMapper, name);
    }

    @Override
    public void removeProduct(String username, Long productId) {
        jdbcTemplate.update(REMOVE_PRODUCT_FROM_CART, username, productId);
    }

    @Override
    public void clearCart(String username) {
        jdbcTemplate.update(CLEAR_CART, username);
    }

}
