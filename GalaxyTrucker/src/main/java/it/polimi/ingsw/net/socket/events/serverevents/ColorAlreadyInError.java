package it.polimi.ingsw.net.socket.events.serverevents;

import java.awt.*;
import java.util.ArrayList;

/**
 * Error event from server to client. The choosen color is already in use
 */
public class ColorAlreadyInError extends ServerEvent {
    private String message;
    private Color color;
    private ArrayList<String> chooseColors;
    public ColorAlreadyInError(Color color,ArrayList<Color> availableColors) {
        super(ServerEventType.COLOR_ALREADY_IN, false);
        this.color = color;
        int i=0;
        for(Color colors : availableColors){
            chooseColors.set(i, colors.toString());
            i++;
        }
        message= color.toString()+ "already is use. Available colors: " + availableColors;

    }
    @Override
    public String getMessage() {
        return message;
    }
    public Color getColor() {
        return color;
    }
    public ArrayList<String> getAvailableColors() {return chooseColors;}
}
