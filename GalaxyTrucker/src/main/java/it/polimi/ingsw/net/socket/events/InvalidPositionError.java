package it.polimi.ingsw.net.socket.events;

/**
 * Error event from server to client. It signals that the player gave an invalid position
 */
public class InvalidPositionError extends Event{
    private String username, message;
    public InvalidPositionError(String username) {
        super(EventType.INVALID_POSITION_ERROR);
        this.username = username;
        message = username + " gave an invalid position";
    }
    public String getUsername() {
        return username;
    }
    public String getMessage() {
        return message;
    }
}
