package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class StackDroppedEvent extends ClientEvent {

    int curr;

    public StackDroppedEvent(int i) {
        super();
        this.curr=i;
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }

    public int getCurr() {
        return curr;
    }
}
