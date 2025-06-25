package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event sent from a client to the server to indicate a decision
 * regarding a chosen planet from a list of planets
 */
public class PlanetDecisionEvent extends ClientEvent {

    /**
     * index of the planet from the list of the card
     */
    private final int index;

    /**
     * Nickname of the player who has to decide
     */
    private final String nickname;

    /**
     * Event constructor
     * @param index index of the planet from the liust of the card
     * @param nickname  nickname of the palyer who has to decide
     */
    public PlanetDecisionEvent(int index, String nickname) {
        this.index = index;
        this.nickname = nickname;
    }

    /**
     * @return the index of the chosen planet
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the nickname of the player who decided
     */
    public String getNickname() {
        return nickname;
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
