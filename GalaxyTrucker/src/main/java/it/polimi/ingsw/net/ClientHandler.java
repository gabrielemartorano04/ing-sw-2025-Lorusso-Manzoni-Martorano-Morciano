package it.polimi.ingsw.net;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.rmi.RemoteException;

/**
 * Interface representing a single ClientConnection, including the instance of the trucker linked to that Client.
 * The interface is used to permit us to not distinguish between RMI and Socket type of connection
 */
public interface ClientHandler {
    public void handleEvent(ServerEvent event) throws RemoteException;
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
}
