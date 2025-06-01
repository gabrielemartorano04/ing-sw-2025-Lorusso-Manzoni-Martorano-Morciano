package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClientHandler extends Remote, ClientHandler {
    @Override
    void handleEvent(ServerEvent event) throws RemoteException;
    @Override
    int getId() throws RemoteException;
    void setId(int id) throws RemoteException;
    void handleIncomingEvent(ServerEvent event) throws RemoteException;
    void handleOutgoingEvent(ClientEvent event) throws RemoteException;
}
