package it.polimi.ingsw.net;

import it.polimi.ingsw.controller.Trucker;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

/**
 * Interface representing a single ClientConnection, including the instance of the trucker linked to that Client.
 * The interface is used to permit us to not distinguish between RMI and Socket type of connection
 */
public interface ClientHandler {
    public void setTruckerController(Trucker truckerController);
    public Trucker getTruckerController();
    public void sendEvent(ServerEvent event);
}
