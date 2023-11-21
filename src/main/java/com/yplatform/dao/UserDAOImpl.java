package com.yplatform.dao;

import com.yplatform.model.User;
import com.yplatform.repository.SQLiteDBManager;

import java.sql.*;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(SQLiteDBManager.getJdbcUrl());
    }

    @Override
    public void addUser(User user) throws Exception {
        String sql = "INSERT INTO users (email, first_name, last_name, date_of_birth, username, password) VALUES(?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setDate(4, new java.sql.Date(user.getDateOfBirth().getTime()));
            pstmt.setString(5, user.getUsername());
            pstmt.setString(6, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error adding user", e);
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        new Date(rs.getLong("date_of_birth")), // Assuming the date is stored as a long
                        rs.getString("username"),
                        rs.getString("password")
                );
                user.setId(rs.getLong("user_id"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new Exception("Error retrieving user by username", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        new Date(rs.getLong("date_of_birth")), // Assuming the date is stored as a long
                        rs.getString("username"),
                        rs.getString("password")
                );
                user.setId(rs.getLong("user_id"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new Exception("Error retrieving user by email", e);
        }
        return Optional.empty();
    }

    @Override
    public void updateUser(User user) throws Exception {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, date_of_birth = ?, username = ?, password = ? WHERE email = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setDate(3, new java.sql.Date(user.getDateOfBirth().getTime()));
            pstmt.setString(4, user.getUsername());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error updating user", e);
        }
    }

    @Override
    public void deleteUser(User user) throws Exception {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error deleting user", e);
        }
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl();
        try {
            userDAO.addUser(new User(
                    "f.noun7@gmail.com","Firas","Noun",
                    new Date(2002,3,17),"firasnoun","123456"));
    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }}
