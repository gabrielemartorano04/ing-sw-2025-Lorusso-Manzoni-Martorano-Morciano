package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardEvent;

public class PlanetNotFreeExceptionEvent extends CardEvent {

    private String message;

    public PlanetNotFreeExceptionEvent(String message, boolean forAll) {
        //TODO inserire i valori corretti nel super
        super(ServerEventType.PLANET_NOT_FREE_ERROR, forAll, null, null);
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
