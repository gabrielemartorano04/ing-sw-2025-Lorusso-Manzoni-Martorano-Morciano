package it.polimi.ingsw.net.rmi;

import java.rmi.Remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

public interface RmiClientInterface extends Remote {
    void receiveMessage(ServerEvent event) throws RemoteException;
}

