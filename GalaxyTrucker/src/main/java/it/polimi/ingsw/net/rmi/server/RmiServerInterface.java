package it.polimi.ingsw.net.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;

/**
 * Remote interface for the RMI server.
 * Defines methods for client connection and event handling via RMI.
 */
public interface RmiServerInterface extends Remote {

    /** Registers a client with the server.*/
    void connect(RemoteClientHandler client) throws RemoteException;

    /** Called by the client to update the game with client's data or actions.*/
    void handleIncomingEvent(ClientEvent event, RemoteClientHandler client) throws RemoteException;
}
