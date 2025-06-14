package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class HumanCoordinates extends CardEvent {

    private final SpaceShip spaceShip;

    private final SpaceShip previousSpaceShip;
    private final String previousNickName;

    public HumanCoordinates(ServerEventType eventType, boolean forAll, String nickname,SpaceShip spaceShip, SpaceShip previousSpaceShip, String previousNickName) {
        super(eventType, forAll, ParserState.HUMAN_COORD, nickname);
        this.spaceShip=spaceShip;
        this.previousSpaceShip=previousSpaceShip;
        this.previousNickName=previousNickName;
    }

    public SpaceShip getShip(){
        return spaceShip;
    }

    public SpaceShip getPreviousSpaceShip(){
        return previousSpaceShip;
    }

    public String getPreviousNickName(){
        return previousNickName;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return getNickname()+" is choosing human coordinates ";
    }
}
