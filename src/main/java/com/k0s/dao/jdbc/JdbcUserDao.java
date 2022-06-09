package com.k0s.dao.jdbc;

import com.k0s.dao.UserDao;
import com.k0s.dao.jdbc.mapper.RoleRowMapper;
import com.k0s.dao.jdbc.mapper.UserRowMapper;
import com.k0s.security.user.Role;
import com.k0s.security.user.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
public class JdbcUserDao implements UserDao {
    private static final String GET_ALL_USERS_QUERY = "SELECT id, username, password FROM users;";
    private static final String GET_USER_BY_NAME_QUERY = "SELECT id, username, password FROM users WHERE username = ?;";
    private static final String GET_USER_ROLES_QUERY = "select users.id, roles.name from users join users_roles on users.id = users_roles.user_id join roles on users_roles.role_id = roles.id where users.username = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRowMapper userRowMapper;

    @Autowired
    private RoleRowMapper roleRowMapper;

    @Autowired
    public JdbcUserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    @SneakyThrows
    public Optional<User> findByUsername(String username) {
        List<User> userList = jdbcTemplate.query(GET_USER_BY_NAME_QUERY, userRowMapper, username);

        if (userList.isEmpty()) {
            return Optional.empty();
        }
        return userList.stream().findFirst();

    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL_USERS_QUERY, userRowMapper);
    }


    public List<Role> getUserRolesByUsername(String username) {
        return jdbcTemplate.query(GET_USER_ROLES_QUERY, roleRowMapper, username);
    }

}
