package com.k0s.dao.jdbc;

import com.k0s.entity.Product;
import com.k0s.entity.user.Role;
import com.k0s.entity.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {

    public UserRowMapper() {
    }

    public User mapRow(ResultSet resultSet) throws SQLException {

        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(Role.getRole(resultSet.getString("role")));

        System.out.println(user);

        return user;
    }
}

