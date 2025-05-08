package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

/**
 * Event from server to client. The spaceships are checked and correctly modified.
 */
public class CheckShipsEvent extends ServerEvent {
    private String message;
    public CheckShipsEvent() {
        super(ServerEventType.CHECK_SHIPS, false);
        message = "Spaceships are checked and correctly modified";
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
