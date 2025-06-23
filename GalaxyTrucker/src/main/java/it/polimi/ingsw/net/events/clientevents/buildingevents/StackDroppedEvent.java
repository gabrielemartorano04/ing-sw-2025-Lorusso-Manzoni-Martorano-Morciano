package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for the drop of the selected stack during building phase
 */
public class StackDroppedEvent extends ClientEvent {

    /**
     * Current selected stack
     */
    int curr;

    /**
     * Event constructor
     * @param i index of the selected stack to drop
     */
    public StackDroppedEvent(int i) {
        super();
        this.curr=i;
    }

    /**
     * @return the current selected stack
     */
    public int getCurr() {
        return curr;
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
