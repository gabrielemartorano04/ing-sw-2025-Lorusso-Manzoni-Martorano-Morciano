package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

public class RearrangeEvent extends CardEvent{

    public RearrangeEvent(boolean forAll, String nickname) {
        super(forAll, ParserState.REARRANGE, nickname);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
    public String getInfoForOthers(){
        return getNickname()+" is deciding goods rearrangement.\n";
    }
}
