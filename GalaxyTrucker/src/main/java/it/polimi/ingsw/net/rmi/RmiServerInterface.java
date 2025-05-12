package it.polimi.ingsw.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;

public interface RmiServerInterface extends Remote {
    void registerClient(RmiClientInterface client) throws RemoteException;
    void receiveMessage(ClientEvent event, RmiClientInterface client) throws RemoteException;
}
