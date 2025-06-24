package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents a specific event sent from a client to the server to respond to a request
 * or decision related to gaining cosmic credits.
 */
public class GainCosmicCreditsResponseEvent extends ClientEvent {

    /**
     * Answer about decision on gaining credits
     */
    private final boolean answer;

    /**
     * Event constructor
     * @param answer to the decision of gaining credits
     */
    public GainCosmicCreditsResponseEvent(boolean answer) {
        this.answer = answer;
    }

    /**
     * @return the answer to the question about the gaining of the credits
     */
    public boolean getAnswer() {
        return answer;
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