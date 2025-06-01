package it.polimi.ingsw.net.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;


public interface RmiServerInterface extends Remote {
    void connect(RemoteClientHandler client) throws RemoteException;
    void handleIncomingEvent(ClientEvent event, RemoteClientHandler client) throws RemoteException;
}
