package com.k0s.dao.jdbc;


import com.k0s.entity.user.User;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getString("name")).thenReturn("name");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("salt")).thenReturn("salt");
        when(resultSet.getString("role")).thenReturn("admin");
        when(resultSet.getLong(any())).thenReturn(Long.valueOf(1));


        UserRowMapper userRowMapper = new UserRowMapper();
        User user  = userRowMapper.mapRow(resultSet);

        assertEquals(resultSet.getString("name"), user.getName());
        assertEquals(resultSet.getString("password"), user.getPassword());
        assertEquals(resultSet.getString("salt"), user.getSalt());
        assertEquals(resultSet.getString("role"), user.getRole().getRole());
        assertEquals(resultSet.getLong("id"), user.getId());
    }
}