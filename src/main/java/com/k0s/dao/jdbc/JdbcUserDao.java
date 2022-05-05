package com.k0s.dao.jdbc;

import com.k0s.dao.UserDao;
import com.k0s.dao.jdbc.mapper.UserRowMapper;
import com.k0s.security.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;


@Slf4j
public class JdbcUserDao implements UserDao {
    private static final String GET_USER_BY_NAME_QUERY = "SELECT id, name, password, salt, role FROM users WHERE name = ?;";
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User get(String name) {
        List<User> userList =  jdbcTemplate.query(GET_USER_BY_NAME_QUERY, new UserRowMapper(), name);
        if(userList.isEmpty()){
            return null;
        }
        return userList.stream().findFirst().get();

    }
}
