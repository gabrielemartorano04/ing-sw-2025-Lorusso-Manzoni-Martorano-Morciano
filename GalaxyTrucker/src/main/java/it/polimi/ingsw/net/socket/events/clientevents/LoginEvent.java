package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.model.PlayerColors;

/**
 * client to server
 */
public class LoginEvent extends ClientEvent {
    private String username;
    private PlayerColors color;
    private String message;

    public LoginEvent(String username,PlayerColors color) {
        super(ClientEventType.LOGIN, false);
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
}
