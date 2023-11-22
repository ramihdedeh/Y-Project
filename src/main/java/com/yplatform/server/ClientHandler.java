package com.yplatform.server;

import com.yplatform.dao.PostDAO;
import com.yplatform.dao.UserDAO;
import com.yplatform.dao.UserRelationshipDAO;
//import com.yplatform.model.Post;
//import com.yplatform.model.User;

import java.io.*;
import java.net.Socket;
//import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final UserDAO userDAO;
    private final PostDAO postDAO;
    private final UserRelationshipDAO userRelationshipDAO;

    public ClientHandler(Socket clientSocket, UserDAO userDAO, PostDAO postDAO, UserRelationshipDAO userRelationshipDAO) {
        this.clientSocket = clientSocket;
        this.userDAO = userDAO;
        this.postDAO = postDAO;
        this.userRelationshipDAO = userRelationshipDAO;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            // Handle client communication here
            // You can use reader and writer to send/receive messages
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add methods for handling user registration, login, and other functionalities
}
