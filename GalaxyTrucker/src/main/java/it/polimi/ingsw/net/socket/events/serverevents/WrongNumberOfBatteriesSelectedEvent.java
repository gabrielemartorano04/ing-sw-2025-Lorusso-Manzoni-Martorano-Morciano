package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardEvent;

public class WrongNumberOfBatteriesSelectedEvent extends CardEvent {

    private String message;

    public WrongNumberOfBatteriesSelectedEvent(String message, boolean forAll) {
        super(ServerEventType.WRONG_NUMBER_OF_BATTERIES_SELECTED_ERROR, forAll, null, null);
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
