package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

import java.util.Map;

/**
 * Server to client
 */
public class EndGameEvent extends ServerEvent {
    private String message;
    private Map<String, Integer> scores;

    public EndGameEvent(Map<String, Integer> scores) {
        super(true);
        message = "The game ended";
        this.scores = scores;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
