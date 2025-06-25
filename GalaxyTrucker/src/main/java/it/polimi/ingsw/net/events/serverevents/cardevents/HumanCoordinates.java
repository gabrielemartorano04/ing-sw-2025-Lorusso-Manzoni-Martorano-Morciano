package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class HumanCoordinates extends CardEvent {

    private final SpaceShip spaceShip;


    public HumanCoordinates(boolean forAll, String nickname,SpaceShip spaceShip, ArrayList<Trucker> truckers ) {
        super(forAll, ParserState.HUMAN_COORD, nickname, truckers);
        this.spaceShip=spaceShip;
    }

    public SpaceShip getShip(){
        return spaceShip;
    }


    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return getNickname()+" is choosing human coordinates ";
    }
}
