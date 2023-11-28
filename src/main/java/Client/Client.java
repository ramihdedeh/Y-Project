package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;

    public Client(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to server at " + serverAddress + ":" + port);
    }

    public void send(String data) {
        try {
            out.write(data + "\n"); // Adding newline for line-based reading on the server side
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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