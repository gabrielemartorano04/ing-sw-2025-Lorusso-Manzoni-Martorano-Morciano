package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class RemoveGoods extends CardEvent{
    private final ArrayList<Goods> goodsList;

    public RemoveGoods(boolean forAll, CardState cardState, String nickname, ArrayList<Goods> goodsList) {
        super(ServerEventType.REMOVE_GOODS, forAll, cardState, nickname);
        this.goodsList = goodsList;
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
