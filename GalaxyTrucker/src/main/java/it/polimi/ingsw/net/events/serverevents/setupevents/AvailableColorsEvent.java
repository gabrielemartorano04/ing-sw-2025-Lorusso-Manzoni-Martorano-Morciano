package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.EnumSet;

/**
 * AvailableColorsEvent is a type of ServerEvent used to notify the client about the available player color options
 * during the game initialization or selection process
 */
public class AvailableColorsEvent extends ServerEvent {

    /**
     * Set of available colors
     */
    private final EnumSet<PlayerColors> colors;

    /**
     * Message associated with the color choosing
     */
    private final String message;

    /**
     * Event constructor
     * @param colors available
     * @param message associated with the color choosing
     */
    public AvailableColorsEvent(EnumSet<PlayerColors> colors, String message) {
        super(false);
        this.colors=colors;
        this.message = message;
    }

    /**
     * @return the set of available colors
     */
    public EnumSet<PlayerColors> getColors() {
        return colors;
    }

    /**
     * @return the message associated with the color choosing
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor){
        visitor.visit(this);
    }
}
