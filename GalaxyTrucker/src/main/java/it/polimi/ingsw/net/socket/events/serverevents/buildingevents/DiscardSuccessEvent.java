package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class DiscardSuccessEvent extends ServerEvent {

    private String message;

    public DiscardSuccessEvent() {
        super(ServerEventType.DISCART_SUCCESS, false);
        message = "Discard success";
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
