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

import java.io.IOException;

public class SignupController {
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private Button button_signup;

    @FXML
    private TextField First_Name;

    @FXML
    private TextField Last_Name;

    @FXML
    private TextField Dateof_birth;
    @FXML
    private void handleSignUpButtonAction(ActionEvent event) {
        try {
            // Load the Signup.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Platform.fxml"));
            Parent signUpRoot = loader.load();

            // Create a new stage for sign-up
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
