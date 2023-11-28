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
import java.security.Timestamp;

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
    private Timestamp timestamp;

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }


    @FXML
    private void handleSignUpButtonAction(ActionEvent event) throws IOException {
        // Collect user input
        String enteredFirstName = First_Name.getText();
        String enteredLastName = Last_Name.getText();
        String enteredDateOfBirth = Dateof_birth.getText();
        String enteredUsername = username.getText();
        String enteredEmail = email.getText();
        String enteredPassword = password.getText();



        // Validate user input (customize this based on your requirements)
        if (enteredUsername.isEmpty() || enteredEmail.isEmpty() || enteredPassword.isEmpty() ||
                enteredFirstName.isEmpty() || enteredLastName.isEmpty() || enteredDateOfBirth.isEmpty()) {
            showAlert("Error", "All fields are required");
            return;
        }

        // Send signup information to the server
        int userId = sendSignupRequest(enteredEmail,enteredFirstName, enteredLastName, enteredDateOfBirth,
                enteredUsername,enteredPassword);

        if (userId != -1) {
            showAlert("Success", "Signup successful!");

            // Optionally, you can navigate to the platform page or perform any other necessary actions
            loadPlatformPage(userId);

        } else {
            showAlert("Error", "Signup failed. Please try again.");
        }
    }
    public Integer sendSignupRequest(String email, String firstName, String lastName, String dateOfBirth,
                                 String username, String password) throws IOException {
        try {
            client = new Client("localhost", 8000);
            String signupRequest = "SIGNUP " + email + " " + firstName + " " + lastName + " " + dateOfBirth +
                    " " + username + " " + password + "\n";
            client.send(signupRequest);

            String response = client.receive();
            if ("User added successfully.".equals(response)) {
                // Assuming server sends the user ID after successful signup
                String user_id_r = client.receive();
                System.out.println(user_id_r);
                int userId = Integer.parseInt(user_id_r);
                client.close();
                return userId;
            } else {
                client.close();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /* //////////////////////////////////////////////////////////////////////////////////////////////////////////////// */

    private void loadPlatformPage(int userId) {
        try {
            // Load the Platform.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Platform.fxml"));
            Parent platformRoot = loader.load();

            // Access the controller and set the user ID
            PlatformController platformController = loader.getController();
            platformController.setUserId(userId);
            platformController.initialize(null, null);

            // Create a new stage for the platform page
            Stage platformStage = new Stage();
            platformStage.setTitle("Platform Page");

            // Set the scene with the loaded FXML content
            platformStage.setScene(new Scene(platformRoot));

            // Show the new stage
            platformStage.show();

            // Close the signup stage
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


    @FXML
    private void HandleSigninbutton() {
        try {
            // Load the Platform.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Login.fxml"));
            Parent platformRoot = loader.load();

            // Create a new stage for the platform page
            Stage platformStage = new Stage();
            platformStage.setTitle("Platform Page");

            // Set the scene with the loaded FXML content
            platformStage.setScene(new Scene(platformRoot));

            // Show the new stage
            platformStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error Loading Page", "An error occurred while loading the new page. Please try again.");
        }
    }
}
