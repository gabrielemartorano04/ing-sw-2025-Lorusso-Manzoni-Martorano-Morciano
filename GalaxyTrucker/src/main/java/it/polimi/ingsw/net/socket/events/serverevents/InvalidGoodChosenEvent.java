package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardEvent;

public class InvalidGoodChosenEvent extends CardEvent {

    private String message;

    public InvalidGoodChosenEvent(String message, boolean forAll) {
        super(ServerEventType.INVALID_GOODS_CHOSEN_ERROR, forAll, null, null);
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
