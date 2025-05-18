package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class BooleanDecision extends CardEvent{

    public BooleanDecision(ServerEventType eventType, boolean forAll, CardState cardState) {
        super(eventType, forAll, cardState);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        // TODO: implementare in ServerEventHandlerVisitor
    }
}
