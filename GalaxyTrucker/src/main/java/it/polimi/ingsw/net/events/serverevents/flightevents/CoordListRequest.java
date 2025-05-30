package it.polimi.ingsw.net.events.serverevents.flightevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class CoordListRequest extends ServerEvent {
    public CoordListRequest() {
        super(ServerEventType.REQUEST, false);
    }
    @Override
    public String getMessage() {
        return "List of coordinates requested";
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
