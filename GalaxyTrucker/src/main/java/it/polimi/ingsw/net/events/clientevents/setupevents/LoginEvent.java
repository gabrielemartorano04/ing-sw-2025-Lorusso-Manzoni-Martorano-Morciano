package it.polimi.ingsw.net.events.clientevents.setupevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.control.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents a login event sent by a client to the server. This event contains
 * the player's chosen username and color
 */
public class LoginEvent extends ClientEvent {

    /**
     * Username chosen by the player
     */
    private final String username;

    /**
     * Color chosen by the player
     */
    private final PlayerColors color;

    /**
     * Event constructor
     * @param username chosen by the player
     * @param color chosen by the player
     */
    public LoginEvent(String username,PlayerColors color) {
        this.username = username;
        this.color = color;
    }

    /**
     * @return username chosen by the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return color chosen by the player
     */
    public PlayerColors getColor() {
        return color;
    }

    /**
     * @return the message associated with the login
     */
    @Override
    public String getMessage() {
        return "Username: " + username+ "\nColor: " + color.toString();
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client);
    }
}
