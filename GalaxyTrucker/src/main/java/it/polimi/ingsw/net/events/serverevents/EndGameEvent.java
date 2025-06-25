package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;

import java.util.Map;

/**
 * Represents an event that signals the end of a game
 */
public class EndGameEvent extends ServerEvent {

    /**
     * Map containing the final scores of the game participants
     */
    private final Map<String, Integer> scores;

    /**
     * Event constructor
     * @param scores of the participants
     */
    public EndGameEvent(Map<String, Integer> scores) {
        super(true);
        this.scores = scores;
    }

    /**
     * @return the map of the final scores of the game participants
     */
    public Map<String, Integer> getScores() {
        return scores;
    }


    /**
     * @return the message associated with the end of the game
     */
    @Override
    public String getMessage() {
        return "The game ended";
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
