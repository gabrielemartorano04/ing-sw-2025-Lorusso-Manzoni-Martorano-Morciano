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

public class GUIApplication extends Application {

    // Queste variabili saranno impostate prima del launch
    public static String serverAddress;
    public static boolean useGui;
    public static int SERVER_PORT;
    private ViewFlow viewFlow;

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
                RemoteClientHandler client = null;
                client = new RmiClient(rmiServer, view);
                rmiServer.connect(client);
                view.setRmiClient(client);
                view.start();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
