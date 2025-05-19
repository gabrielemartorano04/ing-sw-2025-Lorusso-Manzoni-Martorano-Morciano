package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardEvent;

public class InvalidCoordinatesEvent extends CardEvent {

    private String message;

    public InvalidCoordinatesEvent(String message, boolean forAll) {
        //TODO inserire le cose giuste nel super
        super(ServerEventType.INVALID_COORDINATES_ERROR, forAll, null, null);
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
