// Classe RMI del client, il server la usa per inviare eventi
package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.net.rmi.RmiClientInterface;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ViewFlow;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements RmiClientInterface {
    private final ViewFlow view;

    public RmiClient(ViewFlow view) throws RemoteException {
        super();
        this.view = view;
    }

    @Override
    public void receiveMessage(ServerEvent event) throws RemoteException {

    }
}
