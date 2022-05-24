package com.k0s.dao.jdbc;

import com.k0s.dao.UserDao;
import com.k0s.dao.jdbc.mapper.UserRowMapper;
import com.k0s.security.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Slf4j
@Repository
public class JdbcUserDao implements UserDao {
    private static final String GET_USER_BY_NAME_QUERY = "SELECT id, name, password, salt, role FROM users WHERE name = ?;";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRowMapper userRowMapper;

    public JdbcUserDao(@Autowired DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User get(String name) {
//        List<User> userList =  jdbcTemplate.query(GET_USER_BY_NAME_QUERY, new UserRowMapper(), name);
        List<User> userList =  jdbcTemplate.query(GET_USER_BY_NAME_QUERY, userRowMapper, name);
        if(userList.isEmpty()){
            return null;
        }
        return userList.stream().findFirst().get();

    }
}
