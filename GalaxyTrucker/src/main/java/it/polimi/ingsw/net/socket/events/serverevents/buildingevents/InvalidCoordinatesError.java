package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

/**
 * Error event from server to client. It signals that the player inserted invalid coordinates.
 */
public class InvalidCoordinatesError extends ServerEvent {
    private String username, message;
    public InvalidCoordinatesError(String username) {
        super(ServerEventType.INVALID_COORDINATES_ERROR, false);
        this.username = username;
        message = username+" inserted invalid coordinates";
    }
    public String getUsername() {
        return username;
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
