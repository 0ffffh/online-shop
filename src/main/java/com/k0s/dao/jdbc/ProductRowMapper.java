package com.k0s.dao.jdbc;

import com.k0s.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductRowMapper {

    public static Product mapRow(ResultSet resultSet) throws SQLException {

        return new Product(resultSet.getLong("id"),
                normalize(resultSet.getString("name")),
                resultSet.getDouble("price"),
                resultSet.getTimestamp("creation_date").toLocalDateTime(),
                normalize(resultSet.getString("description")));
    }

    private static String normalize(String result){
        return result == null ? "" : result;
    }
}
