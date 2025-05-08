package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class PlacedComponentRequest extends ServerEvent{
    private String message;

    public PlacedComponentRequest() {
        super(ServerEventType.REQUEST, false);
        message = "Placed component requested";
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
