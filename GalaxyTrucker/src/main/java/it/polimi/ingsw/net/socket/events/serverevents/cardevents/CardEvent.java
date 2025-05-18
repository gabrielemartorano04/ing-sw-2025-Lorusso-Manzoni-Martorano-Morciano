package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public abstract class CardEvent extends ServerEvent {
    private final CardState cardState;
    private final String nickname;

    public CardEvent(ServerEventType eventType, boolean forAll, CardState cardState, String nickname) {
        super(eventType, forAll);
        this.cardState = cardState;
        this.nickname = nickname;
    }
    public CardState getCardState() {
        return cardState;
    }

    public String getNickname() {
        return nickname;
    }
}
