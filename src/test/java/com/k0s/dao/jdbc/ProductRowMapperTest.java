package com.k0s.dao.jdbc;

import com.k0s.entity.Product;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProductRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getString("name")).thenReturn("pen");
        when(resultSet.getString("description")).thenReturn("black pen");
        when(resultSet.getLong(any())).thenReturn(Long.valueOf(1));
        when(resultSet.getDouble(any())).thenReturn(111.1);
        when(resultSet.getTimestamp(any())).thenReturn(Timestamp.valueOf(now));

        Product product = ProductRowMapper.mapRow(resultSet);

        assertEquals(resultSet.getString("name"), product.getName());
        assertEquals(resultSet.getString("description"), product.getDescription());
        assertEquals(resultSet.getLong("id"), product.getId());
        assertEquals(resultSet.getDouble("price"), product.getPrice());
        assertEquals(resultSet.getTimestamp("creation_date").toLocalDateTime(), product.getCreationDate());

    }

    @Test
    void mapRowNull() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getString("name")).thenReturn(null);
        when(resultSet.getString("description")).thenReturn(null);
        when(resultSet.getLong(any())).thenReturn((long)0);
        when(resultSet.getDouble(any())).thenReturn((double)0);
        when(resultSet.getTimestamp(any())).thenReturn(Timestamp.valueOf(now));

        Product product = ProductRowMapper.mapRow(resultSet);
        System.out.println(product);

        assertEquals(resultSet.getString("name"), product.getName());
        assertEquals(resultSet.getString("description"), product.getDescription());
        assertEquals(resultSet.getLong("id"), product.getId());
        assertEquals(resultSet.getDouble("price"), product.getPrice());
        assertEquals(resultSet.getTimestamp("creation_date").toLocalDateTime(), product.getCreationDate());

    }
}