package it.polimi.ingsw.net.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static void main(String[] args) {
        final int PORT = 12345;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server in ascolto sulla porta " + PORT + "...");

            ServerController serverController = new ServerController();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress());

                NetworkServer networkServer = new NetworkServer(clientSocket);
                ClientHandlerServer clientHandlerServer = new ClientHandlerServer(networkServer, serverController);

                Thread thread = new Thread((Runnable) clientHandlerServer);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Errore nel server: " + e.getMessage());
        }
    }
}

