package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class HourglassTurnEvent extends ServerEvent {
    private String message;

    public HourglassTurnEvent() {
        super(ServerEventType.TURN_HOURGLASS, true);
        message = "Hourglass turned";
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
