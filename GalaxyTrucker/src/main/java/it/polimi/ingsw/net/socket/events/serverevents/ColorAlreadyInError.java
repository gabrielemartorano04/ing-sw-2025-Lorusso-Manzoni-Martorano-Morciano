package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Error event from server to client. The choosen color is already in use
 */
public class ColorAlreadyInError extends ServerEvent {
    private String message;
    private PlayerColors color;
    private EnumSet<PlayerColors> chooseColors;
    public ColorAlreadyInError(PlayerColors color, EnumSet<PlayerColors> availableColors) {
        super(ServerEventType.COLOR_ALREADY_IN, false);
        this.color = color;
        this.chooseColors = availableColors;
        message= color.toString()+ "already is use. Available colors: " + availableColors;

    }
    @Override
    public String getMessage() {
        return message;
    }
    public PlayerColors getColor() {
        return color;
    }
    public EnumSet<PlayerColors> getAvailableColors() {return chooseColors;}
}
