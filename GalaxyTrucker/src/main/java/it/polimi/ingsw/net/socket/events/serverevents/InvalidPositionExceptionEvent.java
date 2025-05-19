package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardEvent;

public class InvalidPositionExceptionEvent extends CardEvent {

    private String message;

    public InvalidPositionExceptionEvent(String message, boolean val) {
        //TODO correggi ingressi super
        super(ServerEventType.INVALID_POSITION_ERROR, false, null, null);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
