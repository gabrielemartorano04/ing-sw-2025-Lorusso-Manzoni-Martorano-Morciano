package it.polimi.ingsw.net.events.serverevents.flightevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class UpdateCardStateEvent extends ServerEvent {
    private CardState state;
    public UpdateCardStateEvent(CardState state) {
        super(ServerEventType.UPDATE_STATE, true);
        this.state = state;
    }
    public CardState getState() {
        return state;
    }
    @Override
    public String getMessage() {
        return "Current Card State is: " + state.toString();
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
