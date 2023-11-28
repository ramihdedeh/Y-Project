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
        String authenticatedUsername = authenticateUser(enteredUsername, enteredPassword);

        if (!authenticatedUsername.isEmpty()) {
            loadPlatformPage(authenticatedUsername);
        } else {
            showAlert("Authentication Failed", "Incorrect username or password");
        }
    }

    // Modified method to handle authentication
    private String authenticateUser(String username, String password) {
        try {
            client = new Client("localhost", 8000);
            if (client == null) {
                System.err.println("Client is not properly initialized.");
                return "";
            }

            client.send("LOGIN " + username + " " + password);
            String response = client.receive();

            if ("Login successful.".equals(response)) {
                return client.receive(); // Receive username if successful
            } else {
                return ""; // Authentication failed
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Authentication failed due to an exception
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void loadPlatformPage(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/Client/Platform.fxml"));
            Parent root = loader.load();

            PlatformController platformController = loader.getController();
            platformController.setAuthenticatedUsername(username); // Set username instead of userId
            Scene scene = new Scene(root);
            Stage stage = (Stage) root.getScene().getWindow();

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
