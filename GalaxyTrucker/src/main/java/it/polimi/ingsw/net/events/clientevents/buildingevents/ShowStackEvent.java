package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents a client event that requests to show a specific stack identified by its index
 * from the list of stacks maintained on the server
 */
public class ShowStackEvent extends ClientEvent {

    /**
     * Index of the stack in the list of stacks
     */
    private final int index;

    /**
     * Event constructor
     * @param index of the stack in the list of stacks
     */
    public ShowStackEvent(final int index) {
        this.index = index;
    }

    /**
     * @return the index of the stack
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the message associated with stack selection
     */
    @Override
    public String getMessage() {
        return "I want to stanch n " + index;
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client );
    }
}
