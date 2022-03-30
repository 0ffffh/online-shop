package com.k0s.dao.jdbc;

import com.k0s.entity.Product;
import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductRowMapper {


    public static Product mapRow(@NonNull ResultSet resultSet) throws SQLException {

        return Product.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .price(resultSet.getDouble("price"))
                .creationDate(resultSet.getTimestamp("creation_date").toLocalDateTime())
                .description(resultSet.getString("description"))
                .build();
    }
}
