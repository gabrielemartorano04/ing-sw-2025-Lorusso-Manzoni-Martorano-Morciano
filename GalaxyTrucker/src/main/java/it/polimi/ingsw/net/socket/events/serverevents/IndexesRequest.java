package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

import java.util.ArrayList;

public class IndexesRequest extends ServerEvent{
    private ArrayList<Goods> goodsToRemove;

    public IndexesRequest(ArrayList<Goods> goodsToRemove) {
        super(ServerEventType.REQUEST, false);
        this.goodsToRemove = goodsToRemove;
    }

    @Override
    public String getMessage() {
        return "List of indexes requested";
    }

    public ArrayList<Goods> getGoodsToRemove() {
        return goodsToRemove;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
