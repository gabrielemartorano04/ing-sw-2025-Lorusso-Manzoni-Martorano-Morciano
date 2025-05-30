// Classe RMI del client, il server la usa per inviare eventi
package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.rmi.server.RmiServer;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements ClientHandler {
    private final RmiServer server;


    public RmiClient(RmiServer server) throws RemoteException {
        super();
        this.server = server;
        server.connect(this);
    }

    @Override
    public void handleEvent(ServerEvent event) throws RemoteException {
    }


}
