package it.polimi.ingsw.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.net.ClientHandler;


public interface RmiServerInterface extends Remote {
    void connect(ClientHandler client) throws RemoteException;
    String sayHello(String name) throws RemoteException;
}
