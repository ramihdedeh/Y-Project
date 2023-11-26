package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;

public class HelloApplication extends Application {
    private Client Client;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/Client/Login.fxml"));

        Parent root = loader.load();

        LoginController loginController = loader.getController();

        Client = new Client();
        loginController.setClient(Client);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadPlatformPage(int userId) {
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

    public static void main(String[] args) {
        launch();
    }
}
