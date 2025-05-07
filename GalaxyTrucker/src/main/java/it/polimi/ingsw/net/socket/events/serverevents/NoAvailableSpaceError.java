package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Error event from server to client. It happens if a player try to join the game but there are already 4 players
 */
public class NoAvailableSpaceError extends ServerEvent {
    private String username, message;
    public NoAvailableSpaceError(String username){
        super(ServerEventType.NO_AVAILABLE_SPACE);
        this.username = username;
        message = username + " tried to join but there is no available space";
    }
    public String getUsername() {
        return username;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
