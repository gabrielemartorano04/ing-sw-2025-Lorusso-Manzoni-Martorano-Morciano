package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.control.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.io.Serializable;

/**
 * Represents an abstract client event sent from the client to the server.
 * ClientEvent serves as a base class for defining various specific types of events
 * that a client can generate during its interaction with the server. Each event provides
 * a method for processing it on the server and a default or customized message describing the event
 */
public abstract class ClientEvent implements Serializable {

    /**
     * Event constructor
     */
    public ClientEvent() {
    }

    /**
     * @return the message associated with the specific event
     */
    public String getMessage() {
        return "";
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    public abstract ServerEvent call(VirtualServer visitor, Client client);
}
