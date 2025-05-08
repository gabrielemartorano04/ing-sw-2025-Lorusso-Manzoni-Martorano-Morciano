package it.polimi.ingsw.net.socket.events.serverevents;


import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

/**
 * Error event from server to client. It happens if a player try to join the game but there are already 4 players
 */
public class NoAvailableSpaceError extends ServerEvent {
    private String message;
    public NoAvailableSpaceError(){
        super(ServerEventType.NO_AVAILABLE_SPACE, false);
        message = "There is no available space";
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
