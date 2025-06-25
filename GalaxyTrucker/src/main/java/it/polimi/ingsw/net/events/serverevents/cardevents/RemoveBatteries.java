package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class RemoveBatteries extends CardEvent{
    private final int batteriesToRemove;
    private final SpaceShip spaceShip;

    public RemoveBatteries(boolean forAll, String nickname, SpaceShip spaceShip, int batteriesToRemove, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.REMOVE_BATTERIES, nickname, truckers);
        this.batteriesToRemove = batteriesToRemove;
        this.spaceShip=spaceShip;
    }

    public int getBatteriesToRemove() {
        return batteriesToRemove;
    }

    public SpaceShip getShip() {
        return spaceShip;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return getNickname()+" is deciding where to remove batteries";
    }
}
