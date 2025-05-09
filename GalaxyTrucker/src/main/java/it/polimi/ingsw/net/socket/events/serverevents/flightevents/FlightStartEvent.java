package it.polimi.ingsw.net.socket.events.serverevents.flightevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class FlightStartEvent extends ServerEvent {
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
