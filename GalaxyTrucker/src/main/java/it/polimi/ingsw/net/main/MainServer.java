package it.polimi.ingsw.net.main;


import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.rmi.server.RmiServer;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.net.socket.server.NetworkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static void main(String[] args) {
        final int PORT = 12346;
        int clientSocketId = 0;
        ServerController serverController = new ServerController();

        RmiServer.bind(serverController);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                System.out.println(clientSocketId);
                NetworkServer networkServer = new NetworkServer(clientSocket);
                ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(networkServer, serverController, clientSocketId);
                Client client = new Client(clientHandlerSocket);
                serverController.addClient(client);
                clientSocketId++;
                System.out.println(clientSocketId);
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }

}

