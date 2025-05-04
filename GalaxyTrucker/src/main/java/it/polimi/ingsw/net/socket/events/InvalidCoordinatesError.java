package it.polimi.ingsw.net.socket.events;

/**
 * Error event from server to client. It signals that the player inserted invalid coordinates.
 */
public class InvalidCoordinatesError extends Event{
    private String username, message;
    public InvalidCoordinatesError(String username) {
        super(EventType.INVALID_COORDINATES_ERROR);
        this.username = username;
        message = username+" inserted invalid coordinates";
    }
    public String getUsername() {
        return username;
    }
    public String getMessage() {
        return message;
    }
}
