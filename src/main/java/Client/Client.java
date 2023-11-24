package Client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Client {

    // Replace the following URL with the actual endpoint for user authentication on your server
    private static final String AUTHENTICATION_ENDPOINT = "http://your-server-url/authenticate";

    public boolean authenticateUser(String username, String password) {
        try {
            // Create a URL object with the authentication endpoint
            URL url = new URL(AUTHENTICATION_ENDPOINT);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST (or GET, depending on your server implementation)
            connection.setRequestMethod("POST");

            // Enable input/output streams
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Construct the request payload with username and password
            String payload = "username=" + username + "&password=" + password;

            // Write the payload to the connection's output stream
            connection.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));

            // Get the response code from the server
            int responseCode = connection.getResponseCode();

            // Read the response from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();

            // Close the connection
            connection.disconnect();

            // Check if the authentication was successful based on the response
            return responseCode == HttpURLConnection.HTTP_OK && response.equals("Authenticated");

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., log, show an error message)
            return false;
        }
    }



    //Signup page
    /**
     * Sends a signup request to the server.
     *
     * @param username      The username chosen by the user during signup.
     * @param email         The email address provided by the user during signup.
     * @param password      The password chosen by the user during signup.
     * @param firstName     The first name of the user during signup.
     * @param lastName      The last name of the user during signup.
     * @param dateOfBirth   The date of birth provided by the user during signup.
     * @return True if the signup is successful, false otherwise.
     */
    public boolean sendSignupRequest(String username, String email, String password,
                                     String firstName, String lastName, String dateOfBirth) {
        try {
            // Create the signup request URL
            URL signupUrl = new URL("http://your-server-url/signup");

            // Open a connection to the signup URL
            HttpURLConnection connection = (HttpURLConnection) signupUrl.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable input/output streams
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Build the signup request payload
            String signupData = "username=" + username +
                    "&email=" + email +
                    "&password=" + password +
                    "&firstName=" + firstName +
                    "&lastName=" + lastName +
                    "&dateOfBirth=" + dateOfBirth;

            // Convert the signup data to bytes
            byte[] postData = signupData.getBytes(StandardCharsets.UTF_8);

            // Set the content length of the request
            connection.setRequestProperty("Content-Length", String.valueOf(postData.length));

            // Write the signup data to the output stream
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(postData);
            }

            // Get the response code from the server
            int responseCode = connection.getResponseCode();

            // Read the response from the server (you might need to adjust this based on your server response)
            InputStream inputStream = connection.getInputStream();
            byte[] responseBytes = inputStream.readAllBytes();
            String response = new String(responseBytes, StandardCharsets.UTF_8);

            // Close the connection
            connection.disconnect();

            // Check if the signup was successful based on the response code
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Signup failed due to an exception
        }
    }

    // Other methods for communication with the server
}