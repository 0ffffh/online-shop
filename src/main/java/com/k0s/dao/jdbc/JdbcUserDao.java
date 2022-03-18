package com.k0s.dao.jdbc;

import com.k0s.dao.UserDao;
import com.k0s.entity.user.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {
//    private static final String GET_USER_QUERY = "SELECT * FROM users WHERE name = ? AND password = ?;";
    private static final String GET_USER_QUERY = "SELECT * FROM users WHERE name = ?;";

    private final DataSource dataSource;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User getUser(String name) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_QUERY)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                if (!resultSet.next()) {
                    throw new RuntimeException("Users not found");
                }

                User user = userRowMapper.mapRow(resultSet);

                if (resultSet.next()) {
                    throw new IllegalStateException("More than one user found");
                }
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Get user error " + e);
        }
    }

//    @Override
//    public User getUser(String name, String password) {
//
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_QUERY)) {
//
//            preparedStatement.setString(1, name);
//            preparedStatement.setString(2, password);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery();) {
//                if (!resultSet.next()) {
//                    throw new RuntimeException("Users not found");
//                }
//
//                User user = userRowMapper.mapRow(resultSet);
//
//                if (resultSet.next()) {
//                    throw new IllegalStateException("More than one user found");
//                }
//                return user;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Get user error " + e);
//        }
//    }
}
