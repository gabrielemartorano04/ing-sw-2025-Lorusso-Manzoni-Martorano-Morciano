package it.polimi.ingsw.net.rmi.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.rmi.RmiClientInterface;
import it.polimi.ingsw.net.rmi.RmiServerInterface;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientHandlerRmi extends UnicastRemoteObject implements RmiServerInterface, ClientHandler {
    private final ServerController controller;
    private final RmiClientInterface client;
    private Trucker trucker;

    public ClientHandlerRmi(ServerController controller, RmiClientInterface client) throws RemoteException {
        this.controller = controller;
        this.client = client;
        controller.addClient(this);  // come fai con Socket
    }

    public void receiveMessage(ClientEvent event) {
        // controller.handleEvent(event, this);
    }

    @Override
    public void sendEvent(ServerEvent event) {
        try {
            client.receiveMessage(event);  // chiamata remota
        } catch (RemoteException e) {
            System.err.println("Errore invio evento via RMI: " + e.getMessage());
            controller.removeClient(this);
        }
    }

    @Override
    public void setTrucker(Trucker trucker) {
        this.trucker = trucker;
    }

    @Override
    public Trucker getTrucker() {
        return trucker;
    }

    @Override
    public void registerClient(RmiClientInterface client) throws RemoteException {

    }

    @Override
    public void receiveMessage(ClientEvent event, RmiClientInterface client) throws RemoteException {

    }
}




