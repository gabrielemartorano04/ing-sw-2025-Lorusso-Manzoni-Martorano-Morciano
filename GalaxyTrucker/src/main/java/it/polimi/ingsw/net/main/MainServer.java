package it.polimi.ingsw.net.main;


import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.rmi.server.RmiServer;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.net.socket.server.NetworkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MainServer {
    public static void main(String[] args) {
        final int PORT = 12346;
        ServerController serverController = new ServerController();
        Scanner scanner = new Scanner(System.in);

        String ip;
        do{
            System.out.println("Insert the IP of the server (format x.y.z.w). \n Insert -- 127.0.0.1 -- for localhost");
            ip = scanner.nextLine().trim();
        }while(!isValidIp(ip));

        RmiServer.bind(serverController, ip);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                NetworkServer networkServer = new NetworkServer(clientSocket);
                ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(networkServer, serverController);
                Client client = new Client(clientHandlerSocket, null);
                serverController.addClient(client);
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }


    public static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) return false;

        String[] parts = ip.split("\\.");
        if (parts.length != 4) return false;

        for (String part : parts) {
            try {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }


}

