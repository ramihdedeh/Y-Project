package com.yplatform.dao;

import com.yplatform.model.User;
import java.util.Optional;

public interface UserDAO {
    void addUser(User user) throws Exception;
    Optional<User> getUserByUsername(String username) throws Exception;
    Optional<User> getUserByEmail(String email) throws Exception;
    void updateUser(User user) throws Exception;
    void deleteUser(User user) throws Exception;
}
