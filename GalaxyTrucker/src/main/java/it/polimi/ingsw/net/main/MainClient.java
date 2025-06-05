package it.polimi.ingsw.net.main;

import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;
import it.polimi.ingsw.net.rmi.server.RmiServerInterface;
import it.polimi.ingsw.net.rmi.client.RmiClient;
import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.socket.client.NetworkClient;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.view.ViewFlow;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Main client application
 */
public class MainClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        System.out.print("Do you want to connect via (1) Socket or (2) RMI? ");
        do{
            System.out.println("Type 1 or 2, everything else is not accepted:");
            choice = scanner.nextLine().trim();
        }while(!(choice.equals("1") || choice.equals("2")));


        String answer;
        System.out.print("Do you want to connect to the localhost? (y/n): ");
        do{
            System.out.println("Type y or n, everything else is not accepted: ");
            answer = scanner.nextLine().trim().toLowerCase();
        }while(!(answer.equals("y") || answer.equals("n")));

        String serverAddress;
        if (answer.equals("y")) {
            serverAddress = "127.0.0.1"; // Localhost
        } else {
            System.out.print("Insert the server address: ");
            serverAddress = scanner.nextLine().trim();
        }

        if(choice.equals("1")) {
            final int SERVER_PORT = 12346;
            NetworkClient networkClient = new NetworkClient(serverAddress, SERVER_PORT);
            try {
                networkClient.connect();
                ClientEventSender sender = new ClientEventSender(networkClient);
                ViewFlow view = new ViewFlow(sender);

                NetworkListenerClient listenerClient = new NetworkListenerClient(networkClient.getSocket(), view);

                listenerClient.start();
                view.start();

            } catch (IOException e) {
                System.err.println("Error in server connection: " + e.getMessage());
            }
        }
        else {
            try {
                Registry registry = LocateRegistry.getRegistry(serverAddress, 1099);
                System.out.println(serverAddress);
                RmiServerInterface rmiServer = (RmiServerInterface) registry.lookup("MyService");

                ViewFlow view = new ViewFlow(null);
                RemoteClientHandler client = new RmiClient(rmiServer, view);
                rmiServer.connect(client);
                view.setRmiClient(client);
                view.start();

            } catch (Exception e) {
                System.err.println("Error connecting to RMI server: " + e.getMessage());
                e.printStackTrace();
            }

        }

    }
}




