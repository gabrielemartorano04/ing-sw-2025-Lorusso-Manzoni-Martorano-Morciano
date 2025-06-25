package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event indicating the successful discard of a tile by a player.
 */
public class DiscardSuccessEvent extends ServerEvent {

    /**
     * Index of the tile in the list of available components
     */
    private final int index;


    /**
     * Nickname of the player who has discard the tile
     */
    private final String nickname;

    /**
     * Event constructor
     * @param index of the tile in the list of available components
     * @param nickname of the player who has discard the tile
     */
    public DiscardSuccessEvent(int index, String nickname) {
        super(true);
        this.index = index;
        this.nickname = nickname;
    }

    /**
     * @return the index of the tile ijn the list of the available components
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the nickname of the player who discards the tile
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the message associated with the discard of the tile
     */
    @Override
    public String toString() {
        return "Discard success";
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
