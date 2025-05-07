package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.cards.Card;

/**
 * Event from server to clients. It shows the new turned card.
 */
public class ShowCardEvent extends ServerEvent {

    private String message;
    private Card card;

    public ShowCardEvent(Card card) {
        super(ServerEventType.SHOW_CARD, true);
        message = "Card description: "+ card.getDescription();
    }
    @Override
    public String getMessage() {
        return message;
    }
    public Card getCard() {
        return card;
    }
}
