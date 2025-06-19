package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class RemotionConfirmed extends CardEvent{
    private SpaceShip spaceShip;

    public RemotionConfirmed(ParserState parserState , String nickname,SpaceShip spaceShip) {
        super(ServerEventType.GOODS_REMOVED, true, parserState, nickname);
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
