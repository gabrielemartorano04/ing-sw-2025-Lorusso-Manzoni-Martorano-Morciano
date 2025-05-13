package it.polimi.ingsw.net.rmi;

import it.polimi.ingsw.net.rmi.server.ClientHandlerRmi;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ViewFlow;

import java.rmi.Remote;
import java.rmi.RemoteException;


// problema: risolvere la generalizzazione tramite interfacce di modo di trattare i tipi dinamici come statici

public class RmiClient implements Remote {
    private ClientHandlerRmi server;
    private final ViewFlow view;

    public RmiClient(ClientHandlerRmi server, ViewFlow view){
        this.server=server;
        this.view = view;
    }


    public void run() throws RemoteException {

    }

    public void SendMessage(ClientEvent event) throws RemoteException {
        // server.receiveMessage(event, this);
    }

    public void receiveMessage(ServerEvent event) throws RemoteException {
        view.onEventReceived(event);
    }

    public void connect () throws RemoteException {
        // gestione della connessione al server
    }

    public void disconnect () throws RemoteException {
        // gestione della disconnessione dal server
    }
}
