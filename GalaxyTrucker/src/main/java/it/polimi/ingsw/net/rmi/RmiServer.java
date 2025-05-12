package it.polimi.ingsw.net.rmi;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RmiServer {
    private final ServerController serverController;
    private final RmiClient client;

    public RmiServer(ServerController serverController, RmiClient client) {
        this.serverController = serverController;
        // serverController.addClient(client);
        this.client = client;
    }

    public void receiveMessage(ClientEvent event) {
        // serverController.handleEvent(event, this);
    }

    public void sendMessage(ServerEvent event) throws RemoteException {
        // client.receiveMessage(event);
    }
}
