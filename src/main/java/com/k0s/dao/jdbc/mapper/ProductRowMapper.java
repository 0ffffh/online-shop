package com.k0s.dao.jdbc.mapper;

import com.k0s.entity.Product;
import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {

        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        product.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
        product.setDescription(rs.getString("description"));
        return product;
    }
}
