package it.polimi.ingsw.net.socket.events.serverevents;

public class OkEvent extends ServerEvent {
    private final String message;
    public OkEvent(String message) {
        super(ServerEventType.OK);
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
