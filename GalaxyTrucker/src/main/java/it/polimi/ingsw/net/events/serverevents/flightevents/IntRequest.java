package it.polimi.ingsw.net.events.serverevents.flightevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class IntRequest extends ServerEvent {
    public IntRequest() {
        super(ServerEventType.REQUEST, false);

    }
    @Override
    public String getMessage() {
        return "Number requested";
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
