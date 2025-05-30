// Classe RMI del client, il server la usa per inviare eventi
package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.rmi.server.RmiServer;
import it.polimi.ingsw.net.socket.events.serverevents.*;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements ClientHandler {
    private final RmiServer server;
    private TruckerController controller;


    public RmiClient(RmiServer server) throws RemoteException {
        super();
        this.server = server;
        server.connect(this);
    }

    @Override
    public void handleEvent(ServerEvent event) {
    }


    @Override
    public void setTruckerController(TruckerController truckerController) {
        this.controller = truckerController;
    }

    @Override
    public TruckerController getTruckerController() {
        return controller;
    }

}
