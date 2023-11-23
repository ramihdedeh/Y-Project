package com.yplatform.service;

import com.yplatform.dao.UserDAOImpl;
import com.yplatform.model.User;

public class UserService {

    private UserDAOImpl userDAO = new UserDAOImpl();


    public boolean addUser(User user) {
        try {
            // Check if the username already exists in the database
            if (userDAO.checkUsernameExists(user.getUsername())) {
                // If the username exists, return false
                return false;
            }
            // Proceed to add the user as they do not exist in the database
            userDAO.addUser(user);
            return true;
        } catch (Exception e) {
            // Log the exception or handle it as per your error handling policy
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateUser(String username, String hashedPassword) {
        try {
            if (userDAO.checkUsernameExists(username)) {
                // If the username exists, proceed to verify the password
                if (userDAO.verifyPassword(username, hashedPassword)) {
                    System.out.println("User authenticated");
                    return true;
                }
            }
        } catch (Exception e) {
            // Handle exception, log it, etc.
        }
        return false; // Authentication failed
    }
}
