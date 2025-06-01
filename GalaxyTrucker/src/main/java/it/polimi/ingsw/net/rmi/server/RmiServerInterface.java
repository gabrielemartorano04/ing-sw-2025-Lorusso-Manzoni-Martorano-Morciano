package it.polimi.ingsw.net.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;


public interface RmiServerInterface extends Remote {
    void connect(ClientHandler client) throws RemoteException;
    String sayHello(String name) throws RemoteException;
    void handleIncomingEvent(ClientEvent event, ClientHandler client) throws RemoteException;
}
