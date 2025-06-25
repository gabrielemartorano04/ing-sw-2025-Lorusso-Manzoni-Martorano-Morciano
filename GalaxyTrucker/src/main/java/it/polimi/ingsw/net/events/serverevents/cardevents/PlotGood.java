package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class PlotGood extends CardEvent{
    private final Goods good;
    private final SpaceShip spaceShip;

    public PlotGood(boolean forAll, String nickname,SpaceShip spaceShip, Goods good, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.PLOT_GOOD, nickname, truckers);
        this.good = good;
        this.spaceShip=spaceShip;
    }

    public Goods getGood() {
        return good;
    }

    public SpaceShip getShip(){
        return spaceShip;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return getNickname()+" is deciding which goods to gain\n";
    }
}
