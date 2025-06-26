package it.polimi.ingsw.net.main;

import it.polimi.ingsw.net.control.ServerController;
import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.rmi.server.RmiServer;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocketInterfaceSocket;
import it.polimi.ingsw.net.socket.server.NetworkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.UnicastRemoteObject;


/**
 * Main server application that initializes and starts the server.
 * It sets up both the RMI server and the socket server,
 * listens for incoming client connections, and manages client sessions.
 * It also registers a shutdown hook to cleanly stop the server.
 */
public class MainServer {

    /**
     * Entry point of the server application.
     * Initializes ServerController, binds the RMI server, and
     * opens a ServerSocket to accept client connections.
     * For each accepted socket connection, a ClientHandlerSocket
     * and Client instance are created and registered with the ServerController.
     * Registers a shutdown hook to properly unexport the RMI server
     * and shut down the ServerController on JVM termination.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        final int PORT = 12346;
        ServerController serverController = new ServerController();

        RmiServer rmiServer = RmiServer.bind(serverController);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[MainServer] Shutdown hook triggered.");

            if (rmiServer != null) {
                try {
                    UnicastRemoteObject.unexportObject(rmiServer, true);
                    System.out.println("[MainServer] RMI server unexported.");
                } catch (Exception e) {
                    System.err.println("Error unexporting RMI server: " + e.getMessage());
                }
            }

            serverController.shutdown();
            System.out.println("[MainServer] ServerController shut down.");
        }));


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                NetworkServer networkServer = new NetworkServer(clientSocket);
                ClientHandlerSocketInterfaceSocket clientHandlerSocket = new ClientHandlerSocketInterfaceSocket(networkServer, serverController);
                Client client = new Client(clientHandlerSocket, null);
                serverController.addClient(client);
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }

}

