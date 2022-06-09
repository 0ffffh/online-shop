package com.k0s.dao.jdbc.mapper;


import com.k0s.security.user.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return new Role(resultSet.getLong("id"),
                resultSet.getString("name"));
    }
}

