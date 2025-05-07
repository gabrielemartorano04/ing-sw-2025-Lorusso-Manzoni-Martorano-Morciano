package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.cards.CardState;

public class UpdateCardStateEvent extends ServerEvent {
    private CardState state;
    public UpdateCardStateEvent(CardState state) {
        super(ServerEventType.UPDATE_STATE);
        this.state = state;
    }
    public CardState getState() {
        return state;
    }
    @Override
    public String getMessage() {
        return "Current Card State is: " + state.toString();
    }
}
