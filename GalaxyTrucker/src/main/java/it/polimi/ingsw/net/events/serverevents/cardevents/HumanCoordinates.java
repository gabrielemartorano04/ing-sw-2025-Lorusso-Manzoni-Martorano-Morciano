package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class HumanCoordinates extends CardEvent {

    public HumanCoordinates(ServerEventType eventType, boolean forAll, String nickname) {
        super(eventType, forAll, ParserState.HUMAN_COORD, nickname);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return getNickname()+" is choosing human coordinates ";
    }
}
