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

    private Client Client;  // Assuming you have an instance of YClient in your controller

    public void setYClient(Client yClient) {
        this.Client = Client;
    }

    @FXML
    private void handleSignUpButtonAction(ActionEvent event) {
        // Collect user input

        String enteredUsername = username.getText();
        String enteredEmail = email.getText();
        String enteredPassword = password.getText();
        String enteredFirstName = First_Name.getText();
        String enteredLastName = Last_Name.getText();
        String enteredDateOfBirth = Dateof_birth.getText();

        // Validate user input (you should customize this based on your requirements)
        if (enteredUsername.isEmpty() || enteredEmail.isEmpty() || enteredPassword.isEmpty() ||
                enteredFirstName.isEmpty() || enteredLastName.isEmpty() || enteredDateOfBirth.isEmpty()) {
            showAlert("Error", "All fields are required");
            return;
        }

        // we can add more validation logic based on the requirements

        // Send signup information to the server
        boolean signupSuccessful = sendSignupRequest(enteredUsername, enteredEmail, enteredPassword,
                enteredFirstName, enteredLastName, enteredDateOfBirth);

        if (signupSuccessful) {
            showAlert("Success", "Signup successful!");

            // Optionally, you can navigate to the platform page or perform any other necessary actions
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Platform.fxml"));
                Parent platformRoot = loader.load();
                Stage platformStage = new Stage();
                platformStage.setTitle("Platform Page");
                platformStage.setScene(new Scene(platformRoot));
                platformStage.show();
                ((Stage) button_signup.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while loading the platform page. Please try again.");
            }
        } else {
            showAlert("Error", "Signup failed. Please try again.");
        }
    }

    private boolean sendSignupRequest(String username, String email, String password,
                                      String firstName, String lastName, String dateOfBirth) {
        // Check if YClient is set
        if (Client == null) {
            showAlert("Error", "Client is not set.");
            return false;
        }

        // Call the YClient method to send the signup request
        return Client.sendSignupRequest(username, email, password, firstName, lastName, dateOfBirth);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
