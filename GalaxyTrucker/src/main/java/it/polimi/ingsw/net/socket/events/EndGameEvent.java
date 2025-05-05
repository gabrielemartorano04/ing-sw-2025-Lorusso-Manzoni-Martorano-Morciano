package it.polimi.ingsw.net.socket.events;

public class EndGameEvent extends Event {
    private String message;
    public EndGameEvent() {
        super(EventType.END_GAME);
        message = "The game ended";
    }

    public String getMessage() {
        return message;
    }
}
