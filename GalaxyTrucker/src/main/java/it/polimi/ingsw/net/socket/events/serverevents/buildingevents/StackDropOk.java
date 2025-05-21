package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class StackDropOk extends ServerEvent {
    public StackDropOk(String s, boolean forAll) {
        super(ServerEventType.STACK_DROP, forAll);

    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

    }
}
