package it.polimi.ingsw.net.events.serverevents.flightevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class NotYourTurnError extends ServerEvent {
    private final String message;
    public NotYourTurnError(String message) {
        super(ServerEventType.NOT_YOUR_TURN, false);
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

}
