package it.polimi.ingsw.net.socket.events.serverevents;

import java.awt.*;
import java.util.ArrayList;

/**
 * Error event from server to client. The choosen color is already in use
 */
public class ColorAlreadyInError extends ServerEvent {
    private String message;
    private Color color;
    private ArrayList<Color> availableColors;
    public ColorAlreadyInError(Color color,ArrayList<Color> availableColors) {
        super(ServerEventType.COLOR_ALREADY_IN);
        this.color = color;
        this.availableColors=availableColors;
        message= color.toString()+ "already is use. Available colors: " + availableColors;

    }
    @Override
    public String getMessage() {
        return message;
    }
    public Color getColor() {
        return color;
    }
}
