package it.polimi.ingsw.net.rmi;

import java.rmi.Remote;

import java.rmi.RemoteException;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;


public interface RmiClientInterface extends Remote {
        void handleEvent(ServerEvent event) throws RemoteException;
}





