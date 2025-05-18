package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class ExecutionEvent extends CardEvent {

    public ExecutionEvent(boolean forAll, CardState cardState) {
        super(ServerEventType.EXECUTION, forAll, cardState, null);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        // non mi aspetto di fare nulla dentro questo call, infatti l'ExecutionEvent non viene mai dato al Client
        // serve solo per far capire al controller che deve chiamare cardEffect
    }
}
