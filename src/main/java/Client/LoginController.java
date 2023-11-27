package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private Button button_login;
    @FXML
    private Button button_signup;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;


    // Inject YClient instance into the controller
    private Client client;

    public LoginController() {
        try {
            // Assuming your server is running on localhost and port 8000
            client = new Client("localhost", 8000);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    // Add a setter method for the Client
    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void handleSignUpButtonAction(ActionEvent event) {
        try {
            // Load the signup.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Signup.fxml"));
            Parent signUpRoot = loader.load();

            // Create a new stage
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");

            // Set the scene with the loaded FXML content
            signUpStage.setScene(new Scene(signUpRoot));

            // Show the new stage
            signUpStage.show();

            // Close the current login stage (optional)
            ((Stage) button_signup.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Loading Page");
            alert.setContentText("An error occurred while loading the new page. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSignInButtonAction(ActionEvent event) {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        // Perform user authentication using Client
        int userId = authenticateUser(enteredUsername, enteredPassword);

        if (userId != -1) {
            loadPlatformPage(userId);
        } else {
            showAlert("Authentication Failed", "not a user");
        }
    }

    // Add this method to handle authentication using the Client instance
    private int authenticateUser(String username, String password) {
        try {
            // Construct the authentication request
            String authenticationRequest = "LOGIN " + username + " " + password;

            // Send the request to the server
            client.send(authenticationRequest);


            // Receive the response from the server
            String response = client.receive();

            // Check if the authentication was successful based on the response
            if ("Login successful.".equals(response)) {
                // Parse and return the user ID from the server response
                return Integer.parseInt(client.receive());
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

    /* //////////////////////////////////////////////////////////////////////////////////////////////////////// */
        private void showAlert (String title, String content){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        }

        public void loadPlatformPage ( int userId){
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/Client/Platform.fxml"));
                Parent root = loader.load();

                PlatformController platformController = loader.getController();
                platformController.setUserId(userId);
                Scene scene = new Scene(root);
                Stage stage = (Stage) root.getScene().getWindow(); // Assuming you're using the same stage for now

                stage.setScene(scene);

            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception (e.g., log, show an error message)
            }
        }

    }





