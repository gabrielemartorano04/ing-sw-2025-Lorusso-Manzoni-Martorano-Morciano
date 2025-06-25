package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

public class HumanCoordinates extends CardEvent {

    private final SpaceShip spaceShip;


    public HumanCoordinates(boolean forAll, String nickname,SpaceShip spaceShip) {
        super(forAll, ParserState.HUMAN_COORD, nickname);
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
