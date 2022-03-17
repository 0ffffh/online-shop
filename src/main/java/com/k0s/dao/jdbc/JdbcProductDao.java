package com.k0s.dao.jdbc;

import com.k0s.dao.ProductDao;
import com.k0s.entity.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao implements ProductDao<Product> {
    private static final String GET_ALL_QUERY = "SELECT * FROM products";
    private static final String GET_PRODUCT_QUERY = "SELECT * FROM products WHERE id = ?";
    private static final String ADD_PRODUCT_QUERY = "INSERT INTO products (name, price, creation_date, description) values (?, ?, ?, ?);";
    private static final String REMOVE_PRODUCT_QUERY = "DELETE FROM products WHERE id = ?;";
    private static final String EDIT_PRODUCT_QUERY = "UPDATE products SET name = ?, price = ?, description = ? WHERE id = ?;";
    private static final String SEARCH_PRODUCT_QUERY = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ?";

    private final DataSource dataSource;
    private final ProductRowMapper productRowMapper = new ProductRowMapper();

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
                productList.add(productRowMapper.mapRow(resultSet));
            }
            return productList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Get products error " + e);
        }
    }

    @Override
    public Product get(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("Product no found");
            }

            Product product = productRowMapper.mapRow(resultSet);

            if (resultSet.next()) {
                throw new IllegalStateException("More than one product found");
            }
            return product;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Get product error " + e);
        }
    }

    @Override
    public void add(Product product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_PRODUCT_QUERY)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Add product error " + e);
        }
    }

    @Override
    public void remove(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_PRODUCT_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Remove product error " + e);
        }
    }

    @Override
    public void update(Product product) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_PRODUCT_QUERY)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setLong(4, product.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Update product error " + e);
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
                    productList.add(productRowMapper.mapRow(resultSet));
                }
                return productList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Search products error " + e);
        }
    }
}
