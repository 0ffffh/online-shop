package com.k0s.dao.jdbc;

import com.k0s.dao.Dao;
import com.k0s.dao.UserDao;
import com.k0s.entity.user.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
//public class JdbcUserDao implements Dao<User> {
public class JdbcUserDao implements UserDao {
    private static final String GET_USER_BY_NAME_QUERY = "SELECT id, name, password, salt, role FROM users WHERE name = ?;";

    private final UserRowMapper userRowMapper = new UserRowMapper();
    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows
    @Override
    public User get(String name) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_NAME_QUERY)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
//                    throw new RuntimeException("User not found");
                    return null;
                }
//                User user = userRowMapper.mapRow(resultSet);
//                if (!resultSet.next()) {
//                    throw new RuntimeException("More than on User found");
////                    return null;
//                }
                return userRowMapper.mapRow(resultSet);
//                return user;
            }

        }
//        catch (SQLException e) {
//            throw new RuntimeException("UserDao get user error " + e);
//        }
    }
}
