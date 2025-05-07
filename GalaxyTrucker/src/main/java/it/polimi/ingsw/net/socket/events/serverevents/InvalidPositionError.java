package it.polimi.ingsw.net.socket.events.serverevents;

/**
 * Error event from server to client. It signals that the player gave an invalid position
 */
public class InvalidPositionError extends ServerEvent {
    private String username, message;
    public InvalidPositionError(String username) {
        super(ServerEventType.INVALID_POSITION_ERROR, false);
        this.username = username;
        message = username + " gave an invalid position";
    }
    public String getUsername() {
        return username;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
