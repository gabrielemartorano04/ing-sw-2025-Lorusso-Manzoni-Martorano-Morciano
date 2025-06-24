package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents a specific client event indicating that a player needs to draw a card
 */
public class DrawCardEvent extends ClientEvent {

    /**
     * Current player who has to draw the card
     */
    private final String currNickname;

    /**
     * Event constructor
     * @param currNickname player who has to draw the card
     */
    public DrawCardEvent(String currNickname) {
        this.currNickname = currNickname;
    }

    /**
     * @return the current nickname of the player who has to draw the card
     */
    public String getCurrNickname() {
        return currNickname;
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
