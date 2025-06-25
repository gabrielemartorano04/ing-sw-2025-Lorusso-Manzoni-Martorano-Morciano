package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Represents an event sent from the server to communicate the availability of players
 * with their respective colors and nicknames to the client for the resumption of an old game
 */
public class AvailablePlayersEvent extends ServerEvent {

    /**
     * List of available colors
     */
    ArrayList<PlayerColors> colors;

    /**
     * List of available nickname
     */
    ArrayList<String> nicknames;

    /**
     * Event constructor
     * @param colors list of available colors
     * @param nicknames list of available nicknames
     */
    public AvailablePlayersEvent(ArrayList<PlayerColors> colors, ArrayList<String> nicknames) {
        super(false);
        this.colors = colors;
        this.nicknames = nicknames;
    }

    /**
     * @return the list of available colors
     */
    public ArrayList<PlayerColors> getColors() {
        return colors;
    }

    /**
     * @return the list of available nicknames
     */
    public ArrayList<String> getNicknames() {
        return nicknames;
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
