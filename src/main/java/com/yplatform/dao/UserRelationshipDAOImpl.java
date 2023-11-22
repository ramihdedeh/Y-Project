package com.yplatform.dao;

import com.yplatform.model.User;
import com.yplatform.repository.SQLiteDBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRelationshipDAOImpl implements UserRelationshipDAO{
    private static final String URL = SQLiteDBManager.getJdbcUrl(); // Adjust the URL based on your SQLite database location

    @Override
    public void addFollower(Long user_id, Long followed_user_id) {
        try (Connection connection = DriverManager.getConnection(URL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_relationships (user_id, followed_user_id) VALUES (?, ?)")) {
            preparedStatement.setLong(1, user_id);
            preparedStatement.setLong(2, followed_user_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public void removeFollower(Long user_id, Long followed_user_id) {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_relationships WHERE user_id = ? AND followed_user_id = ?")) {
            preparedStatement.setLong(1, user_id);
            preparedStatement.setLong(2, followed_user_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public List<User> getFollowers(Long user_id) {
        List<User> followers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT u.* FROM users u JOIN user_relationships r ON u.user_id = r.followed_user_id WHERE r.user_id = ?")) {
            preparedStatement.setLong(1, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User follower = new User();
                    follower.setId(resultSet.getLong("user_id"));
                    follower.setUsername(resultSet.getString("username"));
                    // Set other properties as needed
                    followers.add(follower);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return followers;
    }
}
