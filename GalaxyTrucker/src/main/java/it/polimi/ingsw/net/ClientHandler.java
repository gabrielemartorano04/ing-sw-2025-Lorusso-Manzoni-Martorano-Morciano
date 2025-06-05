package it.polimi.ingsw.net;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Interface representing a single ClientConnection, including the instance of the trucker linked to that Client.
 * The interface is used to permit us to not distinguish between RMI and Socket type of connection
 */
public interface ClientHandler {
    void sendEvent(ServerEvent event);

    void updateLastPong();

    boolean isTimedOut(long now);

    void sendPing();

    void setId(int id);
    int getId();

    void close();
}
