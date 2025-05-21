package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class RearrangeEvent extends CardEvent{

    public RearrangeEvent(boolean forAll, String nickname) {
        super(ServerEventType.REARRANGE, forAll, ParserState.REARRANGE, nickname);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
    public String getInfoForOthers(){
        return getNickname()+" is deciding goods rearrangement.\n";
    }
}
