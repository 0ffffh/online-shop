package com.k0s.dao.jdbc;

import com.k0s.dao.Dao;
import com.k0s.entity.user.User;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class JdbcUserDao implements Dao<User> {
    private static final String GET_USER_BY_NAME_QUERY = "SELECT * FROM users WHERE name = ?;";

    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User get(String name) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_NAME_QUERY)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new RuntimeException("User not found");
                }
                return UserRowMapper.mapRow(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Get user error " + e);
        }
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User get(long id) {
        return null;
    }

    @Override
    public void add(User user) {

    }

    @Override
    public void remove(long id) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public List<User> search(String value) {
        return null;
    }
}
