package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class RemoveGoods extends CardEvent{
    private final ArrayList<Goods> goodsList;
    private final SpaceShip spaceShip;

    public RemoveGoods(boolean forAll, String nickname,SpaceShip spaceShip, ArrayList<Goods> goodsList, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.REMOVE_GOODS, nickname, truckers);
        this.goodsList = goodsList;
        this.spaceShip=spaceShip;
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public SpaceShip getShip(){
        return spaceShip;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers() {
        return getNickname()+" is deciding goods to remove.\n";
    }
}
