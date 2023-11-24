package com.yplatform.dao;

import com.yplatform.model.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDAO {
    void addUser(User user) throws Exception;

    Optional<User> getUserByUsername(String username) throws Exception;

    Optional<User> getUserByEmail(String email) throws Exception;

    void updateUser(User user) throws Exception;

    void deleteUser(User user) throws Exception;

    boolean checkUsernameExists(String username) throws Exception;

    boolean verifyPassword(String username, String password) throws Exception;

    String getUsernameById(Long userId) throws SQLException;
    
}

