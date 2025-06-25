package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.EnumSet;

/**
 * The NicknameAlreadyInError class represents a server-side event that indicates a selected nickname
 * is already in use by another player in the game
 */
public class NicknameAlreadyInError extends ServerEvent {

    /**
     * Nickname already chosen
     */
    private final String username;

    /**
     * Set of available colors
     */
    EnumSet<PlayerColors> colors;

    /**
     * Event constructor
     * @param username already chosen
     * @param colors set of available colors
     */
    public NicknameAlreadyInError(String username, EnumSet<PlayerColors> colors) {
        super(false);
        this.username = username;
        this.colors = colors;
    }

    /**
     * @return the set of available colors
     */
    public EnumSet<PlayerColors> getColors() {
        return colors;
    }

    /**
     * @return the message associated with the error
     */
    @Override
    public String getMessage() {
        return username + " is already in use";
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
