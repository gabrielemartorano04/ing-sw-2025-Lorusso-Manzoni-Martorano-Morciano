package it.polimi.ingsw.net.main;

import it.polimi.ingsw.net.ConnectionType;
import it.polimi.ingsw.net.rmi.server.RmiServerInterface;
import it.polimi.ingsw.net.rmi.client.RmiClient;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.socket.client.NetworkClient;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.view.ViewFlow;

import java.io.IOException;
import java.rmi.Naming;
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

        ConnectionType connectionType;
        if(choice.equals("1")) {
            connectionType = ConnectionType.SOCKET;
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
        else if(choice.equals("2")) {
            connectionType = ConnectionType.RMI;
            // === RMI client ===
            try {
                String url = "rmi://" + serverAddress + ":1099/MyService";
                RmiServerInterface rmiServer = (RmiServerInterface) Naming.lookup(url);

                //TODO omogolare la creazione della view sia per RMI che per Socket
                // SISTEMARE CREAZIONE VIEW ORA NON DA ERRORE MA E SBAGLIATA
                ViewFlow view = new ViewFlow(new ClientEventSender(new NetworkClient(serverAddress, 12346)));
                ClientHandler client = new RmiClient(rmiServer, view);
                rmiServer.connect(client);
                System.out.println("Connected to RMI server!");
                // Esempio chiamata remota
                String response = rmiServer.sayHello("ClientUser");
                System.out.println("Server says: " + response);

                // Qui puoi avviare la tua logica legata a ViewFlow, se hai un clientHandler RMI
                // (serve creare un clientHandler dedicato per RMI se necessario)

            } catch (Exception e) {
                System.err.println("Error connecting to RMI server: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            System.out.println("Invalid choice. Exiting.");
        }

    }
}




