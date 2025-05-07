package it.polimi.ingsw.net.socket.events.serverevents;

import java.util.Map;

/**
 * Server to client
 */
public class EndGameEvent extends ServerEvent {
    private String message;
    private String winner;
    private Map<String, Integer> scores;

    public EndGameEvent(String winner, Map<String, Integer> scores) {
        super(ServerEventType.END_GAME);
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
}
