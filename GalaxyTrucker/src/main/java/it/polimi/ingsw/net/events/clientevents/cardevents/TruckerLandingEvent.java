package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for the decision of landing on an abandoned place
 */
public class TruckerLandingEvent extends ClientEvent {

    /**
     * Decision about landing
     */
    private boolean landing;

    /**
     * Event constructor
     * @param landing decision about the landinjg
     */
    public TruckerLandingEvent(boolean landing) {
        this.landing = landing;
    }

    /**
     * @return the decision about the landing
     */
    public boolean getLanding() {
        return landing;
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
