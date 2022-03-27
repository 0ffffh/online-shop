package com.k0s.dao.jdbc;

import com.k0s.dao.UserDao;
import com.k0s.entity.user.User;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class JdbcUserDao implements UserDao {
    private static final String GET_USER_QUERY = "SELECT * FROM users WHERE name = ?;";

    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User getUser(String name) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_QUERY)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    log.info("User {} not found in database", name);
                    throw new RuntimeException("Users not found");
                }
                return UserRowMapper.mapRow(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.info("Get user error {}", e.getMessage());
            throw new RuntimeException("Get user error " + e.getMessage());
        }
    }
}
