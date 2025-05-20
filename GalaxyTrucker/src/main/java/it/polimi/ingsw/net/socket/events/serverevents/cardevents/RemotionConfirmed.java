package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class RemotionConfirmed extends CardEvent{
    private final String confirm;

    public RemotionConfirmed(CardState cardState, String nickname, String confirm) {
        super(ServerEventType.GOODS_REMOVED, false, cardState, nickname);
        this.confirm = confirm;
    }

    public String getConfirm() {
        return confirm;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
