package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClientHandler extends Remote, ClientHandler {
    void handleEvent(ServerEvent event) throws RemoteException;
}
