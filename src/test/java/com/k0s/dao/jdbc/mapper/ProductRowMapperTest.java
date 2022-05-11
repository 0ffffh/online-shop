package com.k0s.dao.jdbc.mapper;

import com.k0s.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductRowMapperTest {


    @Test
    void mapRow() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);


        when(resultSet.getString("name")).thenReturn("pen");
        when(resultSet.getString("description")).thenReturn("black pen");
        when(resultSet.getLong(any())).thenReturn(Long.valueOf(1));
        when(resultSet.getDouble(any())).thenReturn(111.1);
        when(resultSet.getTimestamp(any())).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        ProductRowMapper productRowMapper = new ProductRowMapper();

        Product product = productRowMapper.mapRow(resultSet, 0);

        assertEquals(resultSet.getString("name"), product.getName());
        assertEquals(resultSet.getString("description"), product.getDescription());
        assertEquals(resultSet.getLong("id"), product.getId());
        assertEquals(resultSet.getDouble("price"), product.getPrice());
        assertEquals(resultSet.getTimestamp("creation_date").toLocalDateTime(), product.getCreationDate());

    }
}