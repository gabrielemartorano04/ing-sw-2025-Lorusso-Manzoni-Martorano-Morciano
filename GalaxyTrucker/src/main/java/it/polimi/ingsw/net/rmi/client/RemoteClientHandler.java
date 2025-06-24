package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.PingEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClientHandler extends Remote {
    void handleIncomingEvent(ServerEvent event) throws RemoteException;
    void handleOutgoingEvent(ClientEvent event) throws RemoteException;
    void setId(int id) throws RemoteException;
    int getId() throws RemoteException;
    void respondToPing(PingEvent event) throws RemoteException;
}
