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
        int authenticatedUserId = authenticateUser(enteredUsername, enteredPassword);

        if (authenticatedUserId != -1) {
            //System.out.println(authenticatedUserId);
            loadPlatformPage(authenticatedUserId);
        } else {
            showAlert("Authentication Failed", "Incorrect username or password");
        }
    }

    // Modified method to handle authentication
    private Integer authenticateUser(String username, String password) {
        try {
            client = new Client("localhost", 8000);
            if (client == null) {
                System.err.println("Client is not properly initialized.");
                return -1;
            }

            client.send("LOGIN " + username + " " + password);
            String response = client.receive();

            if ("Login successful.".equals(response)) {
                String user_id_r = client.receive();
                int user_id = Integer.parseInt(user_id_r);
                client.close();
                return user_id;
            } else {
                client.close();
                return -1; // Authentication failed
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Authentication failed due to an exception
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void loadPlatformPage(int userID) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/Client/Platform.fxml"));
            Parent root = loader.load();

            // Access the controller and set the user ID
            PlatformController platformController = loader.getController();
            platformController.setUserId(userID);// Set user ID
            platformController.initialize(null, null);
             // Create a new stage for the platform page
             Stage platformStage = new Stage();
             platformStage.setTitle("Platform Page");
 
             // Set the scene with the loaded FXML content
             platformStage.setScene(new Scene(root));
 
             // Show the new stage
             platformStage.show();

             ((Stage) button_login.getScene().getWindow()).close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
