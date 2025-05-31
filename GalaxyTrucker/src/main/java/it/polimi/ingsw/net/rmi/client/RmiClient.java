// Classe RMI del client, il server la usa per inviare eventi
package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.rmi.RmiServerInterface;
import it.polimi.ingsw.net.rmi.server.RmiServer;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements RemoteClientHandler  {
    private final RmiServerInterface server;
    private int id;


    public RmiClient(RmiServerInterface server) throws RemoteException {
        super();
        this.id = -1;
        this.server = server;
    }

    @Override
    public void handleEvent(ServerEvent event) throws RemoteException {
    }

    @Override
    public int getId() throws RemoteException {
        return -1;
    }

    @Override
    public void setId(int id) throws RemoteException {
        this.id = id;
    }


}
