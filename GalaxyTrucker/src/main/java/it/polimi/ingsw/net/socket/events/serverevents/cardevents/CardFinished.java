package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class CardFinished extends CardEvent {

    public CardFinished(boolean forAll, CardState cardState) {
        super(ServerEventType.CARD_FINISHED, forAll, cardState, null);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

    }
}
