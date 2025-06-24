package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * The ShowOtherShipEvent class extends ClientEvent and represents a specific type of event
 * sent by a client to request the visualization of another player's spaceship
 */
public class ShowOtherShipEvent extends ClientEvent{

    /**
     * Nickname of the player of who show the spaceship
     */
    private final String nickname;

    /**
     * Event constructor
     * @param nickname of the player of who show the spaceship
     */
    public ShowOtherShipEvent(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the nickname of the player of who show the spaceship
     */
    public String getNicknameRequested() {
        return nickname;
    }

    /**
     * @return the message associated with the show of other players' spaceship
     */
    @Override
    public String getMessage() {
        return "I want to see "+nickname+"'s spaceship";
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
