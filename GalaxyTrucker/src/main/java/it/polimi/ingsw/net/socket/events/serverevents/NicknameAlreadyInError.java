package it.polimi.ingsw.net.socket.events.serverevents;

import java.awt.*;
import java.util.ArrayList;

/**
 * Event from server to client. A player tried to join the game but the chosen nickname is already used.
 */
public class NicknameAlreadyInError extends ServerEvent {
    private String username,message;
    ArrayList<Color> colors;
    public NicknameAlreadyInError(String username, ArrayList<Color> colors) {
        super(ServerEventType.NICKNAME_ALREADY_IN, false);
        this.username = username;
        this.colors = colors;
        message = username + " is already in use";
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }
}
