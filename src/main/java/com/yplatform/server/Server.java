package com.yplatform.server;

import com.yplatform.dao.PostDAO;
import com.yplatform.dao.UserDAO;
import com.yplatform.dao.UserRelationshipDAO;
import com.yplatform.dao.UserRelationshipDAOImpl;
import com.yplatform.dao.UserDAOImpl;
import com.yplatform.dao.PostDAOImpl;
//import com.yplatform.model.Post;
//import com.yplatform.model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Server {
    private static final int DEFAULT_PORT = 8000;
    private int port;
    private static final int THREAD_POOL_SIZE = 10; // Adjust the size based on your needs
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final UserDAO userDAO = new UserDAOImpl();
    private final PostDAO postDAO = new PostDAOImpl();
    private final UserRelationshipDAO userRelationshipDAO = new UserRelationshipDAOImpl();
    private boolean isRunning = true;
    public Server(int port) {
        this.port = port;
    }
    public void shutdown() {
        isRunning = false;
        executorService.shutdown();
    }
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server is running and listening on port " + port);

            while (isRunning) {
                try (Socket clientSocket = serverSocket.accept()) {
                    // Use the executor service to handle client connection
                    executorService.submit(new ClientHandler(clientSocket, userDAO, postDAO, userRelationshipDAO));
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error accepting client connection:", e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in the server:", e);
            isRunning = false;
        }
    }
    public static void main(String[] args) {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        Server server = new Server(port);
        // Register shutdown hook (Ctrl + c to shutdown the server in the console)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            logger.info("Server is shutting down");
        }));
        server.start();
    }
}
