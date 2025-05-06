package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * client to server
 */
public class LoginEvent extends Event {
    private final String username;
    private PlayerColors color;
    private String message;

    public LoginEvent(String username,PlayerColors color) {
        super(EventType.LOGIN);
        this.username = username;
        this.color = color;
        message = "Username: " + username + " , Color: " + color.toString();
    }

    public String getUsername() {
        return username;
    }

    public PlayerColors getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "LoginEvent{ username='" + username + "' }";
    }
}
