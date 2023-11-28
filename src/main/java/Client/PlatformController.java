package Client;

import com.yplatform.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.net.URL;
import java.util.ResourceBundle;

public class PlatformController implements Initializable{
    public Button AddFollowerButton;
    public Button RemoveFollowerButton;
    @FXML
    private TextArea User_post;
    @FXML
    private TextField New_post;
    @FXML
    private Button Post_button;
    @FXML
    private TextField Followers;
    @FXML
    private Button Search_users;
    @FXML
    private Button Reload_messageofinterest;
    @FXML
    private TextArea Messageofinterest;
    @FXML
    private FlowPane UserPane;
    private Client client;
    private int userId;

    // Getter method for retrieving the user ID
    public int getUserId() {
        return userId;
    }

    // Setter method for setting the user ID
    public void setUserId(int userId) {
        this.userId = userId;
        // Optionally, you can load data based on the user ID here
    }

    public void setClient(Client Client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources ) {
        // Initialize any necessary setup
        // Assume userId is obtained or set in your application
        int userId = getUserId(); // Replace with the actual userId

        // Fetch recent posts and display them in the User_post TextArea
        List<String> recentPosts = getRecentPosts(userId);
        for (String post : recentPosts) {
            User_post.appendText(post + "\n");
        }
    }

    @FXML
    private void handlePostButtonAction(ActionEvent event) {
        // Get the user ID from wherever you store it (assuming it's available in the PlatformController)
        int userId = getUserId();

        // Get the post message from the New_post TextField
        String message = New_post.getText();

        // Check if the message is not empty before sending it
        if (!message.isEmpty()) {
            // Call the sendPostRequest method in the Client class
            boolean postSuccessful = sendPostRequest(userId, message);

            // If the post was successful, update the User_post TextArea
            if (postSuccessful) {
                User_post.clear();
                List<String> recentPosts = getRecentPosts(userId);
                for (String post : recentPosts) {
                     User_post.appendText(post + "\n");
                }
                New_post.clear();
            } else {
                // Handle the case where the message is empty
                // You might want to show an alert or provide feedback to the user

                // For example, display an alert:
                showAlert("Error", "Post message cannot be empty.");
            }
        }
    }

    // Method to send post request
private boolean sendPostRequest(int userId, String message) {
    try {
        client = new Client("localhost", 8000);

        // Construct the post request
        String postRequest = "POSTMESSAGE " + userId + " " + message + "\n";

        // Send the request to the server
        client.send(postRequest);
        //System.out.println("Post req sent.");
        // Receive the response from the server
        String response = client.receive();
        //System.out.println("Post message received.");
        // Check if the post was successful based on the response
        if ("Message posted successfully!".equals(response)) {
            // Close the client connection if the post was successful
           // System.out.println("Posted successfully");
            client.close();
            return true;
        } else {
            // Close the client connection and return false if the post failed
            client.close();
            return false;
        }
    } catch (IOException e) {
        e.printStackTrace();
        // Handle exception (e.g., log, show an error message)
        return false;
    }
}

private List<String> getRecentPosts(int userId) {
    try {
        client = new Client("localhost", 8000);
        // Construct the request to get recent posts
        String getRequest = "MYPOSTS " + userId + "\n"; // Add a space after MYPOSTS

        // Send the request to the server
        client.send(getRequest);

        // Receive the initial response from the server
        String response = client.receive();

        // Check if the response indicates success
        if ("Success".equals(response)) {
            // Receive the subsequent JSON file
            String jsonFile = client.receive();

            // Parse the JSON file and return the list of recent posts
            //System.out.println("Message Arrived.\n");
            List<String> posts = parseJsonPosts(jsonFile);
            //System.out.println("Retrieved list successfully.\n");
            client.close();
            return posts;
        } else {
            // Handle the case where the request failed
            System.out.println("Failed to retrieve recent posts. Server response: " + response);
            client.close();
            return List.of();
        }

    } catch (IOException e) {
        e.printStackTrace();
        // Handle exception (e.g., log, show an error message)
        return List.of();
    }
}
    /* this code now is for the followers */





    @FXML
    private void handleSearchUsersButton() {
        try {
            client = new Client("localhost", 8000);
            // Get the search query from the Followers TextField
            String searchQuery = Followers.getText();

            // Construct the search users request
            String searchRequest = "SEARCH_USERS " + searchQuery + "\n";

            // Send the request to the server
            client.send(searchRequest);

            // Receive the response from the server
            String response = client.receive();

            // Parse the response and return the list of matching users
            // Assuming the response is a comma-separated list of usernames
            String[] usernames = response.split(",");
            // Display the search results in the Followers TextField
            Followers.setText(String.join(", ", usernames));

            // Check if the user is already a follower and show/hide buttons accordingly
            boolean isFollower = isFollower(searchQuery);
            AddFollowerButton.setVisible(!isFollower);
            RemoveFollowerButton.setVisible(isFollower);
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
        }
    }

    private boolean isFollower(String username) {
        try {
            client = new Client("localhost", 8000);
            // Construct the isFollower request
            String isFollowerRequest = "IS_FOLLOWER " + username + "\n";

            // Send the request to the server
            client.send(isFollowerRequest);

            // Receive the response from the server
            String response = client.receive();

            // Parse the response and return true or false
            client.close();
            return "true".equals(response);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return false;
        }
    }

    // Method to handle the Add Follower button action
    @FXML
    private void handleAddFollowerButton(ActionEvent event) {
        try {
            client = new Client("localhost", 8000);
            // Get the username from the Followers TextField
            String username = Followers.getText();

            // Construct the add follower request
            String addFollowerRequest = "ADDFOLLOWER" + username + "\n";

            // Send the request to the server
            client.send(addFollowerRequest);

            // Receive the response from the server
            String response = client.receive();

            // Check if the operation was successful based on the response
            if ("Follower added successfully!".equals(response)) {
                // If the user is successfully added, update button visibility
                AddFollowerButton.setVisible(false);
                RemoveFollowerButton.setVisible(true);
            } else {
                // Handle the case where the follower addition failed
                // You might want to show an alert or provide feedback to the user
                showAlert("Error", "Failed to add follower. Please try again.");
            }
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
        }
    }

    // Method to handle the Remove Follower button action
    @FXML
    private void handleRemoveFollowerButton(ActionEvent event) {
        try {
            client = new Client("localhost", 8000);
            // Get the username from the Followers TextField
            String username = Followers.getText();

            // Construct the remove follower request
            String removeFollowerRequest = "REMOVEFOLLOWER" + username + "\n";

            // Send the request to the server
            client.send(removeFollowerRequest);

            // Receive the response from the server
            String response = client.receive();

            // Check if the operation was successful based on the response
            if ("Follower removed successfully!".equals(response)) {
                // If the user is successfully removed, update button visibility
                RemoveFollowerButton.setVisible(false);
                AddFollowerButton.setVisible(true);
            } else {
                // Handle the case where the follower removal failed
                // You might want to show an alert or provide feedback to the user
                showAlert("Error", "Failed to remove follower. Please try again.");
            }
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
        }
    }

    @FXML
    private void handleReloadMessageOfInterest(ActionEvent event) {
        try {
            client = new Client("localhost", 8000);
            // Get the user ID from wherever you store it (assuming it's available in the PlatformController)
            int userId = getUserId();

            // Construct the request to get posts from followers
            String request = "INTERESTPOSTS" + userId + "\n";

            // Send the request to the server
            client.send(request);

            // Receive the response from the server
            String response = client.receive();

            // Process the response and extract posts
            List<String> posts = parseJsonPosts(response);

            // Update the Messageofinterest TextArea with the retrieved posts
            Messageofinterest.clear(); // Clear existing content
            for (String post : posts) {
                Messageofinterest.appendText(post + "\n");
            }
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
        }
    }

    private List<String> parseJsonPosts(String jsonResponse) throws JSONException {
        List<String> posts = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonResponse);
        //System.out.println("Entered the function.");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonPost = jsonArray.getJSONObject(i);
    
            // Retrieve post details from the JSON object
            String content = jsonPost.getString("content");
            String author = jsonPost.getString("author");
            Long dateLong = jsonPost.getLong("date");
    
            // Convert the Long timestamp to Timestamp
            Timestamp date = new Timestamp(dateLong);
    
            // Construct a string representing the post
            String postString = author + " " + date + "\n" + content;
            posts.add(postString);
        }
    
        return posts;
    }
    private void showAlert(String title, String content) {
        // Create and show an alert with the specified title and content
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    //public void setAuthenticatedUsername(String username) {
    //}
}
