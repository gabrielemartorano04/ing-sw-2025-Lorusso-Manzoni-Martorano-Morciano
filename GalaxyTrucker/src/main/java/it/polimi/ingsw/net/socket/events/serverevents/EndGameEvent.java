package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

import java.util.Map;

/**
 * Server to client
 */
public class EndGameEvent extends ServerEvent {
    private String message;
    private Map<String, Integer> scores;

    public EndGameEvent(Map<String, Integer> scores) {
        super(ServerEventType.END_GAME, true);
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
