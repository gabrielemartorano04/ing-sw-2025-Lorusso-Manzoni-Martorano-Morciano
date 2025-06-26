package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.PingEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for RMI client callbacks.
 * Defines methods the server invokes remotely to notify or interact with the client,
 * and methods the client uses to communicate events to the server.
 */
public interface RemoteClientHandler extends Remote {

    /** called by the server to update client's view. */
    void handleIncomingEvent(ServerEvent event) throws RemoteException;

    /** calls the remote method on the rmiServer that updates the game. */
    void handleOutgoingEvent(ClientEvent event) throws RemoteException;

    /** Sets the client’s unique ID. */
    void setId(int id) throws RemoteException;

    /** Gets the client’s unique ID. */
    int getId() throws RemoteException;

    /** Responds to a ping from the server. */
    void respondToPing(PingEvent event) throws RemoteException;

    /** Closes and unexports the remote client object. */
    void close() throws RemoteException;
}
