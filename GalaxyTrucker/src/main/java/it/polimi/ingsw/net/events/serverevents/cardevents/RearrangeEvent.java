package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class RearrangeEvent extends CardEvent{

    public RearrangeEvent(boolean forAll, String nickname, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.REARRANGE, nickname, truckers);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
    public String getInfoForOthers(){
        return getNickname()+" is deciding goods rearrangement.\n";
    }
}
