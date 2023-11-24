package com.yplatform.server;

import com.yplatform.dao.PostDAO;
import com.yplatform.dao.UserDAO;
import com.yplatform.dao.UserRelationshipDAO;
import com.yplatform.model.User;
import com.yplatform.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
//import com.yplatform.model.Post;
//import com.yplatform.model.User;


import java.io.*;
import java.net.Socket;
import java.util.Date;
//import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final UserDAO userDAO;
    private final PostDAO postDAO;
    private final UserRelationshipDAO userRelationshipDAO;

    public ClientHandler(Socket clientSocket, UserDAO userDAO, PostDAO postDAO, UserRelationshipDAO userRelationshipDAO) {
        this.clientSocket = clientSocket;
        this.userDAO = userDAO;
        this.postDAO = postDAO;
        this.userRelationshipDAO = userRelationshipDAO;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            // Handle client communication here
            // You can use reader and writer to send/receive messages
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                String[] tokens = inputLine.split(" ");
                String command = tokens[0].toUpperCase();
                switch (command) {
                    case "SIGN UP":
                        handleSignup(tokens, writer);
                        break;
                    case "LOGIN":
                        handleLogin(tokens, writer);
                        break;
                    // Other cases for different functionalities
                    default:
                        writer.write("Unknown command.\n");
                        writer.flush();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add methods for handling user registration, login, and other functionalities
    public void handleLogin(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 3) {
            String username = tokens[1];
            String password = tokens[2]; // The plaintext password from the user

            // Hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            if (UserService.authenticateUser(username, hashedPassword)){
                writer.write("Login successful.\n");
            } else {
                writer.write("Login failed: Incorrect username or password.\n");
            }
        } else {
            writer.write("Login failed: Invalid number of arguments.\n");
        }
        writer.flush();
    }
    public void handleSignup(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 7) {
            String email = tokens[1];
            String firstName = tokens[2];
            String lastName = tokens[3];
            String dateOfBirth = tokens[4];
            String username = tokens[5];
            String password = tokens[6]; // The plaintext password from the user

            // Hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Create a new user object
            User user = new User(email, firstName, lastName, new Date(), username, hashedPassword);

            // Add the user to the database
            if (UserService.addUser(user)) {
                writer.write("User added successfully.\n");
            } else {
                writer.write("User could not be added.\n");
            }
        } else {
            writer.write("Invalid number of arguments.\n");
        }
        writer.flush();
    }




}


