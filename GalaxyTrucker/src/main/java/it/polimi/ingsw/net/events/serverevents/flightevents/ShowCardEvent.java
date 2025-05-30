package it.polimi.ingsw.net.events.serverevents.flightevents;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

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

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
