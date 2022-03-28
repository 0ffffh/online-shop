package com.k0s.dao.jdbc;

import com.k0s.dao.ProductDao;
import com.k0s.entity.Product;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcProductDao implements ProductDao<Product> {
    private static final String GET_ALL_QUERY = "SELECT * FROM products";
    private static final String GET_PRODUCT_QUERY = "SELECT * FROM products WHERE id = ?";
    private static final String ADD_PRODUCT_QUERY = "INSERT INTO products (name, price, creation_date, description) values (?, ?, ?, ?);";
    private static final String REMOVE_PRODUCT_QUERY = "DELETE FROM products WHERE id = ?;";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE products SET name = ?, price = ?, creation_date = ?, description = ?  WHERE id = ?;";
    private static final String SEARCH_PRODUCT_QUERY = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?)";

    private final DataSource dataSource;

    public JdbcProductDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> getAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Product> productList = new ArrayList<>();
            while (resultSet.next()) {
                productList.add(ProductRowMapper.mapRow(resultSet));
            }
            return productList;
        } catch (SQLException e) {
            log.error("Get product list error: ",e);
            throw new RuntimeException("Get product list error  " + e.getMessage());
        }
    }

    @Override
    public Product get(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_QUERY)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    log.info("Product id={} not found", id);
                    throw new RuntimeException("Product id = " + id + " not found");
                }
                return ProductRowMapper.mapRow(resultSet);
            }
        } catch (SQLException e) {
            log.error("Get product id={} error: {}", id, e);
            throw new RuntimeException("Get product error " + e.getMessage());
        }
    }

    @Override
    public void add(Product product) {
        if(product == null){
            log.info("Add error, product can't be NULL");
            throw new NullPointerException("Add error, product can't be NULL");
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_PRODUCT_QUERY)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(product.getCreationDate()));
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Add product error: ", e);
            throw new RuntimeException("Add product error " + e.getMessage());
        }
    }

    @Override
    public void remove(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_PRODUCT_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Remove product id = {}, error: {}", id, e);
            throw new RuntimeException("Remove product id = " + id + " error " + e.getMessage());
        }
    }

    @Override
    public void update(Product product) {
        if(product == null){
            log.info("Update error, product can't be NULL");
            throw new NullPointerException("Update error, product can't be NULL");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_QUERY)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(product.getCreationDate()));
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.setLong(5, product.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Update product error: ", e);
            throw new RuntimeException("Update product error " + e.getMessage());
        }
    }

    @Override
    public List<Product> search(String value) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_PRODUCT_QUERY)) {
            preparedStatement.setString(1, "%" + value + "%");
            preparedStatement.setString(2, "%" + value + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Product> productList = new ArrayList<>();
                while (resultSet.next()) {
                    productList.add(ProductRowMapper.mapRow(resultSet));
                }
                return productList;
            }
        } catch (SQLException e) {
            log.error("Search product {} error {}", value, e);
            throw new RuntimeException("Search products error " + e);
        }
    }
}
