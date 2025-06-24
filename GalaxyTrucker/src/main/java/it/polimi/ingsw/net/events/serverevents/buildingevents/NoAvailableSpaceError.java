package it.polimi.ingsw.net.events.serverevents.buildingevents;


import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Error event from server to client. It happens if a player try to join the game but there are already 4 players
 */
public class NoAvailableSpaceError extends ServerEvent {
    private String message;
    public NoAvailableSpaceError(){
        super(false);
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
