package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

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
