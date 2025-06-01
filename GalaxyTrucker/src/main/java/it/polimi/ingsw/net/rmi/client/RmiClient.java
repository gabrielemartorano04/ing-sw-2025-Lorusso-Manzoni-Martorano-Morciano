// Classe RMI del client, il server la usa per inviare eventi
package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.rmi.server.RmiServerInterface;
import it.polimi.ingsw.view.ViewFlow;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements RemoteClientHandler  {
    private final RmiServerInterface server;
    private final ViewFlow view;


    public RmiClient(RmiServerInterface server, ViewFlow view) throws RemoteException {
        super();
        this.server = server;
        this.view = view;
    }


    public void handleIncomingEvent(ServerEvent event) throws RemoteException {
        // metodo che il server chiama in modo remoto per invocare il visitor del client
        view.onEventReceived(event);
    }

    public void handleOutgoingEvent (ClientEvent event) throws RemoteException {
        // metodo che il client chiama per invocare il visitor del server
        server.handleIncomingEvent(event, this);
    }


}
