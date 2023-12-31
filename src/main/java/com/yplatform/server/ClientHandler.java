package com.yplatform.server;

import com.almasb.fxgl.app.SystemActions;
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
import java.util.Arrays;
import java.util.Optional;

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
            //System.out.println("Arrived at Client Handler.");
            //System.out.println(reader.readLine());
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
                    case "SIGNUP":
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
                    case "SEARCHFORUSER":
                        handleSearchUser(tokens, writer);
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
        if (tokens.length >= 3){
            //String title = tokens[1];
            String user_id = tokens[1];

            // Concatenate the remaining tokens to reconstruct the content
            String content = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));


            // Check if user_id is a valid long value
            try {
                Long userId = Long.parseLong(user_id);

                // Create a Post object with the extracted information
                Post post = new Post(content, userId, new Timestamp(System.currentTimeMillis()));

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
            writer.write("Invalid POSTMESSAGE command format. Usage: POSTMESSAGE Content User_ID");
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

                writer.write("Success\n");
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
        if (tokens.length == 4) {
            try {
                Long postId = Long.parseLong(tokens[1]);
                //String title = tokens[2];
                String content = tokens[2];
                String user_id = tokens[3];
                Long userId = Long.parseLong(user_id);

                Post updatedPost = new Post(postId, content, userId, new Timestamp(System.currentTimeMillis()));

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
            writer.write("Invalid UPDATEPOST command format. Usage: UPDATEPOST Post_ID Content User_ID");
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

    public String handleLogin(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 3) {
            String username = tokens[1];
            String password = tokens[2]; // The plaintext password from the user
            long user_id = UserService.authenticateUser(username, password);
            if (user_id != -1){
                writer.write("Login successful.\n");
                writer.write(Long.toString(user_id) + "\n");
                writer.flush(); // Make sure to flush the stream
                return username; // Return the username if authenticated
            } else {
                writer.write("Login failed: Incorrect username or password.\n");
                writer.flush(); // Make sure to flush the stream
                return ""; // Return an empty string if authentication fails
            }
        } else {
            writer.write("Login failed: Invalid number of arguments.\n");
            writer.flush(); // Make sure to flush the stream
            return ""; // Return an empty string for invalid arguments
        }
    }
  
    public String handleSignup(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 7) {
            String email = tokens[1];
            String firstName = tokens[2];
            String lastName = tokens[3];
            String dateOfBirthString = tokens[4];
            String username = tokens[5];
            String password = tokens[6];

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateOfBirth = dateFormat.parse(dateOfBirthString);
                //String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                User user = new User(email, firstName, lastName, dateOfBirth, username, password);
                long user_id = UserService.addUser(user);
                if (user_id != -1) {
                    writer.write("User added successfully.\n");
                    writer.write(Long.toString(user_id) + "\n");
                    writer.flush();
                    return username;
                } else {
                    writer.write("User could not be added.\n");
                    writer.flush();
                    return "";
                }
            } catch (ParseException e) {
                writer.write("Invalid date format. Please use the format yyyy-MM-dd.\n");
                writer.flush();
            }
        } else {
            writer.write("Invalid number of arguments.\n");
            writer.flush();
        }
        writer.flush();
        return "";
    }

    private void handleAddFollower(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 3) {
            try {
                Long userId = Long.parseLong(tokens[1]);
                String followedUsername = tokens[2];
                try {
                    Optional<User> optionalUser = userDAO.getUserByUsername(followedUsername);
                    if (optionalUser.isPresent() && !optionalUser.isEmpty()){
                        User followedUser = optionalUser.get();
                        if (UserRelationshipService.addFollower(userId, followedUser.getId())) {
                            writer.write("Follower added successfully!\n");
                            writer.flush();
                        } else {
                            writer.write("Failed to add follower. Please try again.\n");
                            writer.flush();
                        }
                    }
                } catch (Exception e) {
                    writer.write("Failed to find the Followed user.\n");
                    writer.flush();
                }
            } catch (NumberFormatException e) {
                writer.write("Invalid user IDs. Please provide valid numeric values.\n");
                writer.flush();
            }
        } else {
            writer.write("Invalid ADDFOLLOWER command format. Usage: ADDFOLLOWER User_ID Followed_User_Username\n");
            writer.flush();
        }
        writer.newLine();
        writer.flush();
    }

    private void handleRemoveFollower(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 3) {
            try {
                Long userId = Long.parseLong(tokens[1]);
                String followedUsername = tokens[2];
                try {
                    Optional<User> optionalUser = userDAO.getUserByUsername(followedUsername);
                    if (optionalUser.isPresent() && !optionalUser.isEmpty()){
                        User followedUser = optionalUser.get();
                        if (UserRelationshipService.removeFollower(userId, followedUser.getId())) {
                            writer.write("Follower removed successfully!\n");
                            writer.flush();
                        } else {
                            writer.write("Failed to remove follower. Please try again.\n");
                            writer.flush();
                        }
                    }
                } catch (Exception e) {
                    writer.write("Failed to find the Followed user.\n");
                    writer.flush();
                }
               
            } catch (NumberFormatException e) {
                writer.write("Invalid user IDs. Please provide valid numeric values.\n");
                writer.flush();
            }
        } else {
            writer.write("Invalid REMOVEFOLLOWER command format. Usage: REMOVEFOLLOWER User_ID Followed_User_Username\n");
            writer.flush();
        }
        writer.newLine();
        writer.flush();
    }
    private void handleGetFollowers(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 2) {
            try {
                Long userId = Long.parseLong(tokens[1]);
                writer.write("Success\n");
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
     // Method to handle the search user by username command from the client
     public void handleSearchUser(String[] tokens, BufferedWriter writer) throws IOException {
        if (tokens.length == 2) {
            String username = tokens[1];
            // Check if the user with the given username exists
            boolean userExists = UserService.FindUserByUsername(username);

            // Send the response back to the client
            if (userExists) {
                writer.write("User exists.\n");
                writer.flush();
            } else {
                writer.write("User does not exist.\n");
                writer.flush();
            }

            writer.newLine();
            writer.flush();
        }
        else {
            writer.write("Invalid SEARCHFORUSER command format. Usage: SEARCHFORUSER  Username");
            writer.newLine();
            writer.flush();
        }    
    }
}

