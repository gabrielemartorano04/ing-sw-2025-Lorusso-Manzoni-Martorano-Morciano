package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class CoordRequest extends ServerEvent{
    public CoordRequest() {
        super(ServerEventType.REQUEST, false);
    }
    @Override
    public String getMessage() {
        return "Coordinates requested";
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
