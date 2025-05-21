package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class RemoveGoods extends CardEvent{
    private final ArrayList<Goods> goodsList;

    public RemoveGoods(boolean forAll, String nickname, ArrayList<Goods> goodsList) {
        super(ServerEventType.REMOVE_GOODS, forAll, ParserState.REMOVE_GOODS, nickname);
        this.goodsList = goodsList;
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers() {
        return getNickname()+" is deciding goods to remove.\n";
    }
}
