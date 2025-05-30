package it.polimi.ingsw.net;

import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing a single ClientConnection, including the instance of the trucker linked to that Client.
 * The interface is used to permit us to not distinguish between RMI and Socket type of connection
 */
public interface ClientHandler extends Remote {
    public void setTruckerController(TruckerController truckerController);
    public TruckerController getTruckerController();
    public void handleEvent(ServerEvent event) throws RemoteException;
}
