package Client;

import com.yplatform.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;

public class PlatformController {
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
    private Client Client;

    public void setClient(Client Client) {
        this.Client = Client;
    }

    private void initialize() {
        // Initialize any necessary setup
        // Assume userId is obtained or set in your application
        int userId = getUserId(); // Replace with the actual userId

        // Fetch recent posts and display them in the User_post TextArea
        List<String> recentPosts = Client.getRecentPosts(userId);
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
            boolean postSuccessful = Client.sendPostRequest(userId, message);

            // If the post was successful, update the User_post TextArea
            if (postSuccessful) {
                User_post.appendText(message + "\n");
                // Clear the New_post TextField after posting
                New_post.clear();
            } else {
                // Handle the case where the message is empty
                // You might want to show an alert or provide feedback to the user

                // For example, display an alert:
                showAlert("Error", "Post message cannot be empty.");
            }
        }
    }

    // Replace this with your actual method for getting the user ID


    /* this code now is for the followers */

    @FXML
    private void handleSearchUsersButton(ActionEvent event) {
        // Get the search query from the Followers TextField
        String searchQuery = Followers.getText();
        int userId = getUserId();
        // Call the method in Client to search for users
        List<String> searchResults = Client.searchUsers(searchQuery);

        // Display the search results in the Followers TextField
        Followers.setText(String.join(", ", searchResults));

        // Check if the user is already a follower and show/hide buttons accordingly
        boolean isFollower = Client.isFollower(searchQuery);
        AddFollowerButton.setVisible(!isFollower);
        RemoveFollowerButton.setVisible(isFollower);
    }

    // Method to handle the Add Follower button action
    @FXML
    private void handleAddFollowerButton(ActionEvent event) {
        // Get the username from the Followers TextField
        String username = Followers.getText();

        // Call the method in Client to add the user as a follower
        boolean addSuccessful = Client.addFollower(username);

        // If the user is successfully added, update button visibility
        if (addSuccessful) {
            AddFollowerButton.setVisible(false);
            RemoveFollowerButton.setVisible(true);
        }
    }

    // Method to handle the Remove Follower button action
    @FXML
    private void handleRemoveFollowerButton(ActionEvent event) {
        // Get the username from the Followers TextField
        String username = Followers.getText();

        // Call the method in Client to remove the user as a follower
        boolean removeSuccessful = Client.removeFollower(username);

        // If the user is successfully removed, update button visibility
        if (removeSuccessful) {
            RemoveFollowerButton.setVisible(false);
            AddFollowerButton.setVisible(true);
        }
    }

    @FXML
    private void handleReloadMessageOfInterest(ActionEvent event) {
        // Get the user ID from wherever you store it (assuming it's available in the PlatformController)
        int userId = getUserId();

        // Call the method to get posts from followers from the server
        List<String> posts = Client.getPostsFromFollowers(userId);

        // Update the Messageofinterest TextArea with the retrieved posts
        Messageofinterest.clear(); // Clear existing content
        for (String post : posts) {
            Messageofinterest.appendText(post + "\n");
        }
    }

    private void showAlert(String title, String content) {
        // Create and show an alert with the specified title and content
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
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


}
