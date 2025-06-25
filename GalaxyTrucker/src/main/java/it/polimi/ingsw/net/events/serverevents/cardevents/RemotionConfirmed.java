package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class RemotionConfirmed extends CardEvent{
    private final SpaceShip spaceShip;

    public RemotionConfirmed(ParserState parserState , String nickname,SpaceShip spaceShip) {
        super(true, parserState, nickname, new ArrayList<>());
        this.spaceShip=spaceShip;
    }

    public SpaceShip getShip(){
        return spaceShip;
    }

    public String getInfoForOthers(){
        return getNickname()+" received a remotion confirm";
    }


    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
