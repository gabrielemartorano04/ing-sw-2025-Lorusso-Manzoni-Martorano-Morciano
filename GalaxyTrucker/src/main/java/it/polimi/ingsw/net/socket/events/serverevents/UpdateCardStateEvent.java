package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class UpdateCardStateEvent extends Event {
    private CardState state;
    public UpdateCardStateEvent(CardState state) {
        super(EventType.UPDATE_STATE);
        this.state = state;
    }
    public CardState getState() {
        return state;
    }
    public String getMessage() {
        return "Current Card State is: " + state.toString();
    }
}
