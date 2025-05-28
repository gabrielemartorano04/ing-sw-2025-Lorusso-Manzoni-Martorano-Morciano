package it.polimi.ingsw.net.socket.server;


import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.net.rmi.server.RmiServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static void main(String[] args) {
        final int PORT = 12346;
        ServerController serverController = new ServerController();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                NetworkServer networkServer = new NetworkServer(clientSocket);
                ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(networkServer, serverController);

            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }

}

