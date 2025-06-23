package it.polimi.ingsw.net.events.serverevents.buildingevents;


import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event from server to client. A player tried to turn the hourglass but it's not possible at the moment
 */
public class TurnHourglassError extends ServerEvent {
    private String message;
    public TurnHourglassError() {
        super(false);
        message = " You can't turn the hourglass at the moment";
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
