// Classe RMI del client, il server la usa per inviare eventi
package it.polimi.ingsw.net.rmi.client;

import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.rmi.RmiClientInterface;
import it.polimi.ingsw.net.rmi.server.RmiServer;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;
import it.polimi.ingsw.view.ViewFlow;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements RmiClientInterface, ClientHandler {
    private final RmiServer server;


    public RmiClient(RmiServer server) throws RemoteException {
        super();
        this.server = server;
        server.connect(this);
    }

    @Override
    public void handleEvent(ServerEvent event) {
        //TODO: fare il metodo che chiama il thread per onEventReceived
    }



    @Override
    public void setTruckerController(TruckerController truckerController) {

    }

    @Override
    public TruckerController getTruckerController() {
        return null;
    }

}
