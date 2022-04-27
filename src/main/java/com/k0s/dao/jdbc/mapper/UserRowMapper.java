package com.k0s.dao.jdbc.mapper;


import com.k0s.security.user.Role;
import com.k0s.security.user.User;
import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {

    public static User mapRow(@NonNull ResultSet resultSet) throws SQLException {

        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .password(resultSet.getString("password"))
                .salt(resultSet.getString("salt"))
                .role(Role.getRole(resultSet.getString("role")))
                .build();
    }
}

