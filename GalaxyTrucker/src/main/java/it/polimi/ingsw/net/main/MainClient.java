package it.polimi.ingsw.net.main;

import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;
import it.polimi.ingsw.net.rmi.server.RmiServerInterface;
import it.polimi.ingsw.net.rmi.client.RmiClient;
import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.socket.client.NetworkClient;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.view.ViewFlow;
import it.polimi.ingsw.view.graphic.GUIApplication;

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
        System.out.print("Choose connection:\n" +
                "- Type (1) if you want to use SOCKET and TUI;\n" +
                "- Type (2) if you want to use SOCKET and GUI;\n" +
                "- Type (3) if you want to use RMI and TUI;\n" +
                "- Type (4) if you want to use RMI and GUI;\n");
        do{
            System.out.print("> ");
            choice = scanner.nextLine().trim();
        }while(!(choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4")));


        String answer;
        System.out.print("Do you want to connect to the localhost? (y/n): ");
        do{
            System.out.print("> ");
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
                ViewFlow view = new ViewFlow(sender,false);

                NetworkListenerClient listenerClient = new NetworkListenerClient(networkClient.getSocket(), view);

                listenerClient.start();
                view.start();

            } catch (IOException e) {
                System.err.println("Error in server connection: " + e.getMessage());
            }
        }
        else if(choice.equals("2")) {
            final int SERVER_PORT = 12346;
            GUIApplication.SERVER_PORT = SERVER_PORT;
            GUIApplication.serverAddress = serverAddress;
            GUIApplication.useGui = true;
            GUIApplication.launch(GUIApplication.class);
        }
        else if(choice.equals("3")){
            try {
                Registry registry = LocateRegistry.getRegistry(serverAddress, 4321);
                System.out.println(serverAddress);
                RmiServerInterface rmiServer = (RmiServerInterface) registry.lookup("GalaxyTrucker");

                ViewFlow view = new ViewFlow(null,false);
                RemoteClientHandler client = new RmiClient(rmiServer, view);
                rmiServer.connect(client);
                view.setRmiClient(client);
                view.start();

            } catch (Exception e) {
                System.err.println("Error connecting to RMI server: " + e.getMessage());
                e.printStackTrace();
            }

        }
        else {
            try {
                Registry registry = LocateRegistry.getRegistry(serverAddress, 4321);
                System.out.println(serverAddress);
                RmiServerInterface rmiServer = (RmiServerInterface) registry.lookup("GalaxyTrucker");

                ViewFlow view = new ViewFlow(null,true);
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




