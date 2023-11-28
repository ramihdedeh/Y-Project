package com.yplatform.service;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

import com.yplatform.dao.UserDAOImpl;
import com.yplatform.model.User;

public class UserService {

    private static UserDAOImpl userDAO = new UserDAOImpl();


    public static boolean addUser(User user) {
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

    public static boolean authenticateUser(String username, String password) {
        try {
            // Retrieve the user from the database
            Optional<User> userOptional = userDAO.getUserByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user != null) {
                    // Hash the provided password with the retrieved salt
                    String hashedPassword = BCrypt.hashpw(password, user.getSalt());
                    System.out.println(hashedPassword);
                    if (userDAO.checkUsernameExists(username)) {
                        // If the username exists, proceed to verify the password
                        if (userDAO.verifyPassword(username, hashedPassword)) {
                            System.out.println("User authenticated");
                            return true;
                        }
                    }
                }
            }

        } catch (Exception e) {
            // Handle exception, log it, etc.

        }
        return false; // Authentication failed
    }
}


