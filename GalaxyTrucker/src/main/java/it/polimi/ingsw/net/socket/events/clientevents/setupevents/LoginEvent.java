package it.polimi.ingsw.net.socket.events.clientevents.setupevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

/**
 * client to server
 */
public class LoginEvent extends ClientEvent {
    private String username;
    private PlayerColors color;
    private String message;

    public LoginEvent(String username,PlayerColors color) {
        this.username = username;
        this.color = color;
        message = "Username: " + username+ "\nColor: " + color.toString();
    }

    public String getUsername() {
        return username;
    }

    public PlayerColors getColor() {
        return color;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
