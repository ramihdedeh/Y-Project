package com.yplatform.dao;

import com.yplatform.model.User;
import com.yplatform.repository.SQLiteDBManager;

import java.sql.*;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAOImpl implements UserDAO {
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(SQLiteDBManager.getJdbcUrl());
    }

    @Override
    public boolean addUser(User user){
        String sql = "INSERT INTO users (email, first_name, last_name, date_of_birth, username, salt, password) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Hash the password before storing it in the database
            String salt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setDate(4, new java.sql.Date(user.getDateOfBirth().getTime()));
            pstmt.setString(5, user.getUsername());
            pstmt.setString(6, salt);
            pstmt.setString(7, hashedPassword); // Use the hashed password
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
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
                        rs.getString("salt"),
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
                        rs.getString("salt"),
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
        String sql = "UPDATE users SET first_name = ?, last_name = ?, date_of_birth = ?, username = ?, salt = ?, password = ? WHERE email = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setDate(3, new java.sql.Date(user.getDateOfBirth().getTime()));
            pstmt.setString(4, user.getUsername());
            pstmt.setString(5, user.getSalt());
            pstmt.setString(6, user.getPassword());
            pstmt.setString(7, user.getEmail());
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

    @Override
    public boolean checkUsernameExists(String username) throws Exception {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new Exception("Error checking username existence", e);
        }
        return false;
    }

    public boolean verifyPassword(String username, String hashedPassword) throws Exception {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                // Compare the hashed password from the front-end with the stored hashed password
                return storedHashedPassword.equals(hashedPassword);
            }

        } catch (SQLException e) {
            throw new Exception("Error verifying password", e);
        }
        return false;
    }
    public String getUsernameById(Long userId) {
        String username = null;
        String sql = "SELECT username FROM users WHERE user_id = ?";
        
        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setLong(1, userId);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    username = resultSet.getString("username");
                }
            }
    
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace(); // Log the exception or perform other error handling
        }
    
        return username;
    }

    }


