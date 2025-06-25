package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;
import it.polimi.ingsw.net.rmi.client.RmiClient;
import it.polimi.ingsw.net.rmi.server.RmiServerInterface;
import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.socket.client.NetworkClient;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.view.ViewFlow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * GUIApplication is the main entry point for a graphical user interface (GUI) application
 * extending the class from JavaFX. It manages the setup and initialization for both a network-based
 * client-server communication through sockets and an RMI-based communication model
 */
public class GUIApplication extends Application {

    /**
     * Represents the address of the server to which the application connects
     */
    public static String serverAddress;

    /**
     * A static flag to determine whether the application runs in GUI mode or not
     */
    public static boolean useGui;

    /**
     * A static variable representing the port number the application uses to connect to a server
     */
    public static int SERVER_PORT;

    /**
     * An instance of the ViewFlow class, used to manage the application’s flow
     * and interactions in the graphical user interface (GUI). It is created
     * and initialized based on the chosen communication model (network or RMI)
     */
    private ViewFlow viewFlow;

    /**
     * Starts the JavaFX application. Initializes the GUI and establishes communication
     * with either a TCP server or an RMI server, depending on the configuration.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        if(SERVER_PORT==12346){
            NetworkClient networkClient = new NetworkClient(serverAddress, SERVER_PORT);

            try {
                networkClient.connect();
                ClientEventSender sender = new ClientEventSender(networkClient);
                this.viewFlow = new ViewFlow(sender, useGui);
                viewFlow.getGUI().initGui(primaryStage);
                primaryStage.setOnCloseRequest(e -> {
                    Platform.exit();
                    System.exit(0);
                });

                NetworkListenerClient listenerClient = new NetworkListenerClient(networkClient.getSocket(), viewFlow);

                listenerClient.start();
                viewFlow.start();


            } catch (IOException e) {
                System.err.println("Error in server connection: " + e.getMessage());
            }
        }
        else{
            try {
                Registry registry = LocateRegistry.getRegistry(serverAddress, 4321);
                System.out.println(serverAddress);
                RmiServerInterface rmiServer = (RmiServerInterface) registry.lookup("GalaxyTrucker");

                ViewFlow view = new ViewFlow(null,true);
                view.getGUI().initGui(primaryStage);
                primaryStage.setOnCloseRequest(e -> {
                    Platform.exit();
                    System.exit(0);
                });
                RemoteClientHandler client;
                client = new RmiClient(rmiServer, view);
                rmiServer.connect(client);
                view.setRmiClient(client);
                view.start();
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The main entry point for launching the JavaFX application.
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
