package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class StackDropOk extends ServerEvent {
    public StackDropOk(String s, boolean forAll) {
        super(ServerEventType.STACK_DROP, forAll);

    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
