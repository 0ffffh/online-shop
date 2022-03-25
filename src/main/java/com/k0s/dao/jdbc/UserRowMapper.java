package com.k0s.dao.jdbc;


import com.k0s.entity.user.Role;
import com.k0s.entity.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {

    public static User mapRow(ResultSet resultSet) throws SQLException {

        return new User(resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("password"),
                resultSet.getString("salt"),
                Role.getRole(resultSet.getString("role")));
    }
}

