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
        int userId = Client.authenticateUser(enteredUsername, enteredPassword);

        if (userId != -1) {
            loadPlatformPage(userId);
        } else {
            showAlert("Authentication Failed", "Invalid username or password.");
        }
    }


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




