package com.yplatform.service;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

import com.yplatform.dao.UserDAOImpl;
import com.yplatform.model.User;

public class UserService {

    private static UserDAOImpl userDAO = new UserDAOImpl();


    public static long addUser(User user) {
        try {
            // Check if the username already exists in the database
            if (userDAO.checkUsernameExists(user.getUsername())) {
                // If the username exists, return false
                //System.out.println("User aleready exists");
                return -1;
            }
            // Proceed to add the user as they do not exist in the database
            //System.out.println("User doesn't exist");
            userDAO.addUser(user);
            Optional <User> userOptional = userDAO.getUserByUsername(user.getUsername());
            if (userOptional.isPresent()) {
                User user1 = userOptional.get();
                return user1.getId();
            }
            return -1;
        } catch (Exception e) {
            // Log the exception or handle it as per your error handling policy
            e.printStackTrace();
            return -1;
        }
    }

    public static long authenticateUser(String username, String password) {
        try {
            // Retrieve the user from the database
            Optional<User> userOptional = userDAO.getUserByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user != null) {
                    // Hash the provided password with the retrieved salt
                    String hashedPassword = BCrypt.hashpw(password, user.getSalt());
                    //System.out.println(hashedPassword);
                    if (userDAO.checkUsernameExists(username)) {
                        // If the username exists, proceed to verify the password
                        if (userDAO.verifyPassword(username, hashedPassword)) {
                            System.out.println("User authenticated");
                            return user.getId();
                        }
                    }
                }
            }

        } catch (Exception e) {
            // Handle exception, log it, etc.

        }
        return -1; // Authentication failed
    }
    // Method to search for a user by username
    public static boolean FindUserByUsername(String username) {
        // You can add any additional validation or logic here before calling the DAO method
        try {
            Optional<User> userOptional= userDAO.getUserByUsername(username);
            if (userOptional.isPresent()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}


