package com.k0s.dao.jdbc;


import com.k0s.entity.user.Role;
import com.k0s.entity.user.User;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class UserRowMapper {

    public User mapRow(@NonNull ResultSet resultSet) throws SQLException {

        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .password(resultSet.getString("password"))
                .salt(resultSet.getString("salt"))
                .role(Role.getRole(resultSet.getString("role")))
                .build();
    }
}

