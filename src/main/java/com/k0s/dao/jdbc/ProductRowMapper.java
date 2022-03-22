package com.k0s.dao.jdbc;

import com.k0s.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductRowMapper {

    public ProductRowMapper() {
    }

    public Product mapRow(ResultSet resultSet) throws SQLException {

//        Product product = new Product();
//        product.setId(resultSet.getLong("id"));
//        product.setName(normalize(resultSet.getString("name")));
//        product.setPrice(resultSet.getDouble("price"));
//        product.setDescription(normalize(resultSet.getString("description")));
//        product.setCreationDate(resultSet.getTimestamp("creation_date").toLocalDateTime());
//        return product;

        return new Product(resultSet.getLong("id"),
                normalize(resultSet.getString("name")),
                resultSet.getDouble("price"),
                resultSet.getTimestamp("creation_date").toLocalDateTime(),
                normalize(resultSet.getString("description")));
    }

    private String normalize(String result){
        return result == null ? "" : result;
    }
}
