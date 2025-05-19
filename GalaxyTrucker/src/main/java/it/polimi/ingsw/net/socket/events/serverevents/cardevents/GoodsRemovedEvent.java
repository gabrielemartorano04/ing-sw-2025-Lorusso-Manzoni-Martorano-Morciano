package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class GoodsRemovedEvent extends CardEvent{

    public GoodsRemovedEvent(CardState cardState, String nickname) {
        super(ServerEventType.GOODS_REMOVED, false, cardState, nickname);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        //TODO: FARE
    }
}
