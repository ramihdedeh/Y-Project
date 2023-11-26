package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Client {

    private static final String SERVER_HOST = "localhost"; // Change this to your server's host
    private static final int SERVER_PORT = 12345; // Change this to your server's port

    private static Client instance;



    public int authenticateUser(String username, String password) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the authentication request
            String authenticationRequest = "AUTHENTICATE " + username + " " + password + "\n";

            // Send the request to the server
            outputStream.write(authenticationRequest.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Check if the authentication was successful based on the response
            if ("Authenticated".equals(response)) {
                // Parse and return the user ID from the server response
                return Integer.parseInt(reader.readLine());
            } else {
                // Authentication failed
                return -1;
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return -1; // Authentication failed due to an exception
        }
    }


    //Signup page
    /**
     * Sends a signup request to the server.
     *
     * @param username      The username chosen by the user during signup.
     * @param email         The email address provided by the user during signup.
     * @param password      The password chosen by the user during signup.
     * @param firstName     The first name of the user during signup.
     * @param lastName      The last name of the user during signup.
     * @param dateOfBirth   The date of birth provided by the user during signup.
     * @return True if the signup is successful, false otherwise.
     */
    public int sendSignupRequest(String username, String email, String password,
                                 String firstName, String lastName, String dateOfBirth) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the signup request
            String signupRequest = "SIGNUP " + username + " " + email + " " + password +
                    " " + firstName + " " + lastName + " " + dateOfBirth + "\n";

            // Send the request to the server
            outputStream.write(signupRequest.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Check if the signup was successful based on the response
            if ("SignUp Successful".equals(response)) {
                // Parse and return the user ID from the server response
                return Integer.parseInt(reader.readLine());
            } else {
                // Signup failed
                return -1;
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return -1; // Signup failed due to an exception
        }
    }



    // Other methods for communication with the server
/*                       the Platform side methods                            */
    /**
     * Sends a post request to the server.
     *
     * @param userId The ID of the user posting the message.
     * @param message The message to be posted.
     * @return True if the post is successful, false otherwise.
     */


    public boolean sendPostRequest(int userId, String message) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the post request
            String postRequest = "POST " + userId + " " + message + "\n";

            // Send the request to the server
            outputStream.write(postRequest.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Check if the post was successful based on the response
            return "Post Successful".equals(response);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return false;
        }
    }


    public List<String> getRecentPosts(int userId) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the request to get recent posts
            String getRequest = "GET_RECENT_POSTS " + userId + "\n";

            // Send the request to the server
            outputStream.write(getRequest.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Parse the response and return the list of recent posts
            // You need to implement the logic to parse the response based on your server's protocol
            // The returned list can be a list of strings, where each string is a post
            // Example: return List.of("Post 1", "Post 2", "Post 3");
            // Replace the example with the actual parsing logic.

            // For now, returning an empty list
            return List.of();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return List.of();
        }
    }
    /*    for the followers methods */

    public static List<String> searchUsers(String searchQuery) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the search users request
            String searchRequest = "SEARCH_USERS " + searchQuery + "\n";

            // Send the request to the server
            outputStream.write(searchRequest.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Parse the response and return the list of matching users
            // Assuming the response is a comma-separated list of usernames
            return Arrays.asList(response.split(","));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return Collections.emptyList();
        }
    }
    public static boolean isFollower(String username) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the isFollower request

            String isFollowerRequest = "IS_FOLLOWER "+ username + "\n";

            // Send the request to the server
            outputStream.write(isFollowerRequest.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Parse the response and return true or false
            return "true".equals(response); // Adjust based on the server's response
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return false;
        }

    }
    public static boolean addFollower(String username) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the add follower request
            String addFollowerRequest = "ADD_FOLLOWER " + username + "\n";

            // Send the request to the server
            outputStream.write(addFollowerRequest.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Check if the operation was successful based on the response
            return "Follower Added".equals(response);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return false; // Operation failed due to an exception
        }
    }
    public static boolean removeFollower(String username) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the remove follower request
            String removeFollowerRequest = "REMOVE_FOLLOWER " + username + "\n";

            // Send the request to the server
            outputStream.write(removeFollowerRequest.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Check if the operation was successful based on the response
            return "Follower Removed".equals(response);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return false; // Operation failed due to an exception
        }
    }
    public static List<String> getPostsFromFollowers(int userId) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            // Construct the request to get posts from followers
            String request = "GET_POSTS_FROM_FOLLOWERS " + userId + "\n";

            // Send the request to the server
            outputStream.write(request.getBytes());

            // Receive the response from the server
            String response = reader.readLine();

            // Process the response and extract posts
            List<String> posts = new ArrayList<>();
            // Assuming the response contains posts separated by a specific delimiter (adjust as needed)
            String[] postArray = response.split("\\|"); // Change the delimiter as needed
            for (String post : postArray) {
                posts.add(post);
            }

            return posts;

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return new ArrayList<>(); // Return an empty list in case of an error
        }
    }

}
