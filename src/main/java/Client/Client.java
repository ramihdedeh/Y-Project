package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
public class Client {


    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port); //  A Socket instance to establish a network connection
        out = new PrintWriter(socket.getOutputStream(), true); // A PrintWriter to send data to the server
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //  A BufferedReader to read data from the server
        System.out.println("Connected to server at " + serverAddress + ":" + port);
    }

    public void send(String data) {
        out.println(data);
    }

    public String receive() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
