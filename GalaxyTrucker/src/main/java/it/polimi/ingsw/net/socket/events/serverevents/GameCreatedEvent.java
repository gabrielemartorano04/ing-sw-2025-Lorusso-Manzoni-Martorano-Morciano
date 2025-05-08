package it.polimi.ingsw.net.socket.events.serverevents;

public class GameCreatedEvent extends ServerEvent {
    private String message;
    public GameCreatedEvent(String message) {
        super(ServerEventType.GAME_CREATED, false);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
