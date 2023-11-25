package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class LoginController {

    @FXML
    private Button button_login;
    @FXML
    private Button button_signup;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    // Inject YClient instance into the controller
    private Client Client;

    public void setClient(Client Client) {
        this.Client = Client;
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

        // Perform user authentication using YClient
        boolean isAuthenticated = Client.authenticateUser(enteredUsername, enteredPassword);

        if (isAuthenticated) {
            openPlatformPage();
        } else {
            showAlert("Authentication Failed", "Invalid username or password.");
        }
    }

    private void openPlatformPage() {
        try {
            // Load the Platform.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Platform.fxml"));
            Parent platformRoot = loader.load();

            // Create a new stage for the platform page
            Stage platformStage = new Stage();
            platformStage.setTitle("Platform Page");

            // Set the scene with the loaded FXML content
            platformStage.setScene(new Scene(platformRoot));

            // Show the new stage
            platformStage.show();

            // Close the current login stage (optional)
            ((Stage) button_login.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error Loading Page", "An error occurred while loading the new page. Please try again.");
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}



