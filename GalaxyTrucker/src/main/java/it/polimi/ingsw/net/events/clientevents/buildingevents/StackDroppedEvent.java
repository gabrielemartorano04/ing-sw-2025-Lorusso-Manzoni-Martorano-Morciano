package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class StackDroppedEvent extends ClientEvent {

    int curr;

    public StackDroppedEvent(int i) {
        super();
        this.curr=i;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }

    public int getCurr() {
        return curr;
    }
}
