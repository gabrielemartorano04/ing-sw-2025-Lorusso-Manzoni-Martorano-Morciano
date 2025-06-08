package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.socket.client.NetworkClient;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.view.ViewFlow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApplication extends Application {

    // Queste variabili saranno impostate prima del launch
    public static String serverAddress;
    public static boolean useGui;
    public static int SERVER_PORT;


    @Override
    public void start(Stage primaryStage) {
        NetworkClient networkClient = new NetworkClient(serverAddress, SERVER_PORT);

        try {
            networkClient.connect();
            ClientEventSender sender = new ClientEventSender(networkClient);
            ViewFlow view = new ViewFlow(sender, useGui);
            view.getGUI().initGui(primaryStage);
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });

            NetworkListenerClient listenerClient = new NetworkListenerClient(networkClient.getSocket(), view);

            listenerClient.start();
            view.start();


        } catch (IOException e) {
            System.err.println("Error in server connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
