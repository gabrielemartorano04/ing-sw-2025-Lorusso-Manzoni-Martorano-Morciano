package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class FlightStartEvent extends ServerEvent{
    private String message;

    public FlightStartEvent() {
        super(ServerEventType.START_FLIGHT, true);
        message = "Flight started";
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
