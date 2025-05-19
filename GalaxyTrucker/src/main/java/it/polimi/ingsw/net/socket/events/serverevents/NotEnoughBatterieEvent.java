package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardEvent;

public class NotEnoughBatterieEvent extends CardEvent {

    private String message;

    public NotEnoughBatterieEvent(String message, boolean forAll) {
        //TODO inserire le cose giuste nel super
        super(ServerEventType.NOT_ENOUGH_BATTERIES_ERROR, forAll, null, null);
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
