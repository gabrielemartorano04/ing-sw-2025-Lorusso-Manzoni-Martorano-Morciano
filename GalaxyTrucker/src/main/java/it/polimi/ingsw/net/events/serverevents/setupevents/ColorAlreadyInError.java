package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.EnumSet;

/**
 * The ColorAlreadyInError class represents a server-side event triggered when a player selects a color
 * that is already in use by another player
 */
public class ColorAlreadyInError extends ServerEvent {

    /**
     * Color already in use
     */
    private final PlayerColors color;

    /**
     * Set of available colors
     */
    private final EnumSet<PlayerColors> chooseColors;

    /**
     * Event constructor
     * @param color already chosen
     * @param availableColors set of available colors
     */
    public ColorAlreadyInError(PlayerColors color, EnumSet<PlayerColors> availableColors) {
        super(false);
        this.color = color;
        this.chooseColors = availableColors;
    }

    /**
     * @return the color already chosen
     */
    public PlayerColors getColor() {
        return color;
    }

    /**
     * @return the set of available colors
     */
    public EnumSet<PlayerColors> getAvailableColors() {return chooseColors;}

    /**
     * @return the message associated with the error
     */
    @Override
    public String getMessage() {
        return color.toString() + "already is use. Available colors: " + chooseColors;
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
