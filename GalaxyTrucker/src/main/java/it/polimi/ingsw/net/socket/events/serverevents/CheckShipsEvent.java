package it.polimi.ingsw.net.socket.events.serverevents;

/**
 * Event from server to client. The spaceships are checked and correctly modified.
 */
public class CheckShipsEvent extends ServerEvent {
    private String message;
    public CheckShipsEvent() {
        super(ServerEventType.CHECK_SHIPS);
        message = "Spaceships are checked and correctly modified";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
