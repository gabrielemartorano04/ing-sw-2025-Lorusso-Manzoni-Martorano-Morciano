package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Interface representing the handler of a single client socket handler
 * Provides methods to send events, manage client identity, and close the connection.
 */
public interface ClientHandlerSocketInterface {

    /** Sends a server event to the connected client. */
    void sendEvent(ServerEvent event);

    /** Sets the id of the client. */
    void setId(int id);

    /** Returns the id. */
    int getId();

    /** Close the socket. */
    void close();
}
