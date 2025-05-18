package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class BooleanDecision extends CardEvent{
    private final String message;

    public BooleanDecision(ServerEventType eventType, boolean forAll, CardState cardState, String message) {
        super(eventType, forAll, cardState);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        // TODO: implementare in ServerEventHandlerVisitor
    }
}
