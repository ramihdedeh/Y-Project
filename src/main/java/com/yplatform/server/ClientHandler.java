package com.yplatform.server;

import com.yplatform.dao.PostDAO;
import com.yplatform.dao.UserDAO;
import com.yplatform.dao.UserRelationshipDAO;
import com.yplatform.model.Post;
import com.yplatform.model.User;
import com.yplatform.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
//import com.yplatform.model.Post;
//import com.yplatform.model.User;
import com.yplatform.service.PostService;
import com.yplatform.service.UserRelationshipService;

import java.io.*;
import java.net.Socket;
import java.util.Date;
//import java.util.List;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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
                    case "POSTMESSAGE":
                        handlePostCommand(tokens, writer);
                        break;
                    case "MYPOSTS":
                        displayUserPosts(tokens, writer);
                        break;
                    case "INTERESTPOSTS":
                        displayInterestPosts(tokens, writer);
                        break;
                    case "UPDATEPOST":
                        handleUpdatePostCommand(tokens, writer);
                        break;
                    case "DELETEPOST":
                        handleDeletePostCommand(tokens, writer);
                        break;
                    case "SIGN UP":
                        handleSignup(tokens, writer);
                        break;
                    case "LOGIN":
                        handleLogin(tokens, writer);
                        break;
                    case "ADDFOLLOWER":
                        handleAddFollower(tokens, writer);
                        break;
                    case "REMOVEFOLLOWER":
                        handleRemoveFollower(tokens, writer);
                        break;
                    case "GETFOLLOWERS":
                        handleGetFollowers(tokens, writer);
                        break;
                    // Other cases for different functionalities
                    default:
                        // Handle unknown command
                        writer.write("Unknown command: " + command + "\n");
                        writer.flush();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add methods for handling user registration, login, and other functionalities
    private void handlePostCommand(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 4){
            String title = tokens[1];
            String content = tokens[2];
            String user_id = tokens[3];
        // Check if user_id is a valid long value
            try {
                Long userId = Long.parseLong(user_id);

                // Create a Post object with the extracted information
                Post post = new Post(title, content, userId, new Timestamp(System.currentTimeMillis()));

                // Use the PostService to add the post
                if (PostService.addPost(post)) {
                    writer.write("Message posted successfully!");
                } else {
                    writer.write("Failed to post the message. Please try again.");
                }

                writer.newLine();
                writer.flush();
            } catch (NumberFormatException e) {
                // Invalid user_id format, inform the client
                writer.write("Invalid user_id format. Please provide a valid numeric user_id.");
                writer.newLine();
                writer.flush();
            }
        }
        else {
        // Invalid command format, inform the client
        writer.write("Invalid POSTMESSAGE command format. Usage: POSTMESSAGE Title Content User_ID");
        writer.newLine();
        writer.flush();
        return;
        }
    }

    private void displayUserPosts(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 2) {
            try {
                Long userId = Long.parseLong(tokens[1]);
                List<Post> userPosts = postDAO.getPostsByUserId(userId);

                // Convert the list of posts to JSON
                String jsonPosts = PostService.convertPostsListToJson(userPosts);

                // Send JSON response to the client
                writer.write(jsonPosts);
                writer.newLine();
                writer.flush();
            } catch (NumberFormatException e) {
                // Invalid user ID, inform the client
                writer.write("Invalid user ID in the command.");
                writer.newLine();
                writer.flush();
            }
        } else {
            // Invalid command format, inform the client
            writer.write("Invalid MYPOSTS command format. Usage: MYPOSTS User_ID");
            writer.newLine();
            writer.flush();
        }
    }

    private void displayInterestPosts(String[] tokens, BufferedWriter writer) throws IOException {
        // Implement logic to get and display posts made by the user
        if (tokens.length == 2) {
            try {
                Long userId = Long.parseLong(tokens[1]);
                List<Post> userPosts = postDAO.getPostsOfInterest(userId);

                // Convert the list of posts to JSON
                String jsonPosts = PostService.convertPostsListToJson(userPosts);

                // Display posts from followed users to the client
                writer.write(jsonPosts);
                writer.newLine();
                writer.flush();
            } catch (NumberFormatException e) {
                // Invalid user ID, inform the client
                writer.write("Invalid user ID in the command.");
                writer.newLine();
                writer.flush();
            }
        }
        else {
            // Invalid command format, inform the client
            writer.write("Invalid INTERESTPOSTS command format. Usage: INTERESTPOSTS User_ID");
            writer.newLine();
            writer.flush();
            return;
        }
    }
    private void handleUpdatePostCommand(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 5) {
            try {
                Long postId = Long.parseLong(tokens[1]);
                String title = tokens[2];
                String content = tokens[3];
                String user_id = tokens[4];
                Long userId = Long.parseLong(user_id);

                Post updatedPost = new Post(postId, title, content, userId, new Timestamp(System.currentTimeMillis()));

                if (PostService.updatePost(updatedPost)) {
                    writer.write("Post updated successfully!");
                } else {
                    writer.write("Failed to update the post. Please try again.");
                }

                writer.newLine();
                writer.flush();
            } catch (NumberFormatException e) {
                // Invalid post ID or user ID format, inform the client
                writer.write("Invalid post ID or user ID format. Please provide valid numeric values.");
                writer.newLine();
                writer.flush();
            }
        } else {
            // Invalid command format, inform the client
            writer.write("Invalid UPDATEPOST command format. Usage: UPDATEPOST Post_ID Title Content User_ID");
            writer.newLine();
            writer.flush();
        }
    }

    private void handleDeletePostCommand(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 2) {
            try {
                Long postId = Long.parseLong(tokens[1]);

                if (PostService.deletePost(postId)) {
                    writer.write("Post deleted successfully!");
                } else {
                    writer.write("Failed to delete the post. Please try again.");
                }

                writer.newLine();
                writer.flush();
            } catch (NumberFormatException e) {
                // Invalid post ID format, inform the client
                writer.write("Invalid post ID format. Please provide a valid numeric post ID.");
                writer.newLine();
                writer.flush();
            }
        } else {
            // Invalid command format, inform the client
            writer.write("Invalid DELETEPOST command format. Usage: DELETEPOST Post_ID");
            writer.newLine();
            writer.flush();
        }
    }

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
        String dateOfBirthString = tokens[4];
        String username = tokens[5];
        String password = tokens[6]; // The plaintext password from the user

        try {
            // Parse the date string into a Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = dateFormat.parse(dateOfBirthString);

            // Hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Create a new user object
            User user = new User(email, firstName, lastName, dateOfBirth, username, hashedPassword);

            // Add the user to the database
            if (UserService.addUser(user)) {
                writer.write("User added successfully.\n");
            } else {
                writer.write("User could not be added.\n");
            }
        } catch (ParseException e) {
            writer.write("Invalid date format. Please use the format yyyy-MM-dd.\n");
        }
    } else {
        writer.write("Invalid number of arguments.\n");
    }
    writer.flush();
    }
    private void handleAddFollower(String[] tokens, BufferedWriter writer) throws IOException {
    if (tokens.length == 3) {
        try {
            Long userId = Long.parseLong(tokens[1]);
            Long followedUserId = Long.parseLong(tokens[2]);
            if (UserRelationshipService.addFollower(userId, followedUserId)) {
                writer.write("Follower added successfully!");
            } else {
                writer.write("Failed to add follower. Please try again.");
            }
        } catch (NumberFormatException e) {
            writer.write("Invalid user IDs. Please provide valid numeric values.");
        }
    } else {
        writer.write("Invalid ADDFOLLOWER command format. Usage: ADDFOLLOWER User_ID Followed_User_ID");
    }
    writer.newLine();
    writer.flush();
}

private void handleRemoveFollower(String[] tokens, BufferedWriter writer) throws IOException {
    if (tokens.length == 3) {
        try {
            Long userId = Long.parseLong(tokens[1]);
            Long followedUserId = Long.parseLong(tokens[2]);
            if (UserRelationshipService.removeFollower(userId, followedUserId)) {
                writer.write("Follower removed successfully!");
            } else {
                writer.write("Failed to remove follower. Please try again.");
            }
        } catch (NumberFormatException e) {
            writer.write("Invalid user IDs. Please provide valid numeric values.");
        }
    } else {
        writer.write("Invalid REMOVEFOLLOWER command format. Usage: REMOVEFOLLOWER User_ID Followed_User_ID");
    }
    writer.newLine();
    writer.flush();
    }
    private void handleGetFollowers(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 2) {
            try {
                Long userId = Long.parseLong(tokens[1]);
                writer.write(UserRelationshipService.convertUsersListToJson(userRelationshipDAO.getFollowers(userId)));
                writer.newLine();
                writer.flush();
            } catch (NumberFormatException e) {
                writer.write("Invalid user ID. Please provide a valid numeric user ID.");
                writer.newLine();
                writer.flush();
            }
        } else {
            writer.write("Invalid GETFOLLOWERS command format. Usage: GETFOLLOWERS User_ID");
            writer.newLine();
            writer.flush();
        }
    }
}


