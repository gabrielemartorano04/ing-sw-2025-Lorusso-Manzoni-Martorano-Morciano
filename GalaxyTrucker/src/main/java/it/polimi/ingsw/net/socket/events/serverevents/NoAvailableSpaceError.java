package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Error event from server to client. It happens if a player try to join the game but there are already 4 players
 */
public class NoAvailableSpaceError extends ServerEvent {
    private String  message;
    public NoAvailableSpaceError(){
        super(ServerEventType.NO_AVAILABLE_SPACE);
        message = "There is no available space";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
