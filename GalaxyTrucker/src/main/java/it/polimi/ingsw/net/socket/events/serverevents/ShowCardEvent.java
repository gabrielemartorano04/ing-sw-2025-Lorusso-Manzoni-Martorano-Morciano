package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event from server to clients. It shows the new turned card.
 */
public class ShowCardEvent extends Event {

    private String message;
    private Card card;

    public ShowCardEvent(Card card) {
        super(EventType.SHOW_CARD);
        message = "Card description: "+ card.getDescription();
    }

    public String getMessage() {
        return message;
    }
    public Card getCard() {
        return card;
    }
}
