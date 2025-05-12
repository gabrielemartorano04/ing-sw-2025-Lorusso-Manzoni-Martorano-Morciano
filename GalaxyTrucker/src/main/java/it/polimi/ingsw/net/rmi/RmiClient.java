package it.polimi.ingsw.net.rmi;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ViewFlow;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient{
    private RmiServer server;
    private final ViewFlow view;

    public RmiClient(RmiServer server, ViewFlow view){
        this.server=server;
        this.view = view;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "GTServer";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        RmiServer server = (RmiServer) registry.lookup(serverName);
        // new RmiClient(server).run();
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
