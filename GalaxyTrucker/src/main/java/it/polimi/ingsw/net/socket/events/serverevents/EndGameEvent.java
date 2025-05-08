package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

import java.util.Map;

/**
 * Server to client
 */
public class EndGameEvent extends ServerEvent {
    private String message;
    private String winner;
    private Map<String, Integer> scores;

    public EndGameEvent(String winner, Map<String, Integer> scores) {
        super(ServerEventType.END_GAME, true);
        message = "The game ended";
        this.winner = winner;
        this.scores = scores;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getWinner() {
        return winner;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
