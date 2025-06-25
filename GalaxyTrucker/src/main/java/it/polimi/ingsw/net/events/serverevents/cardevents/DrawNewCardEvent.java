package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event triggered when a player draws a new card
 */
public class DrawNewCardEvent extends ServerEvent {

    /**
     * Nickname of the leader player
     */
    private final String leaderNickname;

    /**
     * Event constructor
     * @param leaderNickname nickname of the leader player
     */
    public DrawNewCardEvent(String leaderNickname) {
        super(true);
        this.leaderNickname= leaderNickname;
    }

    /**
     * @return the nickname of the leader player
     */
    public String getLeaderNickname() {
        return leaderNickname;
    }

    /**
     * @return the messages for the other players
     */
    public String showInfoForOthers() {
        return leaderNickname+" is about to draw the next card\n";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
