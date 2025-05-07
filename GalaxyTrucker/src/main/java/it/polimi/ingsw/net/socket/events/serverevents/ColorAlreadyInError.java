package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

import java.awt.*;

/**
 * Error event from server to client. The choosen color is already in use
 */
public class ColorAlreadyInError extends ServerEvent {
    private String message;
    private Color color;
    public ColorAlreadyInError(Color color) {
        super(ServerEventType.COLOR_ALREADY_IN);
        message= color.toString()+ "already is use";
    }
    public String getMessage() {
        return message;
    }
    public Color getColor() {
        return color;
    }
}
