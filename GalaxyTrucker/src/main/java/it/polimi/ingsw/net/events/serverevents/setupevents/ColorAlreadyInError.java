package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.EnumSet;

/**
 * Error event from server to client. The choosen color is already in use
 */
public class ColorAlreadyInError extends ServerEvent {
    private String message;
    private PlayerColors color;
    private EnumSet<PlayerColors> chooseColors;
    public ColorAlreadyInError(PlayerColors color, EnumSet<PlayerColors> availableColors) {
        super(false);
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

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
