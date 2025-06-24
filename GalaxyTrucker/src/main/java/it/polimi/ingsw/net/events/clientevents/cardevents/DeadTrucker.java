package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for the manage of a dead trucker during flight phase
 */
public class DeadTrucker extends ClientEvent {

    /**
     * Event constructor
     */
    public DeadTrucker() {
    }

    /**
     * @return the message associated with death
     */
    @Override
    public String getMessage() {
        return "Dead Trucker";
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
        return visitor.visit(this);
    }
}
