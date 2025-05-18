package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public abstract class CardEvent extends ServerEvent {
    private final CardState cardState;
    public CardEvent(ServerEventType eventType, boolean forAll, CardState cardState) {
        super(eventType, forAll);
        this.cardState = cardState;
    }
    public CardState getCardState() {
        return cardState;
    }
}
