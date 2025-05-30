package it.polimi.ingsw.net.rmi;

import java.rmi.Remote;

import java.rmi.RemoteException;

import it.polimi.ingsw.net.socket.events.serverevents.*;


public interface RmiClientInterface extends Remote {
        void handleEvent(ServerEvent event) throws RemoteException;
}





